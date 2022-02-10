package com.globasure.giftoga.ui.screen.main_fragment.account

import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.ui.base.BaseView

interface AccountFragView : BaseView {
   fun getAllTransactionSuccess(listTransaction: List<Transaction>)
}