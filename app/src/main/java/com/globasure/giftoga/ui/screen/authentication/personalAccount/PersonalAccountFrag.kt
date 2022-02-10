package com.globasure.giftoga.ui.screen.authentication.personalAccount


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
import com.globasure.giftoga.databinding.PersonalAccountFragBinding
import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.dialog.country.DialogListCountry
import com.globasure.giftoga.ui.screen.authentication.AuthenticationActivity
import com.globasure.giftoga.ui.screen.authentication.verify.VerifyAccountFrag
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PersonalAccountFrag : BaseFragment<PersonalAccountFragBinding, PersonalAccountViewModel>(), PersonalAccountView {

    private var isOn: Boolean = false
    private var allowToCreateAccount: Boolean = false
    private lateinit var countryDialog: DialogListCountry
    private var errorMessage: String? = null

    private val personalAccountViewModel: PersonalAccountViewModel by viewModels()

    override fun getLayoutId() = R.layout.personal_account_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): PersonalAccountViewModel = personalAccountViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun setToolBar(view: View) {
        //do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
        personalAccountViewModel.getCountryList()
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

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.createAccountBtn)
    fun onCreateAccountClick() {
        if (allowToCreateAccount) {
            if (validateForm() && viewDataBinding.tvPasswordInfo.text.isEmpty()) {
                var phoneNumber = viewDataBinding.phoneEdt.text.toString()
                if (phoneNumber.contains("+")) {
                    phoneNumber = phoneNumber.replace("+", "")
                }
                personalAccountViewModel.doSignUp(
                    viewDataBinding.firstNameEdt.text.toString(),
                    viewDataBinding.lastNameEdt.text.toString(),
                    viewDataBinding.emailEdt.text.toString(),
                    viewDataBinding.passwordEdt.getInputView().text.toString(),
                    phoneNumber,
                    viewDataBinding.countryEdt.text.toString()
                )
            } else {
                if (viewDataBinding.tvPasswordInfo.text.isEmpty() && !validateForm()) {
                    if (errorMessage != null) {
                        showSnackBar(errorMessage!!)
                        errorMessage = null
                    } else {
                        showSnackBar(getString(R.string.default_validation))
                    }
                } else if (viewDataBinding.tvPasswordInfo.text.isNotEmpty() && validateForm()) {
                    Timber.i("Show Password Error Message!")
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
        val isEmailPassed = viewDataBinding.emailEdt.validEmail {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_email)
        }

        val isCountryPassed = !viewDataBinding.countryEdt.text.toString().contains(getString(R.string.country))
        if (!isCountryPassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_country)
        }

        val isPhoneNumberPassed = viewDataBinding.phoneEdt.validator()
            .minLength(10)
            .addErrorCallback {
                if (errorMessage == null)
                    errorMessage = getString(R.string.validate_phone_number)
            }
            .check()

        val isPasswordPassed = viewDataBinding.passwordEdt.getInputView().text.isNotBlank()
        if (!isPasswordPassed) {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_password)
        }

        return isFirstNamePassed && isLastNamePassed && isEmailPassed &&
                isPhoneNumberPassed && isCountryPassed && isPasswordPassed
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

    @SuppressLint("SetTextI18n")
    private fun handleChooseCountryCLick() {
        countryDialog.itemClick = {
            viewDataBinding.countryEdt.text = it.name
            viewDataBinding.phoneEdt.setText("+" + it.dialCode)
            countryDialog.dismiss()
        }

        countryDialog.onCloseClick = {
            countryDialog.dismiss()
        }

        countryDialog.show(this.childFragmentManager, TAG)
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
        const val TAG = "PersonalAccountFrag"
        const val SCREEN_TITLE = "Personal Account"

        fun newInstance(): PersonalAccountFrag {
            return PersonalAccountFrag()
        }
    }
}
