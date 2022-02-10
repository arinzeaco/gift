package com.globasure.giftoga.ui.custom.xtrength.internal

/**
 * Created by longnguyen on 7:58 AM, 4/17/20.
 *
 */
object Defaults {
    const val ALLOWED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 1234567890 !@#$%^&*()<>?:;'[]\\{}|"
}

object Rate {
    const val MAX = 100
    const val CHARACTER = 3
    const val CASE_SENSITIVITY = 2
    const val NUMBERS = 3
    const val SYMBOLS = 6
    const val MIDDLE_NUMBERS_SYMBOLS = 2
    const val REQUIREMENTS = 2
}

object RegexPattern {
    const val DIGITS = "\\d"
    const val SYMBOLS = "[^A-Za-z0-9\\s]"
    const val UPPERCASE = "[A-Z]"
    const val LOWERCASE = "[a-z]"
}