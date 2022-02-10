package com.globasure.giftoga.ui.screen.main_fragment.buy_cards

import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.network.response.Merchant
import com.globasure.giftoga.network.response.MetaData
import com.globasure.giftoga.ui.base.BaseView

interface BuyCardsFragView : BaseView {
    fun getCountryList(list: List<CountryData>)
    fun getMerchantsGiftCard(list: List<Merchant>, metaData: MetaData?)
}