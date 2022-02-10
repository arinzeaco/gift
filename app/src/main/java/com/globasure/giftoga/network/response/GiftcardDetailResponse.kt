package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GiftcardDetailResponse(
    @SerializedName("data") val data: GiftcardDetailData
) : Parcelable

@Parcelize
data class GiftcardDetailData(
    @SerializedName("name") val name: String,
    @SerializedName("pin") val pin: String,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("remain_amount") val remainAmount: String,
    @SerializedName("created_date") val createdDate: String
) : Parcelable
