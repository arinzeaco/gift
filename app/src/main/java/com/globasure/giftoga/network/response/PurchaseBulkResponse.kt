package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PurchaseBulkResponse(
    @SerializedName("data") val data: List<PurchaseBulkData>
) : Parcelable

@Parcelize
data class PurchaseBulkData(
    @SerializedName("row") val row: String,
    @SerializedName("fullname") val fullName: String,
    @SerializedName("email") val email: TransactionDetailData,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("giftcard") val giftcard: String,
    @SerializedName("order_id") val orderId: String
) : Parcelable

