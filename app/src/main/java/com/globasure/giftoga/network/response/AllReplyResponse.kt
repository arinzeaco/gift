package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllReplyResponse(
    @SerializedName("data") val data: AllReplyData
) : Parcelable

@Parcelize
data class AllReplyData(
    @SerializedName("reply") val reply: List<Reply>
) : Parcelable
