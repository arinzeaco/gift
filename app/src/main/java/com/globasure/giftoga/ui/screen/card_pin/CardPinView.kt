package com.globasure.giftoga.ui.screen.card_pin

import com.globasure.giftoga.ui.base.BaseView

interface CardPinView : BaseView {
    fun fundSuccess(mess: String)
    fun moveToOtp(reference: String)
    fun addCardSuccess(mess: String)
}