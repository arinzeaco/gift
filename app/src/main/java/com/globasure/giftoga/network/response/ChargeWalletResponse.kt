package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeWalletResponse(
    @SerializedName("data") val data: ChargeWalletData
) : Parcelable

@Parcelize
data class ChargeWalletData(
    @SerializedName("payment_token") val paymentToken: String
) : Parcelable

