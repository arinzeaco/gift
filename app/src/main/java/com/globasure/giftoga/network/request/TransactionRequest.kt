package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionRequest(
    @SerializedName("startDate") val startDate: String? = null,
    @SerializedName("endDate") val endDate: String? = null,
    @SerializedName("reference") val reference: String? = null,
    @SerializedName("method") val method: String? = null,
    @SerializedName("channel") val channel: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("order_by") val orderBy: Boolean? = null,
    @SerializedName("page") val page: Int? = null,
    @SerializedName("per_page") val perPage: Int = 0
) : Parcelable