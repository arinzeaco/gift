package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeBankTransferResponse(
    @SerializedName("data") val data: ChargeBankTransferData,
    @SerializedName("message") val message: String
) : Parcelable

@Parcelize
data class ChargeBankTransferData(
    @SerializedName("bank_name") val bankName: String,
    @SerializedName("account_name") val accountName: String,
    @SerializedName("account_number") val accountNumber: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("charge_type") val chargeType: String,
    @SerializedName("channel") val channel: String,
    @SerializedName("status") val status: String,
    @SerializedName("payment_token") val paymentToken: String? //only have value when confirm bank transfer
) : Parcelable

