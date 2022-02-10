package com.globasure.giftoga.ui.custom.xtrength.internal

import android.content.Context
import android.util.TypedValue

/**
 * Created by longnguyen on 8:00 AM, 4/17/20.
 *
 */
object ResolutionUtil {
    fun dpToPx(context: Context, dipValue: Float): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics).toInt()
    }
}