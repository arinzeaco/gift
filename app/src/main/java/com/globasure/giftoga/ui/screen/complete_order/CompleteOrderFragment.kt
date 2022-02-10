package com.globasure.giftoga.ui.screen.complete_order

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.globasure.giftoga.constant.Constant
import com.globasure.giftoga.constant.Constant.Companion.DEFAULT_CHANNEL
import com.globasure.giftoga.constant.MethodType
import com.globasure.giftoga.constant.PaymentType
import com.globasure.giftoga.databinding.CompleteOrderFragBinding
import com.globasure.giftoga.network.request.BuyGiftcardRequest
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.ChargeGiftCardRequest
import com.globasure.giftoga.network.request.ChargeWalletRequest
import com.globasure.giftoga.network.request.GetLinkForSalesRequest
import com.globasure.giftoga.network.request.SendToFriendRequest
import com.globasure.giftoga.network.response.CardData
import com.globasure.giftoga.network.response.ChargeBankTransferData
import com.globasure.giftoga.network.response.ChargeFeesData
import com.globasure.giftoga.network.response.ChargePayPalData
import com.globasure.giftoga.network.response.Merchant
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.card_pin.CardPinFragment
import com.globasure.giftoga.ui.screen.settings_tab.debit_card.DebitCardsFragment
import com.globasure.giftoga.utils.AppUtils
import com.globasure.giftoga.utils.extension.formatCurrency
import com.globasure.giftoga.utils.extension.formatDecimal
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.invisible
import com.globasure.giftoga.utils.extension.runIfNotNull
import com.globasure.giftoga.utils.extension.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.jakewharton.rxbinding2.widget.RxTextView
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.ShippingPreference
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.android.synthetic.main.dialog_card_options.view.add_card
import kotlinx.android.synthetic.main.dialog_card_options.view.existing_card
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber


@AndroidEntryPoint
class CompleteOrderFragment : BaseFragment<CompleteOrderFragBinding, CompleteOrderViewModel>(),
    CompleteOrderFragView {

    private val completeOrderViewModel: CompleteOrderViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.complete_order_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CompleteOrderViewModel = completeOrderViewModel

    private var paymentMethod = Constant.PAY_WITH_WALLET

    private var isValidCard: Boolean = false

    private var isReady: Boolean = false

    private var finalAmount = 0

    private var giftcardQuantity: String? = null

    lateinit var merchant: Merchant

    lateinit var sendToFriendRequest: SendToFriendRequest

    private var total: Double = 0.0

    private var selectedCardToken: String? = null

    private var allowSendSms: Boolean = false

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        setFragmentResultListener("SELECTED_CARD") { _, bundle ->
            viewDataBinding.payWithCardIv.setOnChangeListener()
            viewDataBinding.payWithWalletIv.setOnChangeListener()
            viewDataBinding.payWithGiftogacardIv.setOnChangeListener()
            viewDataBinding.payWithBanktransferIv.setOnChangeListener()
            //viewDataBinding.payWithPaystackIv.setOnChangeListener()
            viewDataBinding.payWithPaypalIv.setOnChangeListener()
            val selectedCard = bundle.getParcelable<CardData>("selected-card")!!
            selectedCardToken = selectedCard.token
            viewDataBinding.payWithCardIv.setChecked(checked = true, animate = true)
            viewDataBinding.payWithWalletIv.setChecked(false)
            viewDataBinding.payWithGiftogacardIv.setChecked(false)
            viewDataBinding.payWithBanktransferIv.setChecked(false)
            //viewDataBinding.payWithPaystackIv.setChecked(false)
            viewDataBinding.payWithPaypalIv.setChecked(false)
            viewDataBinding.selectMethodTv.text = getString(R.string.choose_from_existing_card)
            viewDataBinding.divider3.visible()
            viewDataBinding.selectedCardLayout.visible()
            viewDataBinding.cardPaymentOptions.visible()
            viewDataBinding.cardPaymentLayout.visible()
            viewDataBinding.cardNumberTv.gone()
            viewDataBinding.cardNumberEd.gone()
            viewDataBinding.giftogaPaymentLayout.gone()
            viewDataBinding.walletPaymentLayout.gone()
            viewDataBinding.banktransferPaymentLayout.gone()
            viewDataBinding.paypalPaymentLayout.gone()
            paymentMethod = Constant.PAY_WITH_CARD

            viewDataBinding.cardBrand.text = selectedCard.cardBrand
            viewDataBinding.cardNumber.text = selectedCard.cardNumber

            initOnChangeListener()
        }

        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        val gson = Gson()
        merchant = gson.fromJson(arguments?.getString(MERCHANT), Merchant::class.java)
        finalAmount = arguments?.getString(AMOUNT)!!.toInt() * arguments?.getString(QUANTITY)!!.toInt()
        giftcardQuantity = arguments?.getString(QUANTITY)
        completeOrderViewModel.getFees(
            currency_name = merchant.currencyName,
            charge_type = ChargeType.SALES.type,
            method = MethodType.BALANCE.type,
            amount = arguments?.getString(AMOUNT)!!,
            merchantId = merchant.id,
            delivery_to_sms = null,
            delivery_country = null
        )

        viewDataBinding.giftCardTv.text = merchant.cardName
        viewDataBinding.priceTv.text = merchant.currencyName + " " + finalAmount.toString()
        viewDataBinding.deliveryLabel.text = String.format(
            getString(R.string.transaction_sms_deliver), hawkHelper.getUserDetail()?.mobile
        )
        viewDataBinding.quantityTv.text = arguments?.getString(QUANTITY)
        viewDataBinding.walletBalance.text =
            hawkHelper.getUserDetail()?.balance?.walletBalance?.formatDecimal()?.formatCurrency(
                isPrefix = true,
                currencyName = hawkHelper.getUserDetail()?.balance?.currencyName
            )
        viewDataBinding.payWithWalletIv.setChecked(true)
        viewDataBinding.payWithCardIv.setChecked(false)

        initOnChangeListener()

        sendToFriendRequest =
            Gson().fromJson(arguments?.getString(SEND_TO_FRIEND_REQUEST), SendToFriendRequest::class.java)
        viewDataBinding.makePaymentBtn.setOnClickListener {
            giftcardQuantity.runIfNotNull { onMakePaymentClick(it) }
        }
    }

    private fun initOnChangeListener() {
        viewDataBinding.deliverSms.setOnChangeListener { isChecked ->
            allowSendSms = isChecked

            completeOrderViewModel.getFees(
                currency_name = merchant.currencyName,
                charge_type = ChargeType.SALES.type,
                method = MethodType.BALANCE.type,
                amount = arguments?.getString(AMOUNT)!!,
                merchantId = merchant.id,
                delivery_to_sms = if (isChecked) "1" else "0",
                delivery_country = if (isChecked) hawkHelper.getUserDetail()?.country else null
            )
        }

        viewDataBinding.payWithWalletIv.setOnChangeListener { isChecked ->
            handlePayWithWalletCheck(isChecked)
        }

        viewDataBinding.payWithCardIv.setOnChangeListener { isChecked ->
            handlePayWithCardCheck(isChecked)
        }

        viewDataBinding.payWithGiftogacardIv.setOnChangeListener { isChecked ->
            handlePayWithGiftCardCheck(isChecked)
        }

        viewDataBinding.payWithBanktransferIv.setOnChangeListener { isChecked ->
            handlePayWithBankTransferCheck(isChecked)
        }

//        viewDataBinding.payWithPaystackIv.setOnChangeListener { isChecked ->
//            handlePayWithPayStackCheck(isChecked)
//        }

        viewDataBinding.payWithPaypalIv.setOnChangeListener { isChecked ->
            handlePayWithPaypalCheck(isChecked)
        }
    }

    private fun onMakePaymentClick(quantity: String) {
        //var deliver = sendToFriendRequest.buyType.contentEquals(BuySendType.FRIEND.type)

        when {
            paymentMethod.contentEquals(Constant.PAY_WITH_CARD) -> {
                //We need to check if selectedCardToken != null -> user choose existing card, this request will be different
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

                val getLinkForSalesRequest = GetLinkForSalesRequest(
                    buyType = BuySendType.SELF.type,
                    amount = finalAmount.toString(),
                    quantity = quantity,
                    channel = DEFAULT_CHANNEL,
                    chargeType = ChargeType.SALES.type,
                    method = MethodType.CARD.type,
                    merchant = merchant.id,
                    hasBulkFile = false
                )

                completeOrderViewModel.getSalesPayLink(
                    getLinkForSalesRequest,
                    chargeCardRequest,
                    sendToFriendRequest
                )
            }

            paymentMethod.contentEquals(Constant.PAY_WITH_GIFTCARD) -> {
                val chargeGiftCardRequest = ChargeGiftCardRequest(
                    giftcard = viewDataBinding.giftCardNumberEd.text.toString(),
                    amount = finalAmount.toString(),
                    quantity = quantity,
                    merchant = merchant.id,
                    hasBulkfile = "no",
                    chargeType = ChargeType.SALES.type,
                    channel = DEFAULT_CHANNEL
                )
                completeOrderViewModel.chargeGiftCard(
                    chargeGiftCardRequest,
                    sendToFriendRequest
                )
            }

            paymentMethod.contentEquals(Constant.PAY_WITH_BANK_TRANSFER) -> {
                completeOrderViewModel.confirmBankTransfer(sendToFriendRequest)
            }

            paymentMethod.contentEquals(Constant.PAY_WITH_PAY_STACK) -> {
                completeOrderViewModel.confirmPayStack(sendToFriendRequest)
            }

            paymentMethod.contentEquals(Constant.PAY_WITH_PAY_PAL) -> {

            }

            else -> {
                val chargeWalletRequest = ChargeWalletRequest(payLink = "")
                val getLinkForSalesRequest = GetLinkForSalesRequest(
                    buyType = BuySendType.SELF.type,
                    amount = finalAmount.toString(),
                    quantity = quantity,
                    channel = DEFAULT_CHANNEL,
                    chargeType = ChargeType.SALES.type,
                    method = MethodType.BALANCE.type,
                    merchant = merchant.id,
                    hasBulkFile = false
                )
                completeOrderViewModel.chargeWallet(
                    getLinkForSalesRequest = getLinkForSalesRequest,
                    chargeWalletRequest = chargeWalletRequest,
                    sendToFriendRequest = sendToFriendRequest
                )
            }
        }
    }

    private fun handlePayWithWalletCheck(isChecked: Boolean) {
        if (isChecked) {
            viewDataBinding.payWithWalletIv.setChecked(true)
            viewDataBinding.payWithCardIv.setChecked(false)
            viewDataBinding.payWithGiftogacardIv.setChecked(false)
            viewDataBinding.payWithBanktransferIv.setChecked(false)
            //viewDataBinding.payWithPaystackIv.setChecked(false)
            viewDataBinding.payWithPaypalIv.setChecked(false)

            viewDataBinding.divider3.visible()

            viewDataBinding.selectedCardLayout.gone()
            viewDataBinding.cardPaymentOptions.gone()
            viewDataBinding.walletPaymentLayout.visible()
            viewDataBinding.cardPaymentLayout.gone()
            viewDataBinding.giftogaPaymentLayout.gone()
            viewDataBinding.banktransferPaymentLayout.gone()
            viewDataBinding.paypalPaymentLayout.gone()

            viewDataBinding.paypalButton.gone()
            viewDataBinding.makePaymentBtn.visible()

            paymentMethod = Constant.PAY_WITH_WALLET
            viewDataBinding.makePaymentBtn.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_button_selector)
        }
    }

    private fun handlePayWithCardCheck(isChecked: Boolean) {
        if (isChecked) {
            viewDataBinding.payWithCardIv.setChecked(true)
            viewDataBinding.payWithWalletIv.setChecked(false)
            viewDataBinding.payWithGiftogacardIv.setChecked(false)
            viewDataBinding.payWithBanktransferIv.setChecked(false)
            //viewDataBinding.payWithPaystackIv.setChecked(false)
            viewDataBinding.payWithPaypalIv.setChecked(false)

            viewDataBinding.divider3.visible()

            viewDataBinding.cardPaymentOptions.visible()
            viewDataBinding.cardPaymentLayout.gone()
            viewDataBinding.giftogaPaymentLayout.gone()
            viewDataBinding.walletPaymentLayout.gone()
            viewDataBinding.banktransferPaymentLayout.gone()
            viewDataBinding.paypalPaymentLayout.gone()
            paymentMethod = Constant.PAY_WITH_CARD

            //viewDataBinding.cardNumberEd.setText("4084084084084081")
            //viewDataBinding.expiryDate.setText("0322")
            //viewDataBinding.cvvEd.setText("408")
        }
    }

    private fun handlePayWithGiftCardCheck(isChecked: Boolean) {
        if (isChecked) {
            viewDataBinding.payWithGiftogacardIv.setChecked(true)
            viewDataBinding.payWithWalletIv.setChecked(false)
            viewDataBinding.payWithCardIv.setChecked(false)
            viewDataBinding.payWithBanktransferIv.setChecked(false)
            //viewDataBinding.payWithPaystackIv.setChecked(false)
            viewDataBinding.payWithPaypalIv.setChecked(false)

            viewDataBinding.divider3.gone()
            completeOrderViewModel.getFees(
                currency_name = merchant.currencyName,
                charge_type = ChargeType.SALES.type,
                method = MethodType.BALANCE.type,
                amount = arguments?.getString(AMOUNT)!!,
                merchantId = merchant.id,
                delivery_to_sms = null,
                delivery_country = null
            )

            viewDataBinding.paypalButton.gone()
            viewDataBinding.makePaymentBtn.visible()

            viewDataBinding.makePaymentBtn.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.button_round_light_blue)

            viewDataBinding.giftCardNumberEd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.length > 15) {
                        viewDataBinding.makePaymentBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_button_selector)
                    } else {
                        viewDataBinding.makePaymentBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_round_light_blue)

                    }
                }
            })
            viewDataBinding.selectedCardLayout.gone()
            viewDataBinding.cardPaymentOptions.gone()
            viewDataBinding.walletPaymentLayout.gone()
            viewDataBinding.cardPaymentLayout.gone()
            viewDataBinding.giftogaPaymentLayout.visible()
            viewDataBinding.banktransferPaymentLayout.gone()
            viewDataBinding.paypalPaymentLayout.gone()
            paymentMethod = Constant.PAY_WITH_GIFTCARD

        }
    }

    private fun handlePayWithBankTransferCheck(isChecked: Boolean) {
        if (isChecked) {
            viewDataBinding.payWithWalletIv.setChecked(false)
            viewDataBinding.payWithCardIv.setChecked(false)
            viewDataBinding.payWithGiftogacardIv.setChecked(false)
            viewDataBinding.payWithBanktransferIv.setChecked(true)
            //viewDataBinding.payWithPaystackIv.setChecked(false)
            viewDataBinding.payWithPaypalIv.setChecked(false)

            viewDataBinding.divider3.visible()

            viewDataBinding.selectedCardLayout.gone()
            viewDataBinding.cardPaymentOptions.gone()
            viewDataBinding.walletPaymentLayout.gone()
            viewDataBinding.cardPaymentLayout.gone()
            viewDataBinding.giftogaPaymentLayout.gone()
            viewDataBinding.banktransferPaymentLayout.visible()
            viewDataBinding.paypalPaymentLayout.gone()

            viewDataBinding.paypalButton.gone()
            viewDataBinding.makePaymentBtn.visible()

            paymentMethod = Constant.PAY_WITH_BANK_TRANSFER
            viewDataBinding.makePaymentBtn.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_button_selector)

            val getLinkForSalesRequest = GetLinkForSalesRequest(
                buyType = BuySendType.SELF.type,
                amount = finalAmount.toString(),
                quantity = giftcardQuantity!!,
                channel = DEFAULT_CHANNEL,
                chargeType = ChargeType.SALES.type,
                method = MethodType.BANK_TRANSFER.type,
                merchant = merchant.id,
                hasBulkFile = false
            )

            completeOrderViewModel.getLinkForBankTransfer(getLinkForSalesRequest)
        }
    }

    private fun handlePayWithPayStackCheck(isChecked: Boolean) {
        if (isChecked) {
            viewDataBinding.payWithWalletIv.setChecked(false)
            viewDataBinding.payWithCardIv.setChecked(false)
            viewDataBinding.payWithGiftogacardIv.setChecked(false)
            viewDataBinding.payWithBanktransferIv.setChecked(false)
            //viewDataBinding.payWithPaystackIv.setChecked(true)
            viewDataBinding.payWithPaypalIv.setChecked(false)

            viewDataBinding.divider3.visible()

            viewDataBinding.selectedCardLayout.gone()
            viewDataBinding.cardPaymentOptions.gone()
            viewDataBinding.walletPaymentLayout.gone()
            viewDataBinding.cardPaymentLayout.gone()
            viewDataBinding.giftogaPaymentLayout.gone()
            viewDataBinding.banktransferPaymentLayout.gone()
            viewDataBinding.paypalPaymentLayout.gone()

            viewDataBinding.paypalButton.gone()
            viewDataBinding.makePaymentBtn.visible()

            paymentMethod = Constant.PAY_WITH_PAY_STACK
            viewDataBinding.makePaymentBtn.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_button_selector)

            val getLinkForSalesRequest = GetLinkForSalesRequest(
                buyType = BuySendType.SELF.type,
                amount = finalAmount.toString(),
                quantity = giftcardQuantity!!,
                channel = DEFAULT_CHANNEL,
                chargeType = ChargeType.SALES.type,
                method = MethodType.PAY_STACK.type,
                merchant = merchant.id,
                hasBulkFile = false
            )

            completeOrderViewModel.getLinkForPayStack(getLinkForSalesRequest)
        }
    }

    private fun handlePayWithPaypalCheck(isChecked: Boolean) {
        if (isChecked) {
            viewDataBinding.payWithWalletIv.setChecked(false)
            viewDataBinding.payWithCardIv.setChecked(false)
            viewDataBinding.payWithGiftogacardIv.setChecked(false)
            viewDataBinding.payWithBanktransferIv.setChecked(false)
            //viewDataBinding.payWithPaystackIv.setChecked(false)
            viewDataBinding.payWithPaypalIv.setChecked(true)

            viewDataBinding.divider3.visible()

            viewDataBinding.selectedCardLayout.gone()
            viewDataBinding.cardPaymentOptions.gone()
            viewDataBinding.walletPaymentLayout.gone()
            viewDataBinding.cardPaymentLayout.gone()
            viewDataBinding.giftogaPaymentLayout.gone()
            viewDataBinding.banktransferPaymentLayout.gone()
            viewDataBinding.paypalPaymentLayout.visible()

            paymentMethod = Constant.PAY_WITH_PAY_PAL
            viewDataBinding.makePaymentBtn.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_button_selector)

            val getLinkForSalesRequest = GetLinkForSalesRequest(
                buyType = BuySendType.SELF.type,
                amount = finalAmount.toString(),
                quantity = giftcardQuantity!!,
                channel = DEFAULT_CHANNEL,
                chargeType = ChargeType.SALES.type,
                method = MethodType.PAYPAL.type,
                merchant = merchant.id,
                hasBulkFile = false
            )

            completeOrderViewModel.getLinkForPaypal(getLinkForSalesRequest)
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.card_payment_options)
    fun onChooseCardOptionClick() {
        openCardOptions(requireContext(), {
            chooseAddCard()
        }, {
            chooseExistingCard()
        })
    }

    private fun chooseAddCard() {
        selectedCardToken = null
        viewDataBinding.selectMethodTv.text = getString(R.string.add_card)

        completeOrderViewModel.getFees(
            currency_name = merchant.currencyName,
            charge_type = ChargeType.SALES.type,
            method = MethodType.BALANCE.type,
            amount = arguments?.getString(AMOUNT)!!,
            merchantId = merchant.id,
            delivery_to_sms = if (allowSendSms) "1" else "0",
            delivery_country = hawkHelper.getUserDetail()?.country
        )

        viewDataBinding.selectedCardLayout.gone()
        viewDataBinding.cardPaymentOptions.visible()
        viewDataBinding.cardPaymentLayout.visible()
        viewDataBinding.giftogaPaymentLayout.gone()
        viewDataBinding.walletPaymentLayout.gone()
        viewDataBinding.banktransferPaymentLayout.gone()
        viewDataBinding.paypalPaymentLayout.gone()
        viewDataBinding.cardNumberTv.visible()
        viewDataBinding.cardNumberEd.visible()
        paymentMethod = Constant.PAY_WITH_CARD
        validateFields()
    }

    private fun chooseExistingCard() {
        viewDataBinding.selectMethodTv.text = getString(R.string.choose_from_existing_card)

        viewDataBinding.cardPaymentLayout.visible()
        viewDataBinding.cardNumberTv.gone()
        viewDataBinding.cardNumberEd.gone()
        navigateExistingCards()
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

    private fun navigateExistingCards() {
        val fragment = DebitCardsFragment.newInstance(getString(R.string.select_a_card_title), true)
        fragmentNavigation.pushFragment(fragment)
    }

    override fun getPageTitle(): String = getString(R.string.complete_order)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    @SuppressLint("SetTextI18n")
    override fun chargeFeesSuccess(data: ChargeFeesData) {
        try {
            total = data.charge.toDouble()
            if (data.fromCurrencyName == data.toCurrencyName) {
                showExchangeRate(false, data)
            } else {
                showExchangeRate(true, data)
            }

            showSMSFee(allowSendSms, data)
            viewDataBinding.transactionFeeTv.text = data.toCurrencyName + " " + data.fees
            viewDataBinding.totalTv.text = data.toCurrencyName + " " + total.toString()
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showExchangeRate(isShow: Boolean, data: ChargeFeesData) {
        if (isShow) {
            viewDataBinding.dividerExchange.visible()
            viewDataBinding.transactionExchangeLabel.visible()
            viewDataBinding.transactionExchangeTv.visible()

            viewDataBinding.transactionExchangeTv.text = data.toCurrencyName + " " + data.charge
        } else {
            viewDataBinding.dividerExchange.gone()
            viewDataBinding.transactionExchangeLabel.gone()
            viewDataBinding.transactionExchangeTv.gone()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSMSFee(isShow: Boolean, data: ChargeFeesData) {
        if (isShow) {
            viewDataBinding.dividerSms.visible()
            viewDataBinding.transactionSmsLabel.visible()
            viewDataBinding.transactionSmsTv.visible()

            viewDataBinding.transactionSmsTv.text = data.toCurrencyName + " " + data.smsFees
        } else {
            viewDataBinding.dividerSms.gone()
            viewDataBinding.transactionSmsLabel.gone()
            viewDataBinding.transactionSmsTv.gone()
        }
    }

    override fun fundSuccess(mess: String) {
        AppUtils().successDialog(requireContext(), mess, ::moveToHome)
    }

    private fun moveToHome() {
        (getBaseActivity() as MainActivity).fragNavController.clearStack()
        (getBaseActivity() as MainActivity).switchBackToHomePage()
    }

    override fun moveToCardPin(reference: String) {
        val buyGiftcardRequest = BuyGiftcardRequest(
            payment_token = reference,
            buyType = BuySendType.SELF.type,
            friendName = "",
            friendEmail = "",
            friendPhone = "",
            message = ""
        )
        val fragment = CardPinFragment.newInstance(
            reference = reference,
            buyGiftcardRequest = buyGiftcardRequest,
            transactionType = PaymentType.COMPLETE_ORDER.type
        )
        fragmentNavigation.pushFragment(fragment)
    }

    override fun chargeBankTransferSuccess(data: ChargeBankTransferData) {
        viewDataBinding.banktransferDescription.text = String.format(
            getString(R.string.method_bank_transfer_description),
            "${data.currency}${data.amount}",
            data.accountNumber,
            data.accountName
        )
    }

    override fun confirmBankTransferSuccess(mess: String) {
        AppUtils().successDialog(requireContext(), mess, ::moveToHome)
    }

    override fun chargePayPalSuccess(data: ChargePayPalData) {
        AppUtils().successDialog(requireContext(), data.description.orEmpty(), ::moveToHome)
    }

    override fun confirmPayPalSuccess(mess: String) {
        AppUtils().successDialog(requireContext(), mess, ::moveToHome)
    }

    override fun setupPaypal(clientId: String, returnURL: String, paypalAmount: String) {
        viewDataBinding.paypalButton.visible()
        viewDataBinding.makePaymentBtn.invisible()

        val config = CheckoutConfig(
            application = this.requireActivity().application,
            clientId = clientId,
            environment = Environment.LIVE,
            returnUrl = returnURL,
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = false
            )
        )
        PayPalCheckout.setConfig(config)

        setupPaypalButton(paypalAmount)
    }

    private fun setupPaypalButton(paypalAmount: String) {
        viewDataBinding.paypalButton.setup(
            createOrder = CreateOrder { createOrderActions ->
                createOrderActions.create(
                    Order.Builder()
                        .appContext(
                            AppContext(
                                userAction = UserAction.PAY_NOW,
                                shippingPreference = ShippingPreference.NO_SHIPPING
                            )
                        )
                        .intent(OrderIntent.CAPTURE)
                        .purchaseUnitList(
                            listOf(
                                PurchaseUnit.Builder()
                                    .amount(Amount.Builder().value(paypalAmount).currencyCode(CurrencyCode.USD).build())
                                    .build()
                            )
                        )
                        .build()
                        .also { Timber.tag(TAG).d("Order: $it") }
                )
            },
            onApprove = OnApprove { approval ->
                //Timber.tag(TAG).d("Approval details: $approval")
                completeOrderViewModel.paypalOrderId = approval.data.orderId
                completeOrderViewModel.confirmPayStack(sendToFriendRequest)
                //approval.orderActions.capture { captureOrderResult ->
                //Timber.tag(TAG).d("Capture order result: $captureOrderResult")
                //}
            },
            onCancel = OnCancel {
                Timber.tag(TAG).d("Buyer cancelled the checkout experience.")
            },
            onError = OnError { errorInfo ->
                Timber.tag(TAG).d("Error details: $errorInfo")
            }
        )
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    private fun validateFields() {
        val cardNumberObservable = RxTextView.textChanges(viewDataBinding.cardNumberEd)
        val expiryDateObservable = RxTextView.textChanges(viewDataBinding.expiryDate)
        val cvvObservable = RxTextView.textChanges(viewDataBinding.cvvEd)
        val isReadyObservable: Observable<Boolean> = Observable.combineLatest(
            cardNumberObservable,
            expiryDateObservable,
            cvvObservable,
            { cardNumber, myExpiryDate, cvv ->
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
        isReadyObservable.subscribe {
            isReady = it
            if (it) {
                viewDataBinding.makePaymentBtn.background = AppCompatResources.getDrawable(
                    requireContext(), R.drawable.rounded_button_selector
                )
            } else {
                viewDataBinding.makePaymentBtn.background = AppCompatResources.getDrawable(
                    requireContext(), R.drawable.button_round_light_blue
                )
            }
        }
    }

    companion object {
        private const val TAG = "CompleteOrderFragment"
        private const val MERCHANT = "merchant"
        private const val AMOUNT = "amount"
        private const val QUANTITY = "quantity"
        private const val SEND_TO_FRIEND_REQUEST = "send_to_friend_request"
        private const val VISA_CARD = "VISA"
        private const val MASTER_CARD = "MASTERCARD"
        private const val VERVE_CARD = "VERVE"
        private const val UNKNOWN_CARD = "UNKNOWN"

        fun newInstance(
            merchant: Merchant,
            amount: String,
            quantity: String,
            sendToFriendRequest: SendToFriendRequest
        ): CompleteOrderFragment {
            val fragment = CompleteOrderFragment()
            val gson = Gson()
            val args = Bundle()
            args.putString(MERCHANT, gson.toJson(merchant))
            args.putString(AMOUNT, amount)
            args.putString(QUANTITY, quantity)
            args.putString(SEND_TO_FRIEND_REQUEST, gson.toJson(sendToFriendRequest))
            fragment.arguments = args
            return fragment
        }
    }

}
