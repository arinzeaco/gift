package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllCasesResponse(
    @SerializedName("data") val data: AllCasesData
) : Parcelable

@Parcelize
data class AllCasesData(
    @SerializedName("dispute") val merchant: List<Case>,
    @SerializedName("meta") val meta: MetaData
) : Parcelable
