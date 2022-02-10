package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OpenCaseResponse(
    @SerializedName("data") val data: OpenCaseData
) : Parcelable

@Parcelize
data class OpenCaseData(
    @SerializedName("case") val case: Case
) : Parcelable

@Parcelize
data class Case(
    @SerializedName("status") val status: String,
    @SerializedName("type") val type: String,
    @SerializedName("case_id") val caseId: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("open_date") val openDate: String
) : Parcelable
