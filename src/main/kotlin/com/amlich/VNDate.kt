package com.amlich

import java.time.*

class VNDate {
    private var timeZoneOffset: Int = TimeZoneOffset
    private var solarDateTime: ZonedDateTime
    private var lunarDate: LunarDate

    val dayOfMonth: Int
        get() = lunarDate.day

    val month: Month
        get() = Month.of(lunarDate.month)

    val year: Int
        get() = lunarDate.year

    constructor(timeZoneOffset: Int = TimeZoneOffset): this(ZonedDateTime.now(VNTimeZone), timeZoneOffset)

    constructor(solarTime: ZonedDateTime, timeZoneOffset: Int = TimeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset
        this.solarDateTime = fixTimezone(solarTime)
        this.lunarDate = solar2lunar(this.solarDateTime.year, this.solarDateTime.month.value, this.solarDateTime.dayOfMonth, timeZoneOffset)
    }

    constructor(lunarDate: LunarDate, timeZoneOffset: Int = TimeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset
        val solarDate = lunar2solar(lunarDate.year, lunarDate.month, lunarDate.day, lunarDate.leap, timeZoneOffset)
        this.solarDateTime = ZonedDateTime.of(solarDate.year, solarDate.month, solarDate.day, 12, 0, 0, 0, VNTimeZone)
        this.lunarDate = lunarDate
        if (!isValid()) {
            throw DateTimeException("Invalid date '${lunarDate.toString()}'")
        }
    }

    protected fun fixTimezone(solarTime: ZonedDateTime): ZonedDateTime {
        if (solarTime.zone != VNTimeZone) {
            return solarTime.withZoneSameInstant(VNTimeZone)
        }
        return solarTime
    }

    fun isValid(): Boolean {
        val reversedDate = solar2lunar(solarDateTime.year, solarDateTime.month.value, solarDateTime.dayOfMonth, TimeZoneOffset)
        return lunarDate == reversedDate
    }

    fun date(): Triple<Int, Month, Int> {
        return Triple(lunarDate.year, Month.of(lunarDate.month), lunarDate.day)
    }

    fun lunarDate(): LunarDate {
        return lunarDate
    }

    fun solarDateTime(): ZonedDateTime {
        return solarDateTime
    }

    fun addDate(years: Int, months: Int, days: Int): VNDate {
        val period = Period.of(years, months, days)
        return plus(period)
    }

    operator fun plus(period: Period): VNDate {
        val newSolarTime = this.solarDateTime.plus(period)
        return VNDate(newSolarTime, TimeZoneOffset)
    }

    operator fun minus(period: Period): VNDate {
        val newSolarTime = this.solarDateTime.minus(period)
        return VNDate(newSolarTime, TimeZoneOffset)
    }

    fun isAfter(other: VNDate): Boolean {
        return this.solarDateTime.isAfter(other.solarDateTime())
    }

    fun isBefore(other: VNDate): Boolean {
        return this.solarDateTime.isBefore(other.solarDateTime())
    }

    fun isEqual(other: VNDate): Boolean {
        return this.solarDateTime.isEqual(other.solarDateTime())
    }

    operator fun compareTo(other: VNDate): Int {
        if (isEqual(other)) {
            return 0
        }
        if (isAfter(other)) {
            return 1
        }
        return -1
    }

    override operator fun equals(other: Any?): Boolean {
        return isEqual(other as VNDate)
    }

    override fun toString(): String {
        return "${padd(year)}-${padd(month.value)}-${padd(dayOfMonth)}"
    }

    companion object {
        fun ofUTC(year: Int, month: Month, day: Int, hour: Int, minute: Int, second: Int, nanoSecond: Int): VNDate {
            return ofLocal(year, month, day, hour, minute, second, nanoSecond, UTCTimeZone)
        }

        fun of(year: Int, month: Month, day: Int, hour: Int, minute: Int, second: Int, nanoSecond: Int): VNDate {
            return ofLocal(year, month, day, hour, minute, second, nanoSecond, VNTimeZone)
        }

        fun of(solarTime: LocalDateTime): VNDate {
            val localTimeZone = ZoneId.systemDefault()
            return ofLocal(solarTime.year, solarTime.month, solarTime.dayOfMonth, solarTime.hour, solarTime.minute, solarTime.second, solarTime.nano, localTimeZone)
        }

        fun ofLocal(year: Int, month: Month, day: Int, hour: Int, minute: Int, second: Int, nanoSecond: Int, zone: ZoneId): VNDate {
            val solarDate = ZonedDateTime.of(year, month.value, day, hour, minute, second, nanoSecond, zone)
            val vnSolarDate = solarDate.withZoneSameInstant(VNTimeZone)
            return VNDate(vnSolarDate, TimeZoneOffset)
        }
    }
}