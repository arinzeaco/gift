package com.globasure.giftoga.ui.screen.settings_tab.debit_card

import com.globasure.giftoga.network.response.CardData
import com.globasure.giftoga.network.response.MetaData
import com.globasure.giftoga.ui.base.BaseView

interface DebitCardsView : BaseView {
    fun loadCardsSuccess(list: List<CardData>, metaData: MetaData?)
    fun deleteCardSuccess()
}