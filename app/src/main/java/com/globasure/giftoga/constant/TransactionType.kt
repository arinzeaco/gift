package com.globasure.giftoga.constant

enum class TransactionType(val type: String) {
    DEPOSIT("Deposit"),
    CHARGE("Charge"),
    REDEEM("RedeemGiftCard"),
    SALES("Sales"),
    VERIFICATION("Verification")
}