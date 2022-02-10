package com.globasure.giftoga.ui.screen.settings_tab.debit_card.add_card

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.ChargeType
import com.globasure.giftoga.constant.PaymentType
import com.globasure.giftoga.databinding.AddCardFragBinding
import com.globasure.giftoga.network.request.ChargeCardRequest
import com.globasure.giftoga.network.request.GetLinkForPaymentRequest
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.card_pin.CardPinFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@AndroidEntryPoint
class AddCardFragment : BaseFragment<AddCardFragBinding, AddCardsViewModel>(), AddCardsView {

    private var isValidCard: Boolean = false

    private var isReady: Boolean = false

    private val addCardsViewModel: AddCardsViewModel by viewModels()

    override fun getViewModel(): AddCardsViewModel = addCardsViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.add_card_frag

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }
        validateFields()
    }

    @SuppressLint("CheckResult")
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
                                viewDataBinding.cardNumberEd.cardType.toString().contentEquals(MASTER_CARD)) &&
                        cardNumber.length == 19
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
            }
        )
        isReadyObservable.subscribe {
            isReady = it
            if (it) {
                viewDataBinding.makePaymentBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_button_selector)
            } else {
                viewDataBinding.makePaymentBtn.background =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.button_round_light_blue)
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.make_payment_btn)
    fun saveCardClick() {
        val cardNumber = viewDataBinding.cardNumberEd.text.toString().filter { !it.isWhitespace() }
        val expiryDate = viewDataBinding.expiryDate.text.toString()
        val expiryMonth = expiryDate.substring(0, expiryDate.indexOf("/"))
        val expiryYear = expiryDate.substring(expiryDate.indexOf("/") + 1, expiryDate.length)
        val cvv = viewDataBinding.cvvEd.text.toString()

        val request = GetLinkForPaymentRequest(
            amount = "10",
            chargeType = ChargeType.VERIFICATION.type
        )
        val chargeCardRequest = ChargeCardRequest(
            payLink = null,
            saveCard = true,
            cardToken = null,
            cardNumber = cardNumber,
            expiryMonth = expiryMonth,
            expiryYear = expiryYear,
            cvv = cvv
        )

        addCardsViewModel.startAddCard(request, chargeCardRequest)
    }

    override fun addCardSuccess() {
        showSnackBar(getString(R.string.add_card_done))
    }

    override fun moveToCardPin(reference: String) {
        val fragment = CardPinFragment.newInstance(reference, null, PaymentType.ADD_CARD.type)
        fragmentNavigation.pushFragment(fragment)
    }

    companion object {
        const val SCREEN_TITLE = "Add New Card"
        private const val VISA_CARD = "VISA"
        private const val MASTER_CARD = "MASTERCARD"
        private const val VERVE_CARD = "VERVE"
        private const val UNKNOWN_CARD = "UNKNOWN"

        fun newInstance(): AddCardFragment {
            return AddCardFragment()
        }
    }
}