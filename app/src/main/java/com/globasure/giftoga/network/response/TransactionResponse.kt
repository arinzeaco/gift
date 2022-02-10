package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionResponse(
    @SerializedName("data") val data: TransactionData
) : Parcelable

@Parcelize
data class TransactionData(
    @SerializedName("transaction") val transaction: List<Transaction>,
    @SerializedName("meta") val meta: MetaData
) : Parcelable

@Parcelize
data class Transaction(
    @SerializedName("type") val type: String,
    @SerializedName("data") val data: Data?,
    @SerializedName("method") val method: String,
    @SerializedName("method_mask") val methodMask: String,
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
    @SerializedName("title") val title: String,
    @SerializedName("transaction_mode") val transactionMode: String,
    @SerializedName("created_date") val createdDate: String
) : Parcelable

@Parcelize
data class MetaData(
    @SerializedName("total") val total: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("page_count") val pageCount: Int
) : Parcelable

@Parcelize
data class Data(
    @SerializedName("giftcard_name") val giftCardName: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("remain_amount") val remainAmount: String,
    @SerializedName("qr_image") val qrImage: String?,
    @SerializedName("buy_type") val buyType: String,
    @SerializedName("friend_name") val friendName: String,
    @SerializedName("friend_email") val friendEmail: String,
    @SerializedName("friend_phone") val friendPhone: String,
    @SerializedName("message") val message: String,
    @SerializedName("order_id") val orderId: String,
    @SerializedName("status") val status: String
) : Parcelable
