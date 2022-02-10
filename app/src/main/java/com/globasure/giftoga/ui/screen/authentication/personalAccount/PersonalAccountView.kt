package com.globasure.giftoga.ui.screen.authentication.personalAccount

import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.ui.base.BaseView

interface PersonalAccountView : BaseView {
    fun signUpSuccess()
    fun getCountryList(list: List<CountryData>)
}
