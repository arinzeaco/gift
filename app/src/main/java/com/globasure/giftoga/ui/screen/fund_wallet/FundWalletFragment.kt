package com.globasure.giftoga.ui.screen.fund_wallet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.BuySendType
import com.globasure.giftoga.constant.ChargeType
import com.globasure.giftoga.constant.PaymentType
import com.globasure.giftoga.databinding.FundWalletFragBinding
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.network.response.CardData
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.card_pin.CardPinFragment
import com.globasure.giftoga.ui.screen.settings_tab.debit_card.DebitCardsFragment
import com.globasure.giftoga.utils.AppUtils
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.android.synthetic.main.dialog_card_options.view.add_card
import kotlinx.android.synthetic.main.dialog_card_options.view.existing_card
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@AndroidEntryPoint
class FundWalletFragment : BaseFragment<FundWalletFragBinding, FundWalletViewModel>(), FundWalletView {

    private val fundWalletViewModel: FundWalletViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.fund_wallet_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): FundWalletViewModel = fundWalletViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    private var isValidCard: Boolean = false
    private var isReady: Boolean = false
    private var selectedCardToken: String? = null

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        viewDataBinding.selectMethodTv.text = getString(R.string.add_card)

        setFragmentResultListener("SELECTED_CARD") { _, bundle ->
            val selectedCard = bundle.getParcelable<CardData>("selected-card")!!
            selectedCardToken = selectedCard.token

            viewDataBinding.selectMethodTv.text = getString(R.string.choose_from_existing_card)
            viewDataBinding.selectedCardLayout.visible()
            viewDataBinding.cardPaymentLayout.visible()
            viewDataBinding.cardNumberTv.gone()
            viewDataBinding.cardNumberEd.gone()
            viewDataBinding.cardNumberEd.setText(FAKE_CARD)

            viewDataBinding.cardBrand.text = selectedCard.cardBrand
            viewDataBinding.cardNumber.text = selectedCard.cardNumber
        }

        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        validateFields()
        viewDataBinding.makePaymentBtn.setOnClickListener {
            if (isReady) {
                val chargeCardRequest = if (selectedCardToken != null) {
                    ChargeCardRequest(
                        payLink = null,
                        saveCard = false,
                        cardToken = selectedCardToken,
                        cardNumber = null,
                        expiryMonth = viewDataBinding.expiryDate.text.toString().substring(0, 2),
                        expiryYear = viewDataBinding.expiryDate.text.toString().substring(3, 5),
                        cvv = viewDataBinding.cvvEd.text.toString()
                    )
                } else {
                    ChargeCardRequest(
                        payLink = null,
                        saveCard = false,
                        cardToken = null,
                        cardNumber = viewDataBinding.cardNumberEd.text.toString().replace(" ", ""),
                        expiryMonth = viewDataBinding.expiryDate.text.toString().substring(0, 2),
                        expiryYear = viewDataBinding.expiryDate.text.toString().substring(3, 5),
                        cvv = viewDataBinding.cvvEd.text.toString()
                    )
                }
                val request = GetLinkForPaymentRequest(
                    amount = viewDataBinding.amountEdt.text.toString(),
                    chargeType = ChargeType.DEPOSIT.type
                )

                fundWalletViewModel.handlePayLink(request, chargeCardRequest)
            } else {
                showSnackBar(getString(R.string.default_validation))
            }
        }
    }

    override fun fundSuccess(mess: String) {
        AppUtils().successDialog(requireContext(), mess, ::moveToHome)
    }

    override fun fundFailed(mess: String) {
        AppUtils().failedDialog(requireContext(), mess)
    }

    private fun moveToHome() {
        (getBaseActivity() as MainActivity).fragNavController.clearStack()
        (getBaseActivity() as MainActivity).switchBackToHomePage()
    }

    override fun moveToCardPin(reference: String) {
        val buyGiftcardRequest = BuyGiftcardRequest(
            payment_token = "",
            buyType = BuySendType.SELF.type,
            friendName = "",
            friendEmail = "",
            friendPhone = "",
            message = ""
        )
        val fragment = CardPinFragment.newInstance(
            reference = reference,
            buyGiftcardRequest = buyGiftcardRequest,
            transactionType = PaymentType.FUND_WALLET.type
        )
        fragmentNavigation.pushFragment(fragment)
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.card_payment_options)
    fun onChooseCardOptionClick() {
        openCardOptions(requireContext(), {
            //Choose fill in form
            selectedCardToken = null
            viewDataBinding.selectMethodTv.text = getString(R.string.add_card)
            viewDataBinding.selectedCardLayout.gone()
            viewDataBinding.cardPaymentLayout.visible()
            viewDataBinding.cardNumberTv.visible()
            viewDataBinding.cardNumberEd.visible()
            viewDataBinding.cardNumberEd.setText("")
            validateFields()
        }, {
            //Choose existing cards
            viewDataBinding.selectMethodTv.text = getString(R.string.choose_from_existing_card)
            viewDataBinding.selectedCardLayout.visible()
            viewDataBinding.cardPaymentLayout.visible()
            viewDataBinding.cardNumberTv.gone()
            viewDataBinding.cardNumberEd.gone()
            navigateExistingCards()
        })
    }

    private fun navigateExistingCards() {
        val fragment = DebitCardsFragment.newInstance("Tap To Select A Card", true)
        fragmentNavigation.pushFragment(fragment)
    }

    @SuppressLint("InflateParams")
    fun openCardOptions(context: Context, chooseAddCard: () -> Unit, chooseExistingCard: () -> Unit) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.transparent_bottom_sheet_dialog_theme)
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_card_options, null)
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(customLayout)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        customLayout.add_card.setOnClickListener {
            chooseAddCard()
            bottomSheetDialog.dismiss()
        }
        customLayout.existing_card.setOnClickListener {
            chooseExistingCard()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        getBaseActivity()?.setStatusBarColor(R.color.white)
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    private fun validateFields() {
        val amountObservable = RxTextView.textChanges(viewDataBinding.amountEdt)
        val cardNumberObservable = RxTextView.textChanges(viewDataBinding.cardNumberEd)
        val expiryDateObservable = RxTextView.textChanges(viewDataBinding.expiryDate)
        val cvvObservable = RxTextView.textChanges(viewDataBinding.cvvEd)
        val isReadyObservable: Observable<Boolean> = Observable.combineLatest(
            amountObservable,
            cardNumberObservable,
            expiryDateObservable,
            cvvObservable,
            { _, cardNumber, myExpiryDate, cvv ->
                if (cardNumber.isNotEmpty()) {
                    isValidCard = false
                    if ((viewDataBinding.cardNumberEd.cardType.toString().contentEquals(VISA_CARD) ||
                                viewDataBinding.cardNumberEd.cardType.toString().contentEquals(MASTER_CARD))
                        && cardNumber.length == 19
                    ) {
                        isValidCard = true
                        viewDataBinding.expiryDate.requestFocus()
                    } else if ((viewDataBinding.cardNumberEd.cardType.toString().contentEquals(VERVE_CARD) ||
                                viewDataBinding.cardNumberEd.cardType.toString().contentEquals(UNKNOWN_CARD)) &&
                        (cardNumber.length == 22 || cardNumber.length == 23)
                    ) {
                        isValidCard = true
                        viewDataBinding.expiryDate.requestFocus()
                    }
                }
                if (myExpiryDate.isNotEmpty() && myExpiryDate.length == 5 &&
                    !myExpiryDate.contains("m") && !myExpiryDate.contains("y") && isValidCard
                ) {
                    viewDataBinding.cvvEd.requestFocus()
                }
                (cardNumber.isNotEmpty()) && myExpiryDate.isNotEmpty() && cvv.isNotEmpty() && cvv.length == 3 && isValidCard
            })
        isReadyObservable.subscribe({
            isReady = it
            if (it) {
                viewDataBinding.makePaymentBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_button_selector)
            } else {
                viewDataBinding.makePaymentBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.button_round_light_blue)
            }
        }, {
            Timber.e(it)
        })
    }

    companion object {
        private const val FAKE_CARD = "5300 0000 0000 0001"
        private const val VISA_CARD = "VISA"
        private const val MASTER_CARD = "MASTERCARD"
        private const val VERVE_CARD = "VERVE"
        private const val UNKNOWN_CARD = "UNKNOWN"
        const val SCREEN_TITLE = "Fund Wallet"

        fun newInstance(): FundWalletFragment {
            return FundWalletFragment()
        }
    }
}