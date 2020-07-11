package com.amlich

import java.time.*
import kotlin.math.abs

class VNCalendar {
    private var timeZoneOffset: Int = TimeZoneOffset
    private var selectedDate: VNDate

    val dayOfMonth: Int
        get() = selectedDate.dayOfMonth

    val month: Month
        get() = selectedDate.month

    val year: Int
        get() = selectedDate.year

    val zone: ZoneId
        get() = selectedDate.solarDateTime().zone

    val solarDateTime: ZonedDateTime
        get() = selectedDate.solarDateTime()

    private var dates: List<VNDate>

    constructor(timeZoneOffset: Int = TimeZoneOffset): this(today(), timeZoneOffset)

    constructor(selectedDate: VNDate, timeZoneOffset: Int = TimeZoneOffset) {
        this.selectedDate = selectedDate
        this.timeZoneOffset = timeZoneOffset
        val solarDate = selectedDate.solarDateTime()
        dates = getMonthDates(solarDate.year, solarDate.month, zone)
    }

    fun previousMonth() {
        dates = getMonthDates(solarDateTime.year, solarDateTime.month - 1, zone)
        selectedDate = fixSelectedDate(dates)
    }

    fun nextMonth() {
        dates = getMonthDates(solarDateTime.year, solarDateTime.month + 1, zone)
        selectedDate = fixSelectedDate(dates)
    }

    fun nextDate() {
        val d = selectedDate
        selectedDate = d.plus(Period.of(0, 0, 1))
        // We go to next month
        // step forward one month
        if (selectedDate.month > d.month) {
            nextMonth()
        }
    }

    fun previousDate() {
        val d = selectedDate
        selectedDate = d.minus(Period.of(0, 0, 1))
        // We go back to previous month
        // step one month back
        if (d.month > selectedDate.month) {
            previousMonth()
        }
    }

    fun getDates(): List<VNDate> {
        return dates
    }

    private fun fixSelectedDate(dates: List<VNDate>): VNDate {
        var date = selectedDate
        val solarDate = selectedDate.solarDateTime()
        val monthDiff = dates.first().solarDateTime().month.value - solarDate.month.value
        if (solarDate.dayOfMonth > dates.size) {
            date = date.minus(Period.of(0, 0, solarDate.dayOfMonth - dates.size))
        }
        if (monthDiff > 0) {
            return date.plus(Period.of(0, monthDiff, 0))
        }
        return date.minus(Period.of(0, abs(monthDiff), 0))
    }

    companion object {
        fun ofUTC(year: Int, month: Month): VNCalendar {
            return ofLocal(year, month, UTCTimeZone)
        }

        fun of(vnDate: VNDate): VNCalendar {
            return VNCalendar(vnDate, TimeZoneOffset)
        }

        fun of(solarTime: LocalDateTime): VNCalendar {
            val localTimeZone = ZoneId.systemDefault()
            return ofLocal(solarTime.year, solarTime.month, localTimeZone)
        }

        fun ofLocal(year: Int, month: Month, zone: ZoneId): VNCalendar {
            val vndate = VNDate.ofLocal(year, month, 1, 12, 0, 0, 0, zone)
            return VNCalendar(vndate, TimeZoneOffset)
        }
    }
}