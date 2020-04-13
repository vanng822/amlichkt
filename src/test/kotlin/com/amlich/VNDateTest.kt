package com.amlich

import org.junit.jupiter.api.*
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class VNDateTest {

    @Test
    fun testToString() {
        val d = VNDate(LunarDate(2014,8, 23, false), 7)
        val actual = d.toString()
        val expected = "2014-08-23"
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun date() {
        val d = VNDate(LunarDate(2014,8, 23, false), 7)
        val actual = d.date()
        val expected = Triple<Int, Int, Int>(2014,8,23)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun lunardate() {
        val expected = LunarDate(2014,8, 23, false)
        val d = VNDate(expected, 7)
        val actual = d.lunarDate()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun solarTime() {
        val expected = LocalDateTime.now()
        val d = VNDate(expected, 7)
        val actual = d.solarTime()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromSolarTime() {
        val solarDate = LocalDateTime.parse("2014-09-16 03:04", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val d = VNDate(solarDate, 7)
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
}