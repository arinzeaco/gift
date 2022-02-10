package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeCardRequest(
    @SerializedName("pay_link") var payLink: String?,
    @SerializedName("save_card") val saveCard: Boolean?,
    @SerializedName("card_token") val cardToken: String?,
    @SerializedName("card_number") val cardNumber: String?,
    @SerializedName("expiry_month") val expiryMonth: String?,
    @SerializedName("expiry_year") val expiryYear: String?,
    @SerializedName("cvv") val cvv: String?
) : Parcelable