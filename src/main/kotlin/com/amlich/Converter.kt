package com.amlich

import kotlin.math.PI
import kotlin.math.sin

data class SolarDate(val year: Int, val month: Int, val day: Int)
data class LunarDate(val year: Int, val month: Int, val day: Int, val leap: Boolean)

fun INT(number: Number): Int {
    return number.toInt()
}

fun DOUBLE(i: Int): Double {
    return i.toDouble()
}

fun jdFromDate(dd: Int, mm: Int, yyyy: Int): Int {
    var a: Int
    var y: Int
    var m: Int
    var jd: Int
    a = INT((14 - mm) / 12)
    y = yyyy + 4800 - a
    m = mm + 12 * a - 3
    jd = dd + INT((153 * m + 2) / 5) + 365 * y + INT(y / 4) - INT(y / 100) + INT(y / 400) - 32045
    if (jd < 2299161) {
        jd = dd + INT((153 * m + 2) / 5) + 365 * y + INT(y / 4) - 32083
    }
    return jd
}

fun jdToDate(jd: Int): SolarDate {
    var a: Int
    var b: Int
    var c: Int
    var d: Int
    var e: Int
    var m: Int
    var day: Int
    var month: Int
    var year: Int

    if (jd > 2299160) {
        // After 5/10/1582, Gregorian calendar
        a = jd + 32044
        b = INT((4 * a + 3) / 146097)
        c = a - INT((b * 146097) / 4)
    } else {
        b = 0
        c = jd + 32082
    }
    d = INT((4 * c + 3) / 1461)
    e = c - INT((1461 * d) / 4)
    m = INT((5 * e + 2) / 153)
    day = e - INT((153 * m + 2) / 5) + 1
    month = m + 3 - 12 * INT(m / 10)
    year = b * 100 + d - 4800 + INT(m / 10)

    return SolarDate(year, month, day)
}

fun newMoon(ak: Int): Double {
    var T: Double
    var T2: Double
    var T3: Double
    var dr: Double
    var Jd1: Double
    var M: Double
    var mPr: Double
    var F: Double
    var C1: Double
    var deltat: Double

    val k = ak.toDouble()
    T = k / 1236.85 // Time in Julian centuries from 1900 January 0.5
    T2 = T * T
    T3 = T2 * T
    dr = PI / 180
    Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3
    Jd1 = Jd1 + 0.00033 * ((166.56 + 132.87 * T - 0.009173 * T2) * dr)  // Mean new moon
    M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3     // Sun's mean anomaly
    mPr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3 // Moon's mean anomaly
    F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3     // Moon's argument of latitude
    C1 = (0.1734 - 0.000393 * T) * sin(M * dr) + 0.0021 * sin(2 * dr * M)
    C1 = C1 - 0.4068 * sin(mPr * dr) + 0.0161 * sin(dr * 2 * mPr)
    C1 = C1 - 0.0004 * sin(dr * 3 * mPr)
    C1 = C1 + 0.0104 * sin(dr * 2 * F) - 0.0051 * sin(dr * (M + mPr))
    C1 = C1 - 0.0074 * sin(dr * (M - mPr)) + 0.0004 * sin(dr * (2 * F + M))
    C1 = C1 - 0.0004 * sin(dr * (2 * F - M)) - 0.0006 * sin(dr * (2 * F + mPr))
    C1 = C1 + 0.0010 * sin(dr * (2 * F - mPr)) + 0.0005 * sin(dr * (2 * mPr + M))
    if (T < -11) {
        deltat = 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3
    } else {
        deltat = -0.000278 + 0.000265 * T + 0.000262 * T2
    }

    return Jd1 + C1 - deltat
}


fun sunLongitude(jdn: Double): Double {
    var T: Double
    var T2: Double
    var dr: Double
    var M: Double
    var L0: Double
    var DL: Double
    var L: Double

    T = (jdn - 2451545.0) / 36525.0 // Time in Julian centuries from 2000-01-01 12:00:00 GMT
    T2 = T * T
    dr = PI / 180                                             // degree to radian
    M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2 // mean anomaly, degree
    L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2                  // mean longitude, degree
    DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * sin(dr * M)
    DL = DL + (0.019993 - 0.000101 * T) * sin(dr * 2 * M) + 0.000290 * sin(dr * 3 * M)
    L = L0 + DL // true longitude, degree
    L = L * dr
    L = L - PI * 2 * DOUBLE(INT(L / (PI * 2))) // Normalize to (0, 2*PI)
    return L
}

fun getSunLongitude(jd: Int, timeZoneOffset: Int): Int {
    return INT(sunLongitude(jd - 0.5 - timeZoneOffset / 24.0) / PI * 6)
}


fun getNewMoonDay(k: Int, timeZoneOffset: Int): Int {
    return INT(newMoon(k) + 0.5 + DOUBLE(timeZoneOffset) / 24)
}


fun getLunarMonth11(yyyy: Int, timeZoneOffset: Int): Int {
    var k: Int
    var off: Int
    var nm: Int
    var sunLong: Int

    off = jdFromDate(31, 12, yyyy) - 2415021
    k = INT(DOUBLE(off) / 29.530588853)
    nm = getNewMoonDay(k, timeZoneOffset)
    sunLong = getSunLongitude(nm, timeZoneOffset) // sun longitude at local midnight
    if (sunLong >= 9) {
        nm = getNewMoonDay(k - 1, timeZoneOffset)
    }
    return nm
}

fun getLeapMonthOffset(a11: Int, timeZoneOffset: Int): Int {
    var k: Int
    var last: Int
    var arc: Int
    var i: Int

    k = INT((DOUBLE(a11) - 2415021.076998695) / 29.530588853 + 0.5)
    last = 0
    i = 1 // We start with the month following lunar month 11
    arc = getSunLongitude(getNewMoonDay(k + i, timeZoneOffset), timeZoneOffset)

    while (arc != last && i < 14) {
        last = arc
        i++
        arc = getSunLongitude(getNewMoonDay(k + i, timeZoneOffset), timeZoneOffset)
    }
    return i - 1
}

fun solar2lunar(yyyy: Int, mm: Int, dd: Int, timeZoneOffset: Int): LunarDate {
    var k: Int
    var dayNumber: Int
    var monthStart: Int
    var a11: Int
    var b11: Int
    var lunarDay: Int
    var lunarMonth: Int
    var lunarYear: Int
    var diff: Int
    var leapMonthDiff: Int
    var lunarLeap: Boolean

    dayNumber = jdFromDate(dd, mm, yyyy)

    k = INT((DOUBLE(dayNumber) - 2415021.076998695) / 29.530588853)
    monthStart = getNewMoonDay(k + 1, timeZoneOffset)
    if (monthStart > dayNumber) {
        monthStart = getNewMoonDay(k, timeZoneOffset)
    }
    a11 = getLunarMonth11(yyyy, timeZoneOffset)
    b11 = a11
    if (a11 >= monthStart) {
        lunarYear = yyyy
        a11 = getLunarMonth11(yyyy - 1, timeZoneOffset)
    } else {
        lunarYear = yyyy + 1
        b11 = getLunarMonth11(yyyy + 1, timeZoneOffset)
    }
    lunarDay = dayNumber - monthStart + 1
    diff = INT((monthStart - a11) / 29)
    lunarLeap = false
    lunarMonth = diff + 11
    if (b11 - a11 > 365) {
        leapMonthDiff = getLeapMonthOffset(a11, timeZoneOffset)
        if (diff >= leapMonthDiff) {
            lunarMonth = diff + 10
            if (diff == leapMonthDiff) {
                lunarLeap = true
            }
        }
    }
    if (lunarMonth > 12) {
        lunarMonth = lunarMonth - 12
    }
    if (lunarMonth >= 11 && diff < 4) {
        lunarYear -= 1
    }

    return LunarDate(year = lunarYear, month = lunarMonth, day = lunarDay, leap = lunarLeap)
}


fun lunar2solar(lunarYear: Int, lunarMonth: Int, lunarDay: Int, lunarLeap: Int, timeZoneOffset: Int): SolarDate {
    var k: Int
    var a11: Int
    var b11: Int
    var off: Int
    var leapOff: Int
    var leapMonth: Int
    var monthStart: Int

    if (lunarMonth < 11) {
        a11 = getLunarMonth11(lunarYear - 1, timeZoneOffset)
        b11 = getLunarMonth11(lunarYear, timeZoneOffset)
    } else {
        a11 = getLunarMonth11(lunarYear, timeZoneOffset)
        b11 = getLunarMonth11(lunarYear + 1, timeZoneOffset)
    }
    k = INT(0.5 + (DOUBLE(a11) - 2415021.076998695) / 29.530588853)
    off = lunarMonth - 11
    if (off < 0) {
        off += 12
    }
    if (b11 - a11 > 365) {
        leapOff = getLeapMonthOffset(a11, timeZoneOffset)
        leapMonth = leapOff - 2
        if (leapMonth < 0) {
            leapMonth += 12
        }
        if (lunarLeap != 0 && lunarMonth != lunarLeap) {
            return SolarDate(0, 0, 0)
        } else if (lunarLeap != 0 || off >= leapOff) {
            off += 1
        }
    }
    monthStart = getNewMoonDay(k + off, timeZoneOffset)

    return jdToDate(monthStart + lunarDay - 1)
}