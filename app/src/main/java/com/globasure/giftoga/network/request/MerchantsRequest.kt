package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MerchantsRequest(
    @SerializedName("search") val search: String? = "",
    @SerializedName("country") val country: String? = "",
    @SerializedName("state") val state: String? = "",
    @SerializedName("order_by") val orderBy: String? = "",
    @SerializedName("page") val page: String? = "",
    @SerializedName("per_page") val perPage: String? = ""
) : Parcelable