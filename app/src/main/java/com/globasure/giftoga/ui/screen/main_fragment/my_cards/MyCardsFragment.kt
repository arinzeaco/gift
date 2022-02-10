package com.globasure.giftoga.ui.screen.main_fragment.my_cards

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.BuySendType
import com.globasure.giftoga.databinding.MyCardsFragBinding
import com.globasure.giftoga.network.request.MyGiftCardRequest
import com.globasure.giftoga.network.request.SendGiftCardRequest
import com.globasure.giftoga.network.response.Giftcard
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.utils.AppUtils
import com.google.zxing.integration.android.IntentIntegrator
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.confirm_giftcard_dialog.view.change_email_btn
import kotlinx.android.synthetic.main.confirm_giftcard_dialog.view.pleaseNoteTv
import kotlinx.android.synthetic.main.confirm_giftcard_dialog.view.send_code_email
import kotlinx.android.synthetic.main.send_giftcard_dialog.view.closeBtn
import kotlinx.android.synthetic.main.send_giftcard_dialog.view.errorTv
import kotlinx.android.synthetic.main.send_giftcard_dialog.view.please_note_Tv
import kotlinx.android.synthetic.main.send_giftcard_dialog.view.recipients_email_Edt
import kotlinx.android.synthetic.main.send_giftcard_dialog.view.recipients_name_Edt
import kotlinx.android.synthetic.main.send_giftcard_dialog.view.send_btn
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@SuppressLint("NonConstantResourceId")
@AndroidEntryPoint
class MyCardsFragment : BaseFragment<MyCardsFragBinding, MyCardsFragViewModel>(), MyCardsFragView {

    private val myCardsFragViewModel: MyCardsFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.my_cards_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): MyCardsFragViewModel = myCardsFragViewModel

    private var qrScanIntegrator: IntentIntegrator? = null

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.my_cards)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    private lateinit var myGiftCardAdapter: MyGiftCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }
        qrScanIntegrator = IntentIntegrator(requireActivity())

        val merchantsRequest = MyGiftCardRequest(
            startDate = "",
            endDate = "",
            reference = "",
            type = "",
            status = "",
            order_by = "",
            page = "",
            per_page = "20"
        )
        myCardsFragViewModel.getMyCards(merchantsRequest)

        viewDataBinding.pullToRefresh.setOnRefreshListener {
            myCardsFragViewModel.getMyCards(merchantsRequest)
        }
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(true)
    }

    fun showBuyCardsScreen() {
        (getBaseActivity() as MainActivity).showBuyCardsScreen()
    }

    @OnClick(R.id.buy_card_button)
    fun onBuyCardsClick() {
        showBuyCardsScreen()
    }

    @OnClick(R.id.scanButton)
    fun onQRScanClick() {
        qrScan()
    }

    private fun qrScan() {
        val scanIntegrator = IntentIntegrator.forSupportFragment(this)
        scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        scanIntegrator.captureActivity = CaptureActivityPortrait::class.java
        scanIntegrator.setPrompt("Scan a QR Code")
        scanIntegrator.setOrientationLocked(false)
        scanIntegrator.setBarcodeImageEnabled(false)
        scanIntegrator.initiateScan()
    }

    @Suppress("ControlFlowWithEmptyBody", "DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                //do nothing
            } else {
                AppUtils().viewCodeDialog(requireContext(), result.contents, null)
            }
        } else { // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun getMyGiftCards(list: List<Giftcard>) {
        viewDataBinding.myCardRv.layoutManager = LinearLayoutManager(requireContext())

        if (list.isEmpty()) {
            viewDataBinding.myCardRv.visibility = View.GONE
            viewDataBinding.empty.visibility = View.VISIBLE
        }
        val finalLister = list as ArrayList<Giftcard>
        val giftcard = Giftcard("", "Add Gift Card", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        finalLister.add(giftcard)
        myGiftCardAdapter = MyGiftCardAdapter(context, finalLister)
        viewDataBinding.myCardRv.adapter = myGiftCardAdapter
        viewDataBinding.pullToRefresh.isRefreshing = false
    }

    override fun sendMyGiftCardSuccess(message: String) {
        showSnackBar(message)
    }

    private fun TextView.setBoldText() {
        val spannedString = text as Spannable
        val spannableString = SpannableString(text)

        spannableString.setSpan(
            StyleSpan(BOLD),
            spannedString.indexOf("friend") + 6,
            spannedString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannableString
    }

    fun sendMyGiftCards(giftcard: Giftcard) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.send_giftcard_dialog, null)
        builder.setView(customLayout)
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        if (giftcard.friendName != null) {
            customLayout.please_note_Tv.setText(
                String.format(
                    getString(R.string.send_giftcard_again),
                    giftcard.friendName
                ), TextView.BufferType.SPANNABLE
            )
            customLayout.please_note_Tv.setBoldText()
            customLayout.recipients_name_Edt.setText(giftcard.friendName)
            customLayout.recipients_name_Edt.isEnabled = false
            customLayout.recipients_email_Edt.setText(giftcard.friendEmail ?: "")
        } else {
            customLayout.recipients_name_Edt.isEnabled = true
        }

        customLayout.send_btn.setOnClickListener {
            val sendGiftCardRequest = SendGiftCardRequest(
                message = "",
                friendEmail = customLayout.recipients_email_Edt.text.toString(),
                friendName = customLayout.recipients_name_Edt.text.toString(),
                sendType = BuySendType.FRIEND.type,
                reference = giftcard.order_id
            )
            if (!validateEmail(customLayout.recipients_email_Edt)) {
                customLayout.errorTv.visibility = View.VISIBLE
            } else {
                confirmGiftcard(
                    giftcard = giftcard,
                    email = customLayout.recipients_email_Edt.text.toString(),
                    sendGiftCardRequest = sendGiftCardRequest
                )
                dialog.dismiss()
            }
        }

        customLayout.closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun confirmGiftcard(
        giftcard: Giftcard,
        email: String,
        sendGiftCardRequest: SendGiftCardRequest
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.confirm_giftcard_dialog, null)
        builder.setView(customLayout)

        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customLayout.pleaseNoteTv.text = requireContext().getString(
            R.string.confirm_email_message,
            giftcard.amount,
            email
        )
        customLayout.change_email_btn.setOnClickListener {
            dialog.dismiss()
        }
        customLayout.send_code_email.setOnClickListener {
            myCardsFragViewModel.sendToFriend(sendGiftCardRequest)

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun validateEmail(recipientsEmailEdt: EditText): Boolean {
        return recipientsEmailEdt.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback {
            }
            .check()
    }

    companion object {
        fun newInstance(): MyCardsFragment {
            return MyCardsFragment()
        }
    }
}
