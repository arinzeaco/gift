package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BuyGiftCardResponse(
//    @SerializedName("data") val data: BuyGiftCardData,
    @SerializedName("message") val message: String
) : Parcelable

//@Parcelize
//data class BuyGiftCardData(
//    @SerializedName("new_balance") val newBalance: BuyGiftNewBalance,
//) : Parcelable

@Parcelize
data class Purchase(
    @SerializedName("amount") val amount: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("giftcard") val giftcard: TransactionDetailData,
    @SerializedName("qr_image") val qrImage: String,
    @SerializedName("buy_type") val buyType: String,
    @SerializedName("friend_name") val friendName: String,
    @SerializedName("friend_email") val friendEmail: String,
    @SerializedName("friend_phone") val friendPhone: String,
    @SerializedName("message") val message: String,
    @SerializedName("order_id") val orderId: String
) : Parcelable

//@Parcelize
//data class BuyGiftNewBalance(
//    @SerializedName("currency_name") val currencyName: String,
//    @SerializedName("wallet_balance") val walletBalance: String,
//    @SerializedName("commission") val commission: String
//) : Parcelable
