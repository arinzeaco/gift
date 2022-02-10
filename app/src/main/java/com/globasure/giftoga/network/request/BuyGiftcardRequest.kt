package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BuyGiftcardRequest(
    @SerializedName("payment_token") val payment_token: String,
    @SerializedName("buy_type") val buyType: String,
    @SerializedName("friend_name") val friendName: String?,
    @SerializedName("friend_email") val friendEmail: String?,
    @SerializedName("friend_phone") val friendPhone: String?,
    @SerializedName("message") val message: String?
) : Parcelable