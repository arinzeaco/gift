package com.globasure.giftoga.ui.custom.xtrength.entity

/**
 * Created by longnguyen on 7:56 AM, 4/17/20.
 *
 */
data class Xtrength(
    internal var complexitySet: Array<String> = arrayOf("Very Weak", "Weak", "Not Bad", "Strong", "Very Strong"),
    internal var hint: String = "Enter desired password",
    internal var isCommonWordsDisabled: Boolean = false,

    // View Configuration
    internal var padding: Float = 20f,
    internal var paddingLeft: Float = 14f,
    internal var paddingTop: Float = 0f,
    internal var paddingRight: Float = 20f,
    internal var paddingBottom: Float = 0f
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Xtrength

        if (!complexitySet.contentEquals(other.complexitySet)) return false

        return true
    }

    override fun hashCode(): Int {
        return complexitySet.contentHashCode()
    }
}