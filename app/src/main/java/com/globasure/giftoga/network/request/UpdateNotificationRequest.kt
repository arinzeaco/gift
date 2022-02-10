package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateNotificationRequest(
    @SerializedName("email_purchases") val emailPurchases: String, //1 is true, 0 is false
    @SerializedName("sms_purchases") val smsPurchases: String, //1 is true, 0 is false
    @SerializedName("email_redeem") val emailRedeem: String, //1 is true, 0 is false
    @SerializedName("sms_redeem") val smsRedeem: String //1 is true, 0 is false
) : Parcelable