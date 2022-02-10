package com.globasure.giftoga.ui.dialog.banks

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.DialogListBankBinding
import com.globasure.giftoga.network.response.BankData
import com.globasure.giftoga.ui.base.BaseBottomSheetDialogFragment

class DialogListBank(
    private var banks: ArrayList<BankData>
) : BaseBottomSheetDialogFragment<DialogListBankBinding, DialogListBankViewModel>(),
    DialogListBankView {

    private lateinit var adapter: DialogListBankAdapter

    lateinit var itemClick: (bank: BankData) -> Unit

    lateinit var onCloseClick: () -> Unit

    private val dialogViewModel: DialogListBankViewModel by viewModels()

    override fun getViewModel(): DialogListBankViewModel = dialogViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getEnableDismissClickOutside(): Boolean = true

    override fun getLayoutId(): Int = R.layout.dialog_list_bank

    override fun initView(view: View) {
        viewDataBinding.banksRV.layoutManager = LinearLayoutManager(context)
        adapter = DialogListBankAdapter(getBaseActivity()!!, banks)
        viewDataBinding.banksRV.adapter = adapter

        adapter.onItemClick = {
            itemClick.invoke(it)
        }

        viewDataBinding.closeBtn.setOnClickListener {
            onCloseClick.invoke()
        }
    }
}