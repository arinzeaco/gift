package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidateTokenResponse(
    @SerializedName("data") val data: ValidateTokenData
) : Parcelable

@Parcelize
data class ValidateTokenData(
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String
) : Parcelable