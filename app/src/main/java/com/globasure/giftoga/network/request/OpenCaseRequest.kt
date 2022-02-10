package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OpenCaseRequest(
    @SerializedName("issue_type") val issueType: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("notes") val notes: String?
) : Parcelable