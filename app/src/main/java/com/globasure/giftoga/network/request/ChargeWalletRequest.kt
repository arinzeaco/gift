package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeWalletRequest(
    @SerializedName("pay_link") var payLink: String
) : Parcelable