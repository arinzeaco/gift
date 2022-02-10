package com.globasure.giftoga.ui.screen.transactions

import com.globasure.giftoga.network.response.MetaData
import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.ui.base.BaseView

interface TransactionsFragView : BaseView {
    fun getAllTransactionSuccess(listTransaction: List<Transaction>, metaData: MetaData?)
}