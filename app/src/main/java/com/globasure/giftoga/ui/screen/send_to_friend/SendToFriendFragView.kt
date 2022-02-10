package com.globasure.giftoga.ui.screen.send_to_friend

import com.globasure.giftoga.network.response.ChargeFeesData
import com.globasure.giftoga.ui.base.BaseView

interface SendToFriendFragView : BaseView {
    fun chargeFeesSuccess(data: ChargeFeesData)
}