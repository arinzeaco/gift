package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CloseCaseResponse(
    @SerializedName("data") val data: CloseCaseData
) : Parcelable

@Parcelize
data class CloseCaseData(
    @SerializedName("case") val case: Case
) : Parcelable
