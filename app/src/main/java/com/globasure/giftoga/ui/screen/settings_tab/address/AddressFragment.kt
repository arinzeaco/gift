package com.globasure.giftoga.ui.screen.settings_tab.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.AddressFragBinding
import com.globasure.giftoga.network.request.UpdateAddressRequest
import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.network.response.StateData
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.dialog.country.DialogListCountry
import com.globasure.giftoga.ui.dialog.state.DialogListState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddressFragment : BaseFragment<AddressFragBinding, AddressViewModel>(), AddressFragView {

    private val addressViewModel: AddressViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.address_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): AddressViewModel = addressViewModel

    private lateinit var countryDialog: DialogListCountry

    private lateinit var stateDialog: DialogListState

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.address)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        viewDataBinding.selectCountryTv.text = hawkHelper.getUserDetail()?.country
        viewDataBinding.selectStateTv.text = hawkHelper.getUserDetail()?.state
        viewDataBinding.addressEdt.setText(hawkHelper.getUserDetail()?.address)
        viewDataBinding.cityEdt.setText(hawkHelper.getUserDetail()?.city)

        viewDataBinding.saveInformationBtn.setOnClickListener {
            val updateAddressRequest = UpdateAddressRequest(
                state = viewDataBinding.selectStateTv.text.toString(),
                city = viewDataBinding.cityEdt.text.toString(),
                address = viewDataBinding.addressEdt.text.toString(),
                postalCode = null
            )
            addressViewModel.updateAddress(updateAddressRequest)
        }

        addressViewModel.getCountryList()
        hawkHelper.getUserDetail()?.country?.let {
            addressViewModel.getStateByCountry(it)
        }
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun updateAddressSuccess() {
        showSnackBar(R.string.update_address_success)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500)
            (getBaseActivity() as MainActivity).onBackPressed()
        }
    }

    override fun getCountryList(list: List<CountryData>) {
        countryDialog = DialogListCountry(list as ArrayList<CountryData>)
        viewDataBinding.selectCountryTv.setOnClickListener {
            handleChooseCountryCLick()
        }
    }

    private fun handleChooseCountryCLick() {
        countryDialog.itemClick = {
            viewDataBinding.selectCountryTv.text = it.name
            countryDialog.dismiss()
            addressViewModel.getStateByCountry(it.name)
        }

        countryDialog.onCloseClick = {
            countryDialog.dismiss()
        }

        countryDialog.show(this.childFragmentManager, TAG)
    }

    override fun getStateList(list: List<StateData>) {
        stateDialog = DialogListState(list as ArrayList<StateData>)
        viewDataBinding.selectStateTv.setOnClickListener {
            handleChooseStateCLick()
        }
    }

    private fun handleChooseStateCLick() {
        stateDialog.itemClick = {
            viewDataBinding.selectStateTv.text = it
            stateDialog.dismiss()
        }

        stateDialog.onCloseClick = {
            stateDialog.dismiss()
        }

        stateDialog.show(this.childFragmentManager, TAG)
    }

    companion object {
        const val TAG = "AddressFragment"

        fun newInstance(): AddressFragment {
            return AddressFragment()
        }
    }
}
