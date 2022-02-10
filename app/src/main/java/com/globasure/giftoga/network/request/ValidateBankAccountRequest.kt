package com.globasure.giftoga.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidateBankAccountRequest(
    @SerializedName("bank_name") val bankName: String,
    @SerializedName("bank_code") val bankCode: String,
    @SerializedName("account_number") val accountNumber: String //10 digits
) : Parcelable