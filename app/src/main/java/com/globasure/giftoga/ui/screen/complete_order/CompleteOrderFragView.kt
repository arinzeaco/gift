package com.globasure.giftoga.ui.screen.complete_order

import com.globasure.giftoga.network.response.ChargeBankTransferData
import com.globasure.giftoga.network.response.ChargeFeesData
import com.globasure.giftoga.network.response.ChargePayPalData
import com.globasure.giftoga.ui.base.BaseView

interface CompleteOrderFragView : BaseView {
    fun chargeFeesSuccess(data: ChargeFeesData)
    fun fundSuccess(mess: String)
    fun moveToCardPin(reference: String)
    fun chargeBankTransferSuccess(data: ChargeBankTransferData)
    fun confirmBankTransferSuccess(mess: String)
    fun chargePayPalSuccess(data: ChargePayPalData)
    fun confirmPayPalSuccess(mess: String)
    fun setupPaypal(clientId: String, returnURL: String, paypalAmount: String)
}