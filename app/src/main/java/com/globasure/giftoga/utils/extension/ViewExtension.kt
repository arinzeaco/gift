package com.globasure.giftoga.utils.extension

import android.view.View
import android.view.animation.AnimationUtils
import com.globasure.giftoga.R


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visibleIf(predicate: Boolean, invisible: Boolean = false) {
    when (predicate) {
        true -> this.visible()
        false -> if (!invisible) this.gone() else this.invisible()
    }
}

fun View.setSemiTransparentIf(shouldBeTransparent: Boolean, disabledAlpha: Float = 0.3f) {
    alpha = when (shouldBeTransparent) {
        true -> disabledAlpha
        false -> 1f
    }
}

fun View.fadeIn(predicate: Boolean) {
    when (predicate) {
        true -> {
            if (this.visibility == View.INVISIBLE) {
                val fadeInAnimation = AnimationUtils.loadAnimation(this.context, R.anim.fade_in)
                this.startAnimation(fadeInAnimation)
            }
        }

        false -> {
            this.invisible()
        }
    }
}