package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeFeesResponse(
    @SerializedName("data") val data: ChargeFeesData
) : Parcelable

@Parcelize
data class ChargeFeesData(
    @SerializedName("exchange_rate") val exchangeRate: String,
    @SerializedName("from_currency_name") val fromCurrencyName: String,
    @SerializedName("to_currency_name") val toCurrencyName: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("fees") val fees: String,
    @SerializedName("sms_fees") val smsFees: String,
    @SerializedName("charge") val charge: String
) : Parcelable
