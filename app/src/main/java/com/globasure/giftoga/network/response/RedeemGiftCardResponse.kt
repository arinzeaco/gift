package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RedeemGiftCardResponse(
    @SerializedName("data") val data: RedeemGiftCardData
) : Parcelable

@Parcelize
data class RedeemGiftCardData(
    @SerializedName("giftcard_name") val giftcardName: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("remain_amount") val remainAmount: String,
    @SerializedName("qr_image") val qrImage: String,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("fees") val fees: String,
    @SerializedName("total") val total: String,
    @SerializedName("commission") val commission: String,
    @SerializedName("commission_rate") val commission_rate: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("status") val status: String,
    @SerializedName("transaction_mode") val transactionMode: String,
    @SerializedName("date") val date: String,
    @SerializedName("created_date") val createdDate: String
) : Parcelable
