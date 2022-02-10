package com.globasure.giftoga.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorResponse(
    @SerializedName("brand_name") val brandName: Boolean,
    @SerializedName("utility_name") val utilityName: String?,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: ErrorData?,
    @SerializedName("response_code") val responseCode: String?,
    @SerializedName("message") val message: String?
) : Parcelable

@Parcelize
data class ErrorData(
    @SerializedName("code") val status: Int?,
    @SerializedName("errors") val errors: ErrorDetail?
) : Parcelable

@Parcelize
data class ErrorDetail(
    @SerializedName("message") val message: String?
) : Parcelable