package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargePayPalResponse(
    @SerializedName("data") val data: ChargePayPalData,
    @SerializedName("message") val message: String
) : Parcelable

@Parcelize
data class ChargePayPalData(
    @SerializedName("method") val method: String,
    @SerializedName("client_id") val clientId: String?,
    @SerializedName("order_id") val orderId: String?,
    @SerializedName("reference") val reference: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("charge_type") val chargeType: String,
    @SerializedName("channel") val channel: String,
    @SerializedName("desc") val description: String?,
    @SerializedName("status") val status: String,
    @SerializedName("payment_token") val paymentToken: String?, //only have value when confirm paypal done
    @SerializedName("app_android_return_url") val returnUrl: String?
) : Parcelable

