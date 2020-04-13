package com.amlich

import org.assertj.core.api.Assert
import org.junit.jupiter.api.*
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class VNDateTest {

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
        val expected = Triple<Int, Int, Int>(2014, 8, 23)
        Assertions.assertEquals(expected, actual)
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
        val actual = d.solarTime()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromSolarTime() {
        val solarDate = LocalDateTime.parse("2014-09-16 03:04", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val d = VNDate(solarDate.atZone(VNTimeZone), 7)
        Assertions.assertEquals(23, d.day)
        Assertions.assertEquals(Month.AUGUST, d.month)
        Assertions.assertEquals(2014, d.year)
    }

    @Test
    fun fromLunarTime() {
        val lunarDate = LunarDate(2014, 8, 23, false)
        val d = VNDate(lunarDate, 7).solarTime()
        Assertions.assertEquals(16, d.dayOfMonth)
        Assertions.assertEquals(Month.SEPTEMBER, d.month)
        Assertions.assertEquals(2014, d.year)
    }

    @Test
    fun timeZoneUTC() {
        // UTC, hour 16 and 17 will shift the date
        var d = VNDate.ofUTC(2017, Month.MAY, 21, 16, 59, 59, 0)
        Assertions.assertEquals(26, d.day)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)

        d = VNDate.ofUTC(2017, Month.MAY, 21, 17, 0, 1, 0)
        Assertions.assertEquals(27, d.day)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)
    }

    @Test
    fun ofVNTimezone() {
        // vietnamese timezone then expected the same date
        var d = VNDate.of(2017, Month.MAY, 21, 16, 59, 59, 0)
        Assertions.assertEquals(26, d.day)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)
        d = VNDate.of(2017, Month.MAY, 21, 17, 0, 1, 0)
        Assertions.assertEquals(26, d.day)
        Assertions.assertEquals(Month.APRIL, d.month)
        Assertions.assertEquals(2017, d.year)
    }

    @Test
    fun ofLocal() {
        // don't know if this prove something
        val expected = LocalDateTime.now()
        var d = VNDate.ofLocal(expected.year, expected.month, expected.dayOfMonth, expected.hour, expected.minute, expected.second, expected.nano, ZoneId.systemDefault())
        var actual = d.solarTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun ofLocalDateTime() {
        val expected = LocalDateTime.now()
        val d = VNDate.of(expected)
        val actual = d.solarTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
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
}