package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoveCardResponse(
    @SerializedName("data") val data: RemoveCardData
) : Parcelable

@Parcelize
data class RemoveCardData(
    @SerializedName("message") val message: String
) : Parcelable
