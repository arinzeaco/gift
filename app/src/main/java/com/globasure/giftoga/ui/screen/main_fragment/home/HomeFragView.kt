package com.globasure.giftoga.ui.screen.main_fragment.home

import com.globasure.giftoga.network.response.ProfileResponse
import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.ui.base.BaseView

interface HomeFragView : BaseView {
    fun getAllTransactionSuccess(listTransaction: List<Transaction>)
    fun getUserDetailSuccess(profileResponse: ProfileResponse)
}