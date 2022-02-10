package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeFeesRequest(
    @SerializedName("delivery_to_sms") val deliveryToSms: String?,
    @SerializedName("delivery_country") val deliveryCountry: String?, //might have value of deliveryToSms = true
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("charge_type") val chargeType: String, // sales, deposit, commission, redeemBalance, redeemGiftCard
    @SerializedName("method") val method: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("merchant") val merchantId: String? // need this when chargeType = sales
) : Parcelable