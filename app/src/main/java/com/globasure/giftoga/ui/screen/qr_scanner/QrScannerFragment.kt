package com.globasure.giftoga.ui.screen.qr_scanner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.QrScannerFragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.*
import timber.log.Timber


@AndroidEntryPoint
class QrScannerFragment : BaseFragment<QrScannerFragBinding, QrScannerViewModel>(), QrScannerView {

    private val qrScannerViewModel: QrScannerViewModel by viewModels()

    override fun getViewModel(): QrScannerViewModel = qrScannerViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.qr_scanner_frag

    override fun getPageTitle(): String = "Debit Cards"

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

    override fun initView(view: View) {

    }
}