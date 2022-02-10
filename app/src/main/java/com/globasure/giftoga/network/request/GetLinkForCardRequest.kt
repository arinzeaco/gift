package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.globasure.giftoga.constant.ChargeType
import com.globasure.giftoga.constant.Constant
import com.globasure.giftoga.constant.MethodType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetLinkForCardRequest(
    @SerializedName("amount") val amount: String,
    @SerializedName("charge_type") val chargeType: String = ChargeType.VERIFICATION.type,
    @SerializedName("method") val method: String = MethodType.CARD.type,
    @SerializedName("channel") val channel: String = Constant.DEFAULT_CHANNEL
) : Parcelable