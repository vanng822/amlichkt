package com.amlich

import java.time.LocalTime

class VNDate( var solarTime: LocalTime, var lunarDate: LunarDate, var timeZoneOffset: Int) {

    val day: Int
        get() = lunarDate.day

    val month: Int
        get() = lunarDate.month

    val year: Int
        get() = lunarDate.year

    fun date(): Triple<Int, Int, Int> {
        return Triple(lunarDate.year, lunarDate.month, lunarDate.day)
    }

    fun lunarDate(): LunarDate {
        return lunarDate
    }

    fun solarTime(): LocalTime {
        return solarTime
    }

    override fun toString(): String {
        return "${padd(year)}-${padd(month)}-${padd(day)}"
    }
}