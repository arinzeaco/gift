package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardOtpResponse(
    @SerializedName("data") val data: ChargeOtpData,
    @SerializedName("message") val message: String
) : Parcelable

@Parcelize
data class ChargeOtpData(
    @SerializedName("reference") val reference: String,
    @SerializedName("payment_token") val paymentToken: String,
) : Parcelable
