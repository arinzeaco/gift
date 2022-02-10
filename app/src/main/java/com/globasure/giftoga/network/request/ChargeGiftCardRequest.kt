package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.globasure.giftoga.constant.Constant
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChargeGiftCardRequest(
    @SerializedName("giftcard") val giftcard: String?, //must be 16 digits
    @SerializedName("amount") val amount: String?,
    @SerializedName("quantity") val quantity: String?,
    @SerializedName("merchant") val merchant: String?,
    @SerializedName("has_bulkfile") val hasBulkfile: String?, //if yes -> this request should contain validationReference
    @SerializedName("charge_type") val chargeType: String?, // sales, deposit
    @SerializedName("channel") val channel: String = Constant.DEFAULT_CHANNEL
) : Parcelable