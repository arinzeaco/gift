package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BankListResponse(
    @SerializedName("data") val data: BankListData
) : Parcelable

@Parcelize
data class BankListData(
    @SerializedName("currency") val currency: String,
    @SerializedName("bank") val bank: List<BankData>
) : Parcelable

@Parcelize
data class BankData(
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String
) : Parcelable