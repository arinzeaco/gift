package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MerchantDetailResponse(
    @SerializedName("data") val data: Merchant
) : Parcelable
