package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferCommissionResponse(
    @SerializedName("data") val data: TransferCommissionData
) : Parcelable

@Parcelize
data class TransferCommissionData(
    @SerializedName("new_balance") val newBalance: NewBalance,
    @SerializedName("transaction") val transaction: TransactionDetail
) : Parcelable

@Parcelize
data class TransactionDetail(
    @SerializedName("type") val transaction: String,
    @SerializedName("short_type") val short_Type: String,
    @SerializedName("data") val data: TransactionDetailData,
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
    @SerializedName("transaction_mode") val transactionMode: String,
    @SerializedName("created_date") val createdDate: String
) : Parcelable

@Parcelize
data class NewBalance(
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("wallet_balance") val walletBalance: String,
    @SerializedName("commission") val commission: String
) : Parcelable

@Parcelize
data class TransactionDetailData(
    @SerializedName("giftcard_name") val giftCardName: String
) : Parcelable
