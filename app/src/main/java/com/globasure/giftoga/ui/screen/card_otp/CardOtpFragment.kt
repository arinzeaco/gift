package com.globasure.giftoga.ui.screen.card_otp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.BuySendType
import com.globasure.giftoga.constant.PaymentType
import com.globasure.giftoga.databinding.CardOtpFragBinding
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.CardOtpRequest
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.main_fragment.home.HomeFragment
import com.globasure.giftoga.utils.AppUtils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@AndroidEntryPoint
class CardOtpFragment : BaseFragment<CardOtpFragBinding, CardOtpViewModel>(), CardOtpView {

    private val cardOtpViewModel: CardOtpViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.card_otp_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CardOtpViewModel = cardOtpViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.enter_otp)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun moveToPin(reference: String) {
        val buyGiftcardRequest = BuyGiftcardRequest(
            payment_token = reference,
            buyType = BuySendType.SELF.type,
            friendName = "",
            friendEmail = "",
            friendPhone = "",
            message = ""
        )
        val fragment = newInstance(
            reference = reference,
            buyGiftcardRequest = buyGiftcardRequest,
            transactionType = requireArguments().getString(TRANSACTION_TYPE) ?: PaymentType.COMPLETE_ORDER.type
        )
        fragmentNavigation.pushFragment(fragment)
    }

    override fun addCardSuccess(mess: String) {
        AppUtils().successDialog(requireContext(), mess, ::moveToHome)
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        viewDataBinding.sendBtn.setOnClickListener {
            val cardOptRequest = CardOtpRequest(
                reference = requireArguments().getString(REFERENCE),
                card_otp = viewDataBinding.otpEdt.text.toString()
            )
            cardOtpViewModel.cardOtp(cardOptRequest, requireArguments().getString(TRANSACTION_TYPE))
        }
    }

    override fun fundSuccess(mess: String) {
        AppUtils().successDialog(requireContext(), mess, ::moveToHome)
    }

    private fun moveToHome() {
        val fragment = HomeFragment.newInstance()
        (getBaseActivity() as MainActivity).pushFragment(fragment)
    }

    companion object {
        private const val REFERENCE = "reference"
        private const val TRANSACTION_TYPE = "transaction_type"
        private const val BUY_GIFT_CARD_REQUEST = "buyGiftcardRequest"
        fun newInstance(
            reference: String, buyGiftcardRequest: BuyGiftcardRequest?, transactionType: String
        ): CardOtpFragment {
            val fragment = CardOtpFragment()
            val args = Bundle()
            args.putString(REFERENCE, reference)
            args.putString(TRANSACTION_TYPE, transactionType)
            args.putString(BUY_GIFT_CARD_REQUEST, Gson().toJson(buyGiftcardRequest))

            fragment.arguments = args
            return fragment
        }
    }
}
