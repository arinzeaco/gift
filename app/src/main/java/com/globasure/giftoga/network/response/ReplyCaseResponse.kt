package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReplyCaseResponse(
    @SerializedName("data") val data: ReplyCaseData
) : Parcelable

@Parcelize
data class ReplyCaseData(
    @SerializedName("reply") val reply: Reply
) : Parcelable

@Parcelize
data class Reply(
    @SerializedName("case_id") val caseId: String,
    @SerializedName("comment_id") val commentId: String,
    @SerializedName("comment") val comment: String,
    @SerializedName("reply_by") val replyBy: String,
    @SerializedName("open_date") val openDate: String
) : Parcelable
