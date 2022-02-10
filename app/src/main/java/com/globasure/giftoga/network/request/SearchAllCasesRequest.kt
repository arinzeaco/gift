package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchAllCasesRequest(
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("search") val search: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("order_by") val orderBy: String?,
    @SerializedName("page") val page: String?,
    @SerializedName("per_page") val perPage: String?
) : Parcelable