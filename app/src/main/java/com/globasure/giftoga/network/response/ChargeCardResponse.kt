package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeCardResponse(
    @SerializedName("data") val data: ChargeCardData,
    @SerializedName("message") val message: String
) : Parcelable

@Parcelize
data class ChargeCardData(
    @SerializedName("result") val result: String?,
    @SerializedName("reference") val reference: String,
    @SerializedName("payment_token") val paymentToken: String?
) : Parcelable
