package com.globasure.giftoga.ui.screen.settings_tab.address

import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.network.response.StateData
import com.globasure.giftoga.ui.base.BaseView

interface AddressFragView : BaseView {
    fun updateAddressSuccess()
    fun getCountryList(list: List<CountryData>)
    fun getStateList(list: List<StateData>)
}