package com.amlich

internal fun padd(number: Int): String {
    return if (number > 9) "${number}" else "0${number}"
}