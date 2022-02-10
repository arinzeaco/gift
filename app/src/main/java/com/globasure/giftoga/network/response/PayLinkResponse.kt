package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PayLinkResponse(
    @SerializedName("data") val data: PayLinkData
) : Parcelable

@Parcelize
data class PayLinkData(
    @SerializedName("paylink") val payLink: String,
    @SerializedName("paynumber") val payNumber: String
) : Parcelable
