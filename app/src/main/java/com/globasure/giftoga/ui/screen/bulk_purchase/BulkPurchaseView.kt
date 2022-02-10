package com.globasure.giftoga.ui.screen.bulk_purchase

import com.globasure.giftoga.ui.base.BaseView

interface BulkPurchaseView : BaseView {
    fun uploadFile(mess: String)
    fun validateBulkFileSuccess()
}