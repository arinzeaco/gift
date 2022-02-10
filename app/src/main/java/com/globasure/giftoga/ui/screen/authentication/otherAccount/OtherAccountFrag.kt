package com.globasure.giftoga.ui.screen.authentication.otherAccount

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AccountType
import com.globasure.giftoga.databinding.OtherAccountTypeFragBinding
import com.globasure.giftoga.network.request.SignUpOtherRequest
import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.network.response.StateData
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.dialog.country.DialogListCountry
import com.globasure.giftoga.ui.dialog.state.DialogListState
import com.globasure.giftoga.ui.screen.authentication.AuthenticationActivity
import com.globasure.giftoga.ui.screen.authentication.verify.VerifyAccountFrag
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OtherAccountFrag : BaseFragment<OtherAccountTypeFragBinding, OtherAccountViewModel>(), OtherAccountView {

    private var isOn: Boolean = false
    private var allowToCreateAccount: Boolean = false
    private lateinit var countryDialog: DialogListCountry
    private lateinit var stateDialog: DialogListState
    private var accountType: String = ""
    private var errorMessage: String? = null

    private val otherAccountViewModel: OtherAccountViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.other_account_type_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): OtherAccountViewModel = otherAccountViewModel

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun setToolBar(view: View) {
        //do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
        otherAccountViewModel.getCountryList()
        accountType = requireArguments().getString(ACCOUNT_TYPE) ?: AccountType.BUSINESS.type
        Timber.i("Sign up $accountType")
    }

    override fun initView(view: View) {
        viewDataBinding.passwordToggle.setOnClickListener {
            if (isOn) {
                isOn = false
                viewDataBinding.passwordToggle.setImageResource(R.drawable.ic_visibility_off_black_24dp)
                viewDataBinding.passwordEdt.getInputView().transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                isOn = true
                viewDataBinding.passwordToggle.setImageResource(R.drawable.ic_visibility_black_24dp)
                viewDataBinding.passwordEdt.getInputView().transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        viewDataBinding.termCheckBox.setOnChangeListener {
            if (it) {
                allowToCreateAccount = true
                viewDataBinding.createAccountBtn.setBackgroundResource(R.drawable.button_round)
            } else {
                allowToCreateAccount = false
                viewDataBinding.createAccountBtn.setBackgroundResource(R.drawable.button_round_light_blue)
            }
        }

        viewDataBinding.passwordEdt.getInputView().doAfterTextChanged {
            renderSomeCustomPrompt()
        }
    }

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.createAccountBtn)
    fun onCreateAccountClick() {
        if (allowToCreateAccount) {
            if (validateForm() && viewDataBinding.tvPasswordInfo.text.isEmpty()) {
                val signUpOtherRequest = SignUpOtherRequest(
                    firstName = viewDataBinding.firstNameEdt.text.toString(),
                    lastName = viewDataBinding.lastNameEdt.text.toString(),
                    businessName = viewDataBinding.businessNameEdt.text.toString(),
                    email = viewDataBinding.emailEdt.text.toString(),
                    mobile = viewDataBinding.mobileEdt.text.toString(),
                    country = viewDataBinding.countryEdt.text.toString(),
                    state = viewDataBinding.stateEdt.text.toString(),
                    city = viewDataBinding.cityEdt.text.toString(),
                    address = viewDataBinding.addressEdt.text.toString(),
                    postalCode = "",
                    password = viewDataBinding.passwordEdt.getInputView().text.toString()
                )

                when (accountType) {
                    AccountType.BUSINESS.type -> {
                        otherAccountViewModel.doSignUpBusiness(signUpOtherRequest)
                    }
                    AccountType.AGENT.type -> {
                        otherAccountViewModel.doSignUpAgent(signUpOtherRequest)
                    }
                    AccountType.MERCHANT.type -> {
                        otherAccountViewModel.doSignUpSignUpMerchant(signUpOtherRequest)
                    }
                }
            } else {
                if (viewDataBinding.tvPasswordInfo.text.isEmpty() && !validateForm()) {
                    if (errorMessage != null) {
                        showSnackBar(errorMessage!!)
                        errorMessage = null
                    } else {
                        showSnackBar(getString(R.string.default_validation))
                    }
                } else if (viewDataBinding.tvPasswordInfo.text.isNotEmpty() && validateForm()) {
                    Timber.d("Show Password Error Message!")
                } else {
                    showSnackBar(errorMessage!!)
                }
            }
        } else {
            showSnackBar(getString(R.string.agree_terms_of_use))
        }
    }

    private fun validateForm(): Boolean {
        val isFirstNamePassed = viewDataBinding.firstNameEdt.validator()
            .noNumbers()
            .minLength(2)
            .addErrorCallback {
                errorMessage = getString(R.string.validate_first_name)
            }
            .check()

        val isLastNamePassed = viewDataBinding.lastNameEdt.validator()
            .noNumbers()
            .minLength(2)
            .addErrorCallback {
                if (errorMessage == null)
                    errorMessage = getString(R.string.validate_last_name)
            }
            .check()

        val isBusinessPassed = viewDataBinding.businessNameEdt.text.isNotBlank()
        if (!isBusinessPassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_business)
        }

        val isEmailPassed = viewDataBinding.emailEdt.validEmail {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_email)
        }

        val isPhoneNumberPassed = viewDataBinding.mobileEdt.validator()
            .validNumber()
            .minLength(10)
            .addErrorCallback {
                if (errorMessage == null)
                    errorMessage = getString(R.string.validate_phone_number)
            }
            .check()

        val isCountryPassed = !viewDataBinding.countryEdt.text.toString().contains(getString(R.string.country))
        if (!isCountryPassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_country)
        }

        val isStatePassed = !viewDataBinding.stateEdt.text.toString().contains(getString(R.string.state))
        if (!isStatePassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_country)
        }

        val isCityPassed = viewDataBinding.cityEdt.text.isNotBlank()
        if (!isCityPassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_city)
        }

        val isAddressPassed = viewDataBinding.addressEdt.text.isNotBlank()
        if (!isAddressPassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_address)
        }

        val isPasswordPassed = viewDataBinding.passwordEdt.getInputView().text.isNotBlank()
        if (!isPasswordPassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_password)
        }

        return isFirstNamePassed && isLastNamePassed && isBusinessPassed
                && isEmailPassed && isPhoneNumberPassed && isCountryPassed
                && isStatePassed && isCityPassed && isAddressPassed && isPasswordPassed
    }

    override fun signUpSuccess() {
        val fragment = VerifyAccountFrag.newInstance(
            viewDataBinding.emailEdt.text.toString(),
            viewDataBinding.firstNameEdt.text.toString()
        )
        (getBaseActivity() as AuthenticationActivity).pushFragment(fragment)
    }

    override fun getCountryList(list: List<CountryData>) {
        countryDialog = DialogListCountry(list as ArrayList<CountryData>)
        viewDataBinding.countryEdt.setOnClickListener {
            handleChooseCountryCLick()
        }
    }

    override fun getStateList(list: List<StateData>) {
        stateDialog = DialogListState(list as ArrayList<StateData>)
        viewDataBinding.stateEdt.setOnClickListener {
            handleChooseStateCLick()
        }
    }

    private fun handleChooseCountryCLick() {
        countryDialog.itemClick = {
            viewDataBinding.countryEdt.text = it.name
            countryDialog.dismiss()
            otherAccountViewModel.getStateByCountry(it.name)
        }

        countryDialog.onCloseClick = {
            countryDialog.dismiss()
        }

        countryDialog.show(this.childFragmentManager, TAG)
    }

    private fun handleChooseStateCLick() {
        stateDialog.itemClick = {
            viewDataBinding.stateEdt.text = it
            stateDialog.dismiss()
        }

        stateDialog.onCloseClick = {
            stateDialog.dismiss()
        }

        stateDialog.show(this.childFragmentManager, TAG)
    }

    private fun renderSomeCustomPrompt() {

        val info = StringBuilder()
        var low = false
        for (i in viewDataBinding.passwordEdt.getInputView().text.toString()) {
            if (i.isLowerCase()) {
                low = true
            }
        }
        if (!low) {
            info.append(requireContext().getString(R.string.password_lowercase))
        }
        if (viewDataBinding.passwordEdt.getUppercaseScore() == 0) {
            info.append(requireContext().getString(R.string.password_uppercase))
        }
        if (viewDataBinding.passwordEdt.getSymbolScore() == 0) {
            info.append(requireContext().getString(R.string.password_special_character))
        }
        if (viewDataBinding.passwordEdt.getNumberScore() == 0) {
            info.append(requireContext().getString(R.string.password_number))
        }
        if (viewDataBinding.passwordEdt.getCharacterScore() < 24) {
            info.append(requireContext().getString(R.string.password_length))
        }
        viewDataBinding.tvPasswordInfo.text = info.toString()
    }

    companion object {
        const val TAG = "OtherAccountFrag"
        private const val ACCOUNT_TYPE = "account_type"
        const val SCREEN_TITLE = "Other Account"

        fun newInstance(accountType: String): OtherAccountFrag {
            val args = Bundle()
            args.putString(ACCOUNT_TYPE, accountType)
            val fragment = OtherAccountFrag()
            fragment.arguments = args
            return fragment
        }
    }
}
