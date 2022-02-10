package com.globasure.giftoga.ui.dialog.state

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.DialogListStateBinding
import com.globasure.giftoga.network.response.StateData
import com.globasure.giftoga.ui.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogListState(
    private var states: ArrayList<StateData>
) : BaseBottomSheetDialogFragment<DialogListStateBinding, DialogListStateViewModel>(),
    DialogListStateView {

    private lateinit var adapter: DialogListStateAdapter

    lateinit var itemClick: (countryName: String) -> Unit

    lateinit var onCloseClick: () -> Unit

    private val dialogViewModel: DialogListStateViewModel by viewModels()

    override fun getViewModel(): DialogListStateViewModel = dialogViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getEnableDismissClickOutside(): Boolean = true

    override fun getLayoutId(): Int = R.layout.dialog_list_state

    override fun initView(view: View) {
        viewDataBinding.stateRV.layoutManager = LinearLayoutManager(context)
        adapter = DialogListStateAdapter(getBaseActivity()!!, states)
        viewDataBinding.stateRV.adapter = adapter

        adapter.onItemClick = {
            itemClick.invoke(it)
        }

        viewDataBinding.closeBtn.setOnClickListener {
            onCloseClick.invoke()
        }
    }
}