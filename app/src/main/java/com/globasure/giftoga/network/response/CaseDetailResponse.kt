package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CaseDetailResponse(
    @SerializedName("data") val data: CaseDetailData
) : Parcelable

@Parcelize
data class CaseDetailData(
    @SerializedName("details") val details: Case
) : Parcelable
