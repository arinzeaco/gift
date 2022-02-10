package com.globasure.giftoga.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MerchantsResponse(
    @SerializedName("data") val data: MerchantsData
) : Parcelable

@Parcelize
data class MerchantsData(
    @SerializedName("merchant") val merchant: List<Merchant>,
    @SerializedName("meta") val meta: MetaData?
) : Parcelable

@Parcelize
data class Merchant(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("card_name") val cardName: String,
    @SerializedName("description") val description: String,
    @SerializedName("long_description") val longDescription: String,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("homepage_url") val homepageUrl: String,
    @SerializedName("min_card_value") val minCardValue: String,
    @SerializedName("max_card_value") val maxCardValue: String,
    @SerializedName("icon_url") val iconUrl: String,
    @SerializedName("legal_disclaimer_html") val legalDisclaimerHtml: String,
    @SerializedName("categories") val categories: List<Category>,
    @SerializedName("has_denomination") val hasDenomination: Boolean,
    @SerializedName("denomination") val denomination: List<String>,
    @SerializedName("is_fixed_amount") val isFixedAmount: Boolean
) : Parcelable

@Parcelize
data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
) : Parcelable
