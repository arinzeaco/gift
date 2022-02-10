package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryListResponse(
    @SerializedName("data") val data: List<CountryData>
) : Parcelable

@Parcelize
data class CountryData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("short_name") val shortName: String,
    @SerializedName("dial_code") val dialCode: String,
    @SerializedName("flag_image") val flagImage: String
) : Parcelable