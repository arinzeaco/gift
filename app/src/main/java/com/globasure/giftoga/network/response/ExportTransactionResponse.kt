package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExportTransactionResponse(
    @SerializedName("data") val data: ExportTransactionData
) : Parcelable

@Parcelize
data class ExportTransactionData(
    @SerializedName("download_file") val downloadFile: String
) : Parcelable
