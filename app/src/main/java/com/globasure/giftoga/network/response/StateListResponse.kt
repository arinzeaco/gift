package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StateListResponse(
    @SerializedName("data") val data: List<StateData>
) : Parcelable

@Parcelize
data class StateData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
) : Parcelable