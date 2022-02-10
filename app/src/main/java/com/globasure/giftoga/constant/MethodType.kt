package com.globasure.giftoga.constant

enum class MethodType(val type: String) {
    BALANCE("Balance"),
    CARD("Card"),
    STRIPE("Stripe"),
    BANK_TRANSFER("bank_transfer"),
    PAY_STACK("paystack"),
    PAYPAL("paypal"),
    GIFT_CARD("GiftCard")
}