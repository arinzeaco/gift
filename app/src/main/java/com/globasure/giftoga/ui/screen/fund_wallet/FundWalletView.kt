package com.globasure.giftoga.ui.screen.fund_wallet

import com.globasure.giftoga.ui.base.BaseView

interface FundWalletView : BaseView {
    fun fundSuccess(mess: String)
    fun fundFailed(mess: String)
    fun moveToCardPin(reference: String)
}