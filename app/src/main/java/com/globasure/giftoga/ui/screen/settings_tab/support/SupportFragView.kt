package com.globasure.giftoga.ui.screen.settings_tab.support

import com.globasure.giftoga.network.response.OpenCaseResponse
import com.globasure.giftoga.ui.base.BaseView

interface SupportFragView : BaseView {
    fun submitCaseSuccess(response: OpenCaseResponse)
}