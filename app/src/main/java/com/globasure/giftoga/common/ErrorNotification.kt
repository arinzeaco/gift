package com.globasure.giftoga.common

interface ErrorNotification {

    enum class AfterNotify {
        NONE, SCREEN_BACK, LOGOUT, SYSTEM_ERROR
    }

    fun notifyError(
        message: String,
        throwable: Throwable?,
        option: Any?,
        workingType: AfterNotify = AfterNotify.NONE
    )

    fun onErrorAfterDialogDismiss(error: Throwable, option: Any?)

}