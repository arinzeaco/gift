package com.globasure.giftoga.ui.custom

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.globasure.giftoga.R

class CustomOutline() : ViewOutlineProvider() {
    private var defaultAlpha = 0.4f

    constructor(alpha: Float) : this() {
        defaultAlpha = alpha
    }

    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
            0,
            0,
            view.width,
            view.height,
            view.context.resources.getDimensionPixelSize(R.dimen.radius_5).toFloat()
        )
        outline.alpha = defaultAlpha
    }
}