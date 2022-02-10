package com.globasure.giftoga.ui.screen.main_fragment.my_cards

import com.globasure.giftoga.network.response.Giftcard
import com.globasure.giftoga.ui.base.BaseView

interface MyCardsFragView : BaseView {
    fun getMyGiftCards(list: List<Giftcard>)
    fun sendMyGiftCardSuccess(message: String)
}