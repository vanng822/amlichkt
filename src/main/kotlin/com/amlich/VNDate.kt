package com.amlich

import java.time.LocalDateTime
import java.time.Month

class VNDate {
    private var timeZoneOffset: Int = TimeZoneOffset
    private var solarTime: LocalDateTime
    private var lunarDate: LunarDate

    val day: Int
        get() = lunarDate.day

    val month: Month
        get() = Month.of(lunarDate.month)

    val year: Int
        get() = lunarDate.year

    constructor(timeZoneOffset: Int = TimeZoneOffset): this(LocalDateTime.now(), timeZoneOffset)

    // TODO: make sure solarTime is in vietnamese timezone
    constructor(solarTime: LocalDateTime, timeZoneOffset: Int = TimeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset
        this.solarTime = solarTime
        this.lunarDate = solar2lunar(solarTime.year, solarTime.month.value, solarTime.dayOfMonth, timeZoneOffset)
    }

    constructor(lunarDate: LunarDate, timeZoneOffset: Int = TimeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset
        val solarDate = lunar2solar(lunarDate.year, lunarDate.month, lunarDate.day, lunarDate.leap.toInt(), timeZoneOffset)
        this.solarTime = LocalDateTime.of(solarDate.year, solarDate.month, solarDate.day, 12, 12)
        this.lunarDate = lunarDate
    }

    fun date(): Triple<Int, Int, Int> {
        return Triple(lunarDate.year, lunarDate.month, lunarDate.day)
    }

    fun lunarDate(): LunarDate {
        return lunarDate
    }

    fun solarTime(): LocalDateTime {
        return solarTime
    }

    override fun toString(): String {
        return "${padd(year)}-${padd(month.value)}-${padd(day)}"
    }
}