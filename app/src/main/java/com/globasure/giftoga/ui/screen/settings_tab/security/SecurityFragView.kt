package com.globasure.giftoga.ui.screen.settings_tab.security

import com.globasure.giftoga.ui.base.BaseView

interface SecurityFragView : BaseView {
    fun requestTokenSuccess()
    fun updatePasswordSuccess()
}