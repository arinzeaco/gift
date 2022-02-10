package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResetTokenResponse(
    @SerializedName("data") val data: ResetTokenData
) : Parcelable

@Parcelize
data class ResetTokenData(
    @SerializedName("email") val email: String
) : Parcelable