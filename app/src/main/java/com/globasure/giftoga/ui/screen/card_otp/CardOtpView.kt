package com.globasure.giftoga.ui.screen.card_otp

import com.globasure.giftoga.ui.base.BaseView

interface CardOtpView : BaseView {
    fun fundSuccess(mess: String)
    fun moveToPin(reference: String)
    fun addCardSuccess(mess: String)
}