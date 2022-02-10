package com.globasure.giftoga.ui.base

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

open class BaseViewModel<V> : ViewModel() {

    private var view: WeakReference<V>? = null

    fun getView(): V? {
        return this.view?.get()
    }

    fun setView(view: V) {
        this.view = WeakReference(view)
    }
}