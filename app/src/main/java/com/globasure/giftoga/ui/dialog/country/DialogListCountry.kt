package com.globasure.giftoga.ui.dialog.country

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.DialogListCountryBinding
import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.ui.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogListCountry(
    private var countries: ArrayList<CountryData>
) : BaseBottomSheetDialogFragment<DialogListCountryBinding, DialogListCountryViewModel>(),
    DialogListCountryView {

    private lateinit var adapter: DialogListCountryAdapter

    lateinit var itemClick: (countryName: CountryData) -> Unit

    lateinit var onCloseClick: () -> Unit

    private val dialogViewModel: DialogListCountryViewModel by viewModels()

    override fun getViewModel(): DialogListCountryViewModel = dialogViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getEnableDismissClickOutside(): Boolean = true

    override fun getLayoutId(): Int = R.layout.dialog_list_country

    override fun initView(view: View) {
        viewDataBinding.countriesRV.layoutManager = LinearLayoutManager(context)
        adapter = DialogListCountryAdapter(getBaseActivity()!!, countries)
        viewDataBinding.countriesRV.adapter = adapter

        adapter.onItemClick = {
            itemClick.invoke(it)
        }

        viewDataBinding.closeBtn.setOnClickListener {
            onCloseClick.invoke()
        }
    }

}