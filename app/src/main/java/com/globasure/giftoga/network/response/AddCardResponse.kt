package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddCardResponse(
    @SerializedName("data") val data: AddCardData
) : Parcelable

@Parcelize
data class AddCardData(
    @SerializedName("message") val message: String
) : Parcelable
