package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignUpResponse(
    @SerializedName("data") val data: SignUpPersonalData
) : Parcelable

@Parcelize
data class SignUpPersonalData(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserDetail
) : Parcelable