package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResetPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String,
    @SerializedName("password") val password: String
) : Parcelable