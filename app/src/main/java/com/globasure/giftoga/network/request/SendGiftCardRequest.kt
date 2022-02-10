package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendGiftCardRequest(
    @SerializedName("message") val message: String,
    @SerializedName("friend_email") val friendEmail: String,
    @SerializedName("friend_name") val friendName: String,
    @SerializedName("send_type") val sendType: String?, //self or friend
    @SerializedName("reference") val reference: String? //order_id
) : Parcelable