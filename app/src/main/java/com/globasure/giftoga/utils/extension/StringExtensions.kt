package com.globasure.giftoga.utils.extension

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import java.text.DecimalFormat
import java.text.NumberFormat

fun String.formatCurrency(isPrefix: Boolean, currencyName: String?): String {
    val formatter: NumberFormat = DecimalFormat("#,###,###.00")
    val builder = StringBuilder()
    return when {
        (this == "0" || this == "0.0" || this.isEmpty()) -> {
            if (!currencyName.isNullOrBlank()) "$currencyName 0.00" else "₦ 0.00"
        }
        (this[0] != '#') -> {
            if (!currencyName.isNullOrBlank()) "$currencyName $this" else "₦ $this"
        }
        this.contains("₦") -> {
            this
        }
        else -> {
            val result = if (this.contains(",")) {
                this.replace(",", "").trim()
            } else {
                this
            }
            builder.append(formatter.format(result.toDouble()))
            if (isPrefix) {
                builder.insert(0, if (!currencyName.isNullOrBlank()) "$currencyName " else "₦ ")
            }
            builder.toString()
        }
    }
}

fun String.formatDecimal(): String {
    val formatter: NumberFormat = DecimalFormat("#,###,###.00")
    val builder = StringBuilder()
    builder.append(formatter.format(this.toDouble()))
    return builder.toString()
}

fun String.convertCalculatedValue(): String {
    var result = ""
    if (this.contains("₦")) {
        result = this.replace("₦", "").trim()
        if (result.contains(",")) {
            result = result.replace(",", "").trim()
        }
    } else {
        if (this.contains(",")) {
            result = this.replace(",", "").trim()
        }
    }
    if (result.isNotEmpty()) {
        return result
    }
    return this
}

fun String.convertBalanceToSign(): String {
    return this.replace("NGN", "₦")
}

fun String.convertToLocationText(): SpannableString {
    val location = this.substring(0, this.indexOf("(") - 1)
    val state = this.substring(this.indexOf("(") + 1, this.indexOf(")"))
    val fullLocation = "$location\n$state"
    val spannableLocation = SpannableString(fullLocation)
    spannableLocation.setSpan(
        StyleSpan(Typeface.BOLD),
        0,
        fullLocation.indexOf("\n"),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableLocation.setSpan(
        RelativeSizeSpan(0.8f),
        fullLocation.indexOf("\n") + 1,
        spannableLocation.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableLocation
}