package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VerificationResponse(
    @SerializedName("data") val data: VerificationData,
    @SerializedName("message") val message: String
) : Parcelable

@Parcelize
data class VerificationData(
    @SerializedName("verification") val verification: Verification
) : Parcelable

@Parcelize
data class Verification(
    @SerializedName("status") val status: String,
    @SerializedName("account_type") val accountType: String,
    @SerializedName("bvn") val bvn: String,
    @SerializedName("rc_number") val rcNumber: String?,
    @SerializedName("document_type") val documentType: String,
    @SerializedName("document_file") val documentFile: String,
    @SerializedName("reason") val reason: String
) : Parcelable