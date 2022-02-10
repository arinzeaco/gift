package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendGiftCardResponse(
    @SerializedName("data") val data: SendGiftCardData,
    @SerializedName("message") val message: String,
) : Parcelable

@Parcelize
data class SendGiftCardData(
    @SerializedName("send_type") val sendType: String,
    @SerializedName("friend_name") val friendName: String,
    @SerializedName("friend_email") val friendEmail: String,
    @SerializedName("reference") val reference: String
) : Parcelable
