package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by longnguyen on 11:54 AM, 3/12/20.
 *
 */
@Parcelize
data class TransactionRecord(
    @SerializedName("brand_name") val brand_name: String,
    @SerializedName("utility_name") val utility_name: String,
    @SerializedName("data") val data: String,
) : Parcelable

@Parcelize
data class Transactions(
    @SerializedName("type") val id: String,
    @SerializedName("short_type") val short_type: String,
    @SerializedName("data") val customer: List<String>,
    @SerializedName("method") val method: String,
    @SerializedName("method_mask") val method_mask: String,
    @SerializedName("channel") val channel: String,
    @SerializedName("pos_terminal") val pos_terminal: String,
    @SerializedName("currency_name") val currency_name: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("fees") val fees: String,
    @SerializedName("total") val total: String,
    @SerializedName("commission") val commission: String,
    @SerializedName("commission_rate") val commissionRate: String,
    @SerializedName("order_id") val order_id: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("status") val status: String,
    @SerializedName("transaction_mode") val transactionMode: String,
    @SerializedName("created_date") val created_date: String,
) : Parcelable