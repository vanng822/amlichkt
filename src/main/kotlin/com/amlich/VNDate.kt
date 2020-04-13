package com.amlich

import java.time.*

class VNDate {
    private var timeZoneOffset: Int = TimeZoneOffset
    private var solarTime: ZonedDateTime
    private var lunarDate: LunarDate

    val day: Int
        get() = lunarDate.day

    val month: Month
        get() = Month.of(lunarDate.month)

    val year: Int
        get() = lunarDate.year

    constructor(timeZoneOffset: Int = TimeZoneOffset): this(ZonedDateTime.now(VNTimeZone), timeZoneOffset)

    // TODO: make sure solarTime is in vietnamese timezone
    constructor(solarTime: ZonedDateTime, timeZoneOffset: Int = TimeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset
        this.solarTime = fixTimezone(solarTime)
        this.lunarDate = solar2lunar(this.solarTime.year, this.solarTime.month.value, this.solarTime.dayOfMonth, timeZoneOffset)
    }

    constructor(lunarDate: LunarDate, timeZoneOffset: Int = TimeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset
        val solarDate = lunar2solar(lunarDate.year, lunarDate.month, lunarDate.day, lunarDate.leap.toInt(), timeZoneOffset)
        this.solarTime = ZonedDateTime.of(solarDate.year, solarDate.month, solarDate.day, 12, 12, 0, 0, ZoneId.of(
            VNTimeZoneName))
        this.lunarDate = lunarDate
    }

    protected fun fixTimezone(solarTime: ZonedDateTime): ZonedDateTime {
        if (solarTime.zone != VNTimeZone) {
            return solarTime.withZoneSameInstant(VNTimeZone)
        }
        return solarTime
    }

    fun date(): Triple<Int, Int, Int> {
        return Triple(lunarDate.year, lunarDate.month, lunarDate.day)
    }

    fun lunarDate(): LunarDate {
        return lunarDate
    }

    fun solarTime(): ZonedDateTime {
        return solarTime
    }

    override fun toString(): String {
        return "${padd(year)}-${padd(month.value)}-${padd(day)}"
    }

    companion object {
        fun of(year: Int, month: Month, day: Int, hour: Int, minute: Int, second: Int, nanoSecond: Int): VNDate {
            val solarDate = ZonedDateTime.of(year, month.value, day, hour, minute, second, nanoSecond, ZoneId.of("UTC"))
            val vnSolarDate = solarDate.withZoneSameInstant(VNTimeZone)
            return VNDate(vnSolarDate, TimeZoneOffset)
        }
    }
}