package com.globasure.giftoga.ui.screen.send_to_friend

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.BuySendType
import com.globasure.giftoga.databinding.SendToFriendFragBinding
import com.globasure.giftoga.network.request.SendToFriendRequest
import com.globasure.giftoga.network.response.ChargeFeesData
import com.globasure.giftoga.network.response.Merchant
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.complete_order.CompleteOrderFragment
import com.google.gson.Gson
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber


@AndroidEntryPoint
class SendToFriendFragment : BaseFragment<SendToFriendFragBinding, SendToFriendViewModel>(),
    SendToFriendFragView {

    private val sendToFriendViewModel: SendToFriendViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.send_to_friend_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SendToFriendViewModel = sendToFriendViewModel

    private var errorMessage: String? = null

    lateinit var merchant: Merchant

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.recipient_information)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }
        merchant = Gson().fromJson(arguments?.getString(MERCHANT), Merchant::class.java)

        viewDataBinding.continueBtn.setOnClickListener {
            viewDataBinding.recipientsNameEdt.setText("Arinze")
            viewDataBinding.recipientsEmailEdt.setText("aobi@globasure.net")
            viewDataBinding.phoneNumberEdt.setText("08061105628")
            if (validateForm()) {
                val request = SendToFriendRequest(
                    buyType = BuySendType.FRIEND.type,
                    friendName = viewDataBinding.recipientsNameEdt.text.toString(),
                    friendEmail = viewDataBinding.recipientsEmailEdt.text.toString(),
                    friendPhone = viewDataBinding.phoneNumberEdt.text.toString(),
                    message = viewDataBinding.recipientsMessageEdt.text.toString()
                )

                val fragment = CompleteOrderFragment.newInstance(
                    merchant = merchant,
                    amount = arguments?.getString(AMOUNT)!!,
                    quantity = arguments?.getString(QUANTITY)!!,
                    sendToFriendRequest = request
                )
                fragmentNavigation.pushFragment(fragment)
            } else {
                if (errorMessage != null) {
                    showSnackBar(errorMessage!!)
                    errorMessage = null
                } else {
                    showSnackBar(getString(R.string.default_validation))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    @SuppressLint("SetTextI18n")
    override fun chargeFeesSuccess(data: ChargeFeesData) {

    }

    private fun validateForm(): Boolean {
        val isNamePassed = viewDataBinding.recipientsNameEdt.validator()
            .noNumbers()
            .minLength(2)
            .addErrorCallback {
                errorMessage = getString(R.string.validate_first_name)
            }
            .check()

        val isEmailPassed = viewDataBinding.recipientsEmailEdt.validEmail {
            if (errorMessage == null)
                errorMessage = getString(R.string.validate_email)
        }

        val isPhoneNumberPassed = viewDataBinding.phoneNumberEdt.validator()
            .validNumber()
            .minLength(10)
            .addErrorCallback {
                if (errorMessage == null)
                    errorMessage = getString(R.string.validate_phone_number)
            }
            .check()

        return isNamePassed && isEmailPassed && isPhoneNumberPassed
    }

    companion object {
        private const val MERCHANT = "merchant"
        private const val AMOUNT = "amount"
        private const val QUANTITY = "quantity"

        fun newInstance(merchant: Merchant, amount: String, quantity: String): SendToFriendFragment {
            val fragment = SendToFriendFragment()
            val gson = Gson()
            val args = Bundle()
            args.putString(MERCHANT, gson.toJson(merchant))
            args.putString(AMOUNT, amount)
            args.putString(QUANTITY, quantity)
            fragment.arguments = args
            return fragment
        }
    }
}
