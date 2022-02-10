package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileResponse(
    @SerializedName("data") val data: ProfileData
) : Parcelable

@Parcelize
data class ProfileData(
    @SerializedName("details") val user: UserDetail?
) : Parcelable