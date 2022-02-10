package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReplyDetailResponse(
    @SerializedName("data") val data: ReplyDetailData
) : Parcelable

@Parcelize
data class ReplyDetailData(
    @SerializedName("reply") val reply: Reply
) : Parcelable
