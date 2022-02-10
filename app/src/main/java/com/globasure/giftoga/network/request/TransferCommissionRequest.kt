package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransferCommissionRequest(
    @SerializedName("validation_reference") val validationReference: String,
    @SerializedName("amount") val amount: String
) : Parcelable