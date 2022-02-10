package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidateBankAccountResponse(
    @SerializedName("data") val data: BankAccountData
) : Parcelable

@Parcelize
data class BankAccountData(
    @SerializedName("bank_name") val bankName: String,
    @SerializedName("bank_code") val bankCode: String,
    @SerializedName("account_number") val accountNumber: String,
    @SerializedName("account_name") val accountName: String,
    @SerializedName("validate_reference") val validateReference: String
) : Parcelable