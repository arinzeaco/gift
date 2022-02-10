package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListCardResponse(
    @SerializedName("data") val data: ListCardData
) : Parcelable

@Parcelize
data class ListCardData(
    @SerializedName("card") val list: List<CardData>,
    @SerializedName("meta") val metaData: MetaData
) : Parcelable

@Parcelize
data class CardData(
    @SerializedName("bank_name") val bankName: String,
    @SerializedName("card_number") val cardNumber: String,
    @SerializedName("card_brand") val cardBrand: String,
    @SerializedName("token") val token: String,
    @SerializedName("mode") val mode: String,
) : Parcelable
