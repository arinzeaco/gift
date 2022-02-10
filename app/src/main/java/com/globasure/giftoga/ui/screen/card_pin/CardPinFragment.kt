package com.globasure.giftoga.ui.screen.card_pin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.BuySendType
import com.globasure.giftoga.constant.PaymentType
import com.globasure.giftoga.databinding.CardPinFragBinding
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.CardPinRequest
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.card_otp.CardOtpFragment
import com.globasure.giftoga.utils.AppUtils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@AndroidEntryPoint
class CardPinFragment : BaseFragment<CardPinFragBinding, CardPinViewModel>(), CardPinView {

    private val cardPinViewModel: CardPinViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.card_pin_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CardPinViewModel = cardPinViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.enter_pin)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun moveToOtp(reference: String) {
        val buyGiftcardRequest = BuyGiftcardRequest(
            payment_token = reference,
            buyType = BuySendType.SELF.type,
            friendName = "",
            friendEmail = "",
            friendPhone = "",
            message = ""
        )
        val fragment = CardOtpFragment.newInstance(
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
            val cardPinRequest = CardPinRequest(
                reference = requireArguments().getString(REFERENCE),
                card_pin = viewDataBinding.pinEdt.text.toString()
            )
            cardPinViewModel.cardPin(cardPinRequest, requireArguments().getString(TRANSACTION_TYPE))
        }
    }

    override fun fundSuccess(mess: String) {
        AppUtils().successDialog(requireContext(), mess, ::moveToHome)
    }

    private fun moveToHome() {
        (getBaseActivity() as MainActivity).fragNavController.clearStack()
        (getBaseActivity() as MainActivity).switchBackToHomePage()
    }

    companion object {
        private const val REFERENCE = "reference"
        private const val TRANSACTION_TYPE = "transaction_type"
        private const val BUY_GIFT_CARD_REQUEST = "buyGiftcardRequest"
        fun newInstance(
            reference: String, buyGiftcardRequest: BuyGiftcardRequest?, transactionType: String
        ): CardPinFragment {
            val fragment = CardPinFragment()
            val args = Bundle()
            args.putString(REFERENCE, reference)
            args.putString(TRANSACTION_TYPE, transactionType)
            args.putString(BUY_GIFT_CARD_REQUEST, Gson().toJson(buyGiftcardRequest))

            fragment.arguments = args
            return fragment
        }
    }
}
