package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtpPinRequest(
    @SerializedName("reference") val reference: String?, //must be 16 digits such GOCC64309394
    @SerializedName("otp") val otp: String?,
) : Parcelable