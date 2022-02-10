package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetLinkForBulkRequest(
    @SerializedName("has_bulkfile") val hasBulkFile: Boolean = true,
    @SerializedName("validation_reference") val validationReference: String,
    @SerializedName("charge_type") val chargeType: String, //sales
    @SerializedName("method") val method: String,
    @SerializedName("channel") val channel: String
) : Parcelable