package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllowDailyTransferRequest(
    @SerializedName("allow_transfer") val allowTransfer: String, //1 is true, 0 is false
    @SerializedName("validation_reference") val validationReference: String
) : Parcelable