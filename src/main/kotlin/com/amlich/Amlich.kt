package com.amlich

import java.time.Month
import java.time.Period
import java.time.ZoneId

var TimeZoneOffset:Int = 7
const val VNTimeZoneName = "Asia/Ho_Chi_Minh"
val VNTimeZone = ZoneId.of(VNTimeZoneName)
val UTCTimeZone = ZoneId.of("UTC")

fun today(): VNDate {
    return VNDate(TimeZoneOffset)
}

fun getMonthDates(year: Int, month: Month, zone: ZoneId = VNTimeZone): List<VNDate> {
    var dates: MutableList<VNDate> = mutableListOf()

    val start = VNDate.ofLocal(year, month, 1, 12, 0, 0, 0, zone)
    dates.add(start)
    // Minimum is 28 days so with 27 we reach 28
    for (i in 1..27) {
        val d = start.plus(Period.ofDays(i))
        dates.add(d)
    }
    // From date 29 to 31 we need to check if we go over to next month
    for (i in 28..30) {
        val d = start.plus(Period.ofDays(i))
        // next month
        if (d.solarDateTime().month != month) {
            break
        }
        dates.add(d)
    }
    return dates
}

fun getYearMonthDates(year: Int, zone: ZoneId = VNTimeZone): Map<Month, List<VNDate>> {
    val months: MutableMap<Month, List<VNDate>> = mutableMapOf()
    for (m in Months) {
        months.put(m, getMonthDates(year, m, zone = zone))
    }
    return months
}


val Months: Set<Month> = setOf(
    Month.JANUARY,
    Month.FEBRUARY,
    Month.MARCH,
    Month.APRIL,
    Month.MAY,
    Month.JUNE,
    Month.JULY,
    Month.AUGUST,
    Month.SEPTEMBER,
    Month.OCTOBER,
    Month.NOVEMBER,
    Month.DECEMBER
)