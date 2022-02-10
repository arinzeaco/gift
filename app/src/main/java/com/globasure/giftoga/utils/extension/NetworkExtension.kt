@file:Suppress("DEPRECATION")

package com.globasure.giftoga.utils.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun Context.isConnected(): Boolean {
    val cm: ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo? = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}