package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.globasure.giftoga.constant.Constant
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetLinkForSalesRequest(
    @SerializedName("buy_type") val buyType: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("channel") val channel: String = Constant.DEFAULT_CHANNEL,
    @SerializedName("charge_type") val chargeType: String, //sales
    @SerializedName("method") val method: String,
    @SerializedName("merchant") val merchant: String,
    @SerializedName("has_bulkfile") val hasBulkFile: Boolean = false,
) : Parcelable