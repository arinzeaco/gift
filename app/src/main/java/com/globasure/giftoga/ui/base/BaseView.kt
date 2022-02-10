package com.globasure.giftoga.ui.base

interface BaseView {
    fun logout()
    fun handleError(error: Throwable, option: Any? = null)
    fun screenBack()
    fun showProgressDialog(isShow: Boolean)
    fun showSnackBar(message: String)
    fun showSnackBar(resId: Int)
}