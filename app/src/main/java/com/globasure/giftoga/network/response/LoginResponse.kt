package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("data") val data: LoginData,
    @SerializedName("refresh_token") val refreshToken: String
) : Parcelable

@Parcelize
data class LoginData(
    @SerializedName("code") val code: Int,
    @SerializedName("token") val token: String
) : Parcelable