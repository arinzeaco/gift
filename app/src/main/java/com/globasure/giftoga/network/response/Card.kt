package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    @SerializedName("fund_type") val fund_type: String,
    @SerializedName("balance_type") val balance_type: String,
    @SerializedName("amount") val amount: String,
) : Parcelable