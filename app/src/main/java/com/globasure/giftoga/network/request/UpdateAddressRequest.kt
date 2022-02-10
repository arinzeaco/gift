package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateAddressRequest(
    @SerializedName("state") val state: String,
    @SerializedName("city") val city: String,
    @SerializedName("address") val address: String?,
    @SerializedName("postal_code") val postalCode: String?
) : Parcelable