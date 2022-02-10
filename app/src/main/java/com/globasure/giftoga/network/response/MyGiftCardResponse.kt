package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by longnguyen on 11:54 AM, 3/12/20.
 *
 */
@Parcelize
data class MyGiftCardResponse(
    @SerializedName("data") val data: GiftData,
    @SerializedName("message") val message: String

) : Parcelable

@Parcelize
data class GiftData(
    @SerializedName("giftcard") val name: List<Giftcard>,
) : Parcelable

@Parcelize
data class Giftcard(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("security") val security: String,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("remain_amount") val remainAmount: String,
    @SerializedName("qr_image") val qrImage: String,
    @SerializedName("buy_type") val buyType: String,
    @SerializedName("friend_name") val friendName: String?,
    @SerializedName("friend_email") val friendEmail: String?,
    @SerializedName("friend_phone") val friendPhone: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("order_id") val order_id: String,
    @SerializedName("status") val status: String,
    @SerializedName("date") val date: String,
    @SerializedName("created_date") val createdDate: String
) : Parcelable

