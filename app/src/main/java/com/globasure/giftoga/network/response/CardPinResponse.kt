package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardPinResponse(
    @SerializedName("data") val data: ChargePinData,
    @SerializedName("message") val message: String
) : Parcelable

@Parcelize
data class ChargePinData(
    @SerializedName("reference") val reference: String,
    @SerializedName("payment_token") val paymentToken: String,
) : Parcelable
