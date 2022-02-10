package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail(
    @SerializedName("account_type") val accountType: String?,
    @SerializedName("account_mode") val accountMode: String?,
    @SerializedName("account_verified") val accountVerified: Boolean?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("business_name") val businessName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("mobile") val mobile: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("dial_code") val dialCode: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("postal_code") val postalCode: String?,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("notification") val notification: NotificationData?,
    @SerializedName("has_daily_transfer") val hasDailyTransfer: String?,
    @SerializedName("daily_transfer") val dailyTransfer: List<String>?,
    @SerializedName("balance") val balance: BalancePersonalData?,
    @SerializedName("created_date") val created_date: String?
) : Parcelable

@Parcelize
data class NotificationData(
    @SerializedName("email_purchases") val emailPurchases: Boolean?,
    @SerializedName("sms_purchases") val smsPurchases: Boolean?,
    @SerializedName("email_redeem") val emailRedeem: Boolean?,
    @SerializedName("sms_redeem") val smsRedeem: Boolean?,
    @SerializedName("email_deposit") val emailDeposit: Boolean?,
    @SerializedName("sms_deposit") val smsDeposit: Boolean?,
    @SerializedName("email_transfer_commission") val emailTransferCommission: Boolean?,
    @SerializedName("sms_transfer_commission") val smsTransferCommission: Boolean?,
    @SerializedName("email_transfer_redeem") val emailTransferRedeem: Boolean?,
    @SerializedName("sms_transfer_redeem") val smsTransferRedeem: Boolean?
) : Parcelable

@Parcelize
data class BalancePersonalData(
    @SerializedName("currency_name") val currencyName: String?,
    @SerializedName("wallet_balance") val walletBalance: String?,
    @SerializedName("commission") val commission: String?,
    @SerializedName("giftcard_sales") val giftcardSales: String?,
    @SerializedName("sales_amount") val salesAmount: String?,
    @SerializedName("redeem_amount") val redeemAmount: String?
) : Parcelable
