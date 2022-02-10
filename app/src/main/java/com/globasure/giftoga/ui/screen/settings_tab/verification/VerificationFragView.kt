package com.globasure.giftoga.ui.screen.settings_tab.verification

import com.globasure.giftoga.network.response.BankData
import com.globasure.giftoga.ui.base.BaseView

interface VerificationFragView : BaseView {
    fun verifySuccess()
    fun getBanksSuccess(list: List<BankData>)
}