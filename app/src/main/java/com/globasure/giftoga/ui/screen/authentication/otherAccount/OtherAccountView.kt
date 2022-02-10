package com.globasure.giftoga.ui.screen.authentication.otherAccount

import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.network.response.StateData
import com.globasure.giftoga.ui.base.BaseView

interface OtherAccountView : BaseView {
    fun signUpSuccess()
    fun getCountryList(list: List<CountryData>)
    fun getStateList(list: List<StateData>)
}