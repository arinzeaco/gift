package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidateBulkFileResponse(
    @SerializedName("data") val data: ValidateBulkFileData
) : Parcelable

@Parcelize
data class ValidateBulkFileData(
    @SerializedName("amount") val amount: String,
    @SerializedName("reference") val reference: String
) : Parcelable
