package com.globasure.giftoga.ui.screen.settings_tab.debit_card.add_card

import com.globasure.giftoga.ui.base.BaseView

interface AddCardsView : BaseView {
    fun addCardSuccess()
    fun moveToCardPin(reference: String)
}