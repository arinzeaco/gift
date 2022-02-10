package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendToFriendRequest(
    @SerializedName("buy_type") val buyType: String,
    @SerializedName("friend_name") val friendName: String?,
    @SerializedName("friend_email") val friendEmail: String?,
    @SerializedName("friend_phone") val friendPhone: String?,
    @SerializedName("message") val message: String?
) : Parcelable