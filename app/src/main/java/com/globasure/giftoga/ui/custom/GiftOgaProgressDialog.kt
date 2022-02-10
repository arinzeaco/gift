package com.globasure.giftoga.ui.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.globasure.giftoga.R

class GiftOgaProgressDialog(context: Context) : Dialog(context, R.style.transparent_progress_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading_progress)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }
}