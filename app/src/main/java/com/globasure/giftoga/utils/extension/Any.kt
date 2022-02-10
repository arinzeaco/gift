package com.globasure.giftoga.utils.extension

fun <T, R> T?.runIfNotNull(block: (T) -> R): R? {
    return if (this != null) {
        block(this)
    } else {
        null
    }
}

fun <T> T?.runIfNotNull(block: (T) -> Unit) {
    runIfNotNull<T, Unit>(block)
}

inline fun <reified T, R> T.to(block: (T) -> R): R {
    return block(this)
}