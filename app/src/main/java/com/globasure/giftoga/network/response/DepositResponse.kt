package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DepositResponse(
    @SerializedName("data") val data: DepositData,
    @SerializedName("message") val message: String

) : Parcelable

@Parcelize
data class DepositData(
    @SerializedName("type") val type: String,
    @SerializedName("short_type") val shortType: String,
    @SerializedName("data") val data: String?,
    @SerializedName("method") val method: String,
    @SerializedName("method_mask") val method_mask: String,
    @SerializedName("channel") val channel: String,
    @SerializedName("pos_terminal") val posTerminal: String,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("fees") val fees: String,
    @SerializedName("total") val total: String,
    @SerializedName("commission") val commission: String,
    @SerializedName("commission_rate") val commissionRate: String,
    @SerializedName("order_id") val orderId: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("status") val status: String,
    @SerializedName("transaction_mode") val transactionMode: String,
    @SerializedName("created_date") val createdDate: String
) : Parcelable
