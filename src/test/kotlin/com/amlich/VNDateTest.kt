package com.amlich

import org.junit.jupiter.api.*
import java.time.*
import java.time.format.DateTimeFormatter

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class VNDateTest {

    @Test
    fun invalidDate() {
        Assertions.assertThrows(DateTimeException::class.java, fun() {
            VNDate(LunarDate(2025, 6, 30, true), 7)
        })
    }

    @Test
    fun testToString() {
        val d = VNDate(LunarDate(2014, 8, 23, false), 7)
        val actual = d.toString()
        val expected = "2014-08-23"
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun date() {
        val d = VNDate(LunarDate(2014, 8, 23, false), 7)
        val actual = d.date()
        val expected = Triple<Int, Month, Int>(2014, Month.of(8), 23)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun defaultContructor() {
        val expected = ZonedDateTime.now(VNTimeZone)
        val d = VNDate()
        val actual = d.solarDateTime()
        Assertions.assertEquals(expected.dayOfMonth, actual.dayOfMonth)
        Assertions.assertEquals(expected.month, actual.month)
        Assertions.assertEquals(expected.year, actual.year)
    }

    @Test
    fun lunardate() {
        val expected = LunarDate(2014, 8, 23, false)
        val d = VNDate(expected, 7)
        val actual = d.lunarDate()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun solarTime() {
        val expected = ZonedDateTime.now(VNTimeZone)
        val d = VNDate(expected, 7)
        val actual = d.solarDateTime()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromSolarTime() {
        val solarDate = LocalDateTime.parse("2014-09-16 03:04", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val d = VNDate(solarDate.atZone(VNTimeZone), 7)
        Assertions.assertEquals(23, d.dayOfMonth)
        Assertions.assertEquals(Month.AUGUST, d.month)
        Assertions.assertEquals(2014, d.year)
    }

    @Test
    fun fromLunarTime() {
        val lunarDate = LunarDate(2014, 8, 23, false)
        val d = VNDate(lunarDate, 7).solarDateTime()
        Assertions.assertEquals(16, d.dayOfMonth)
        Assertions.assertEquals(Month.SEPTEMBER, d.month)
        Assertions.assertEquals(2014, d.year)
    }

    @Test
    fun timeZoneUTC() {
        // UTC, hour 16 and 17 will shift the date
        var d = VNDate.ofUTC(2017, Month.MAY, 21, 16, 59, 59, 0)
        Assertions.assertEquals(26, d.dayOfMonth)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)

        d = VNDate.ofUTC(2017, Month.MAY, 21, 17, 0, 1, 0)
        Assertions.assertEquals(27, d.dayOfMonth)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)
    }

    @Test
    fun ofVNTimezone() {
        // vietnamese timezone then expected the same date
        var d = VNDate.of(2017, Month.MAY, 21, 16, 59, 59, 0)
        Assertions.assertEquals(26, d.dayOfMonth)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)
        d = VNDate.of(2017, Month.MAY, 21, 17, 0, 1, 0)
        Assertions.assertEquals(26, d.dayOfMonth)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)
    }

    @Test
    fun ofLocal() {
        // don't know if this prove something
        val expected = LocalDateTime.now()
        var d = VNDate.ofLocal(expected.year, expected.month, expected.dayOfMonth, expected.hour, expected.minute, expected.second, expected.nano, ZoneId.systemDefault())
        var actual = d.solarDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun ofLocalDateTime() {
        val expected = LocalDateTime.now()
        val d = VNDate.of(expected)
        val actual = d.solarDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun isEqual() {
        val now = LocalDateTime.now()
        val d = VNDate.of(now)
        Assertions.assertEquals(true, d.isEqual(d))
        val now2 = now.minusHours(1)
        val d2 = VNDate.of(now2)
        Assertions.assertEquals(false, d.isEqual(d2))
    }

    @Test
    fun isBefore() {
        val now = LocalDateTime.now()
        val d = VNDate.of(now)
        Assertions.assertEquals(false, d.isBefore(d))
        val now2 = now.minusHours(1)
        val d2 = VNDate.of(now2)
        Assertions.assertEquals(false, d.isBefore(d2))

        val now3 = now.plusHours(1)
        val d3 = VNDate.of(now3)
        Assertions.assertEquals(true, d.isBefore(d3))
    }

    @Test
    fun isAfter() {
        val now = LocalDateTime.now()
        val d = VNDate.of(now)
        Assertions.assertEquals(false, d.isAfter(d))
        val now2 = now.minusHours(1)
        val d2 = VNDate.of(now2)
        Assertions.assertEquals(true, d.isAfter(d2))

        val now3 = now.plusHours(1)
        val d3 = VNDate.of(now3)
        Assertions.assertEquals(false, d.isAfter(d3))
    }

    @Test
    fun comparisonOperators() {
        val now = LocalDateTime.now()
        val d = VNDate.of(now)
        val now2 = now.minusHours(1)
        val before = VNDate.of(now2)
        val now3 = now.plusHours(1)
        val after = VNDate.of(now3)

        Assertions.assertEquals(true, d == d)
        Assertions.assertEquals(true, d >= d)

        Assertions.assertEquals(false, d > after)
        Assertions.assertEquals(false, d < before)
        Assertions.assertEquals(true, after > before)
        Assertions.assertEquals(true, after >= before)
    }

    @Test
    fun addDate() {
        val lunarDate = LunarDate(2014, 8, 23, false)
        val vnDate = VNDate(lunarDate, TimeZoneOffset)
        val actual = vnDate.addDate(0, 1, 2)
        Assertions.assertEquals(25, actual.dayOfMonth)
        Assertions.assertEquals(Month.SEPTEMBER, actual.month)
        Assertions.assertEquals(2014, actual.year)
    }

    @Test
    fun plus() {
        val lunarDate = LunarDate(2014, 8, 23, false)
        val vnDate = VNDate(lunarDate, TimeZoneOffset)
        var actual = vnDate.plus(period = Period.of(0, 0, 3))
        Assertions.assertEquals(26, actual.dayOfMonth)
        Assertions.assertEquals(Month.AUGUST, actual.month)
        Assertions.assertEquals(2014, actual.year)

        // Operator
        actual = vnDate + Period.of(0, 0, 3)
        Assertions.assertEquals(26, actual.dayOfMonth)
        Assertions.assertEquals(Month.AUGUST, actual.month)
        Assertions.assertEquals(2014, actual.year)
    }

    @Test
    fun minus() {
        val lunarDate = LunarDate(2014, 8, 23, false)
        val vnDate = VNDate(lunarDate, TimeZoneOffset)
        var actual = vnDate.minus(period = Period.of(0, 0, 3))
        Assertions.assertEquals(20, actual.dayOfMonth)
        Assertions.assertEquals(Month.AUGUST, actual.month)
        Assertions.assertEquals(2014, actual.year)

        // operator
        actual = vnDate - Period.of(0, 0, 3)
        Assertions.assertEquals(20, actual.dayOfMonth)
        Assertions.assertEquals(Month.AUGUST, actual.month)
        Assertions.assertEquals(2014, actual.year)
    }
}