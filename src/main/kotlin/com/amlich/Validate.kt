package com.amlich

fun isValid(lunarDate: LunarDate): Boolean {
    val solarDate = lunar2solar(lunarDate.year, lunarDate.month, lunarDate.day, lunarDate.leap, TimeZoneOffset)
    val reversedDate = solar2lunar(solarDate.year, solarDate.month, solarDate.day, TimeZoneOffset)
    return lunarDate == reversedDate
}