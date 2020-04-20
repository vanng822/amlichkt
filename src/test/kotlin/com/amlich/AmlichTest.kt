package com.amlich

import org.junit.jupiter.api.*
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AmlichTest {
    @Test
    fun testToday() {
        val expected = ZonedDateTime.now(ZoneId.of(VNTimeZoneName))
        val actual = today().solarDateTime()
        // unlucky if execute over the midnight
        Assertions.assertEquals(expected.dayOfMonth, actual.dayOfMonth)
        Assertions.assertEquals(expected.month, actual.month)
        Assertions.assertEquals(expected.year, actual.year)
    }

    @Test
    fun getMonthDates() {
        Assertions.assertEquals( 31, getMonthDates(2016, Month.JULY).count())
        // Leap
        Assertions.assertEquals(29, getMonthDates(2016, Month.FEBRUARY).count())
        Assertions.assertEquals(28, getMonthDates(2017, Month.FEBRUARY).count())
        val dates = getMonthDates(2016, Month.NOVEMBER)
        Assertions.assertEquals(30, dates.count())
        // first day
        Assertions.assertEquals(2, dates[0].day)
        Assertions.assertEquals(Month.OCTOBER, dates[0].month)
        Assertions.assertEquals(2016, dates[0].year)
        // last day
        Assertions.assertEquals(2, dates[29].day)
        Assertions.assertEquals(Month.NOVEMBER, dates[29].month)
        Assertions.assertEquals(2016, dates[29].year)
    }

    @Test
    fun getYearMonthDates2020() {
        val monthDates = getYearMonthDates(2020)
        // expected 12 months
        Assertions.assertEquals( 12, monthDates.count())

        // Number of dates in each month
        val expectedNumberDays = mapOf<Month,Int>(
            Month.JANUARY to 31,
            Month.FEBRUARY to 29,
            Month.MARCH to 31,
            Month.APRIL to 30,
            Month.MAY to 31,
            Month.JUNE to 30,
            Month.JULY to 31,
            Month.AUGUST to 31,
            Month.SEPTEMBER to 30,
            Month.OCTOBER to 31,
            Month.NOVEMBER to 30,
            Month.DECEMBER to 31
        )

        for ((month, expected) in expectedNumberDays) {
            val actual:List<VNDate> = monthDates[month]!!
            Assertions.assertEquals(expected, actual.count())
        }
    }

    @Test
    fun getYearMonthDates2021() {
        val monthDates = getYearMonthDates(2021)
        // expected 12 months
        Assertions.assertEquals( 12, monthDates.count())

        // Number of dates in each month
        val expectedNumberDays = mapOf<Month,Int>(
            Month.JANUARY to 31,
            Month.FEBRUARY to 28,
            Month.MARCH to 31,
            Month.APRIL to 30,
            Month.MAY to 31,
            Month.JUNE to 30,
            Month.JULY to 31,
            Month.AUGUST to 31,
            Month.SEPTEMBER to 30,
            Month.OCTOBER to 31,
            Month.NOVEMBER to 30,
            Month.DECEMBER to 31
        )

        for ((month, expected) in expectedNumberDays) {
            val actual:List<VNDate> = monthDates[month]!!
            Assertions.assertEquals(expected, actual.count())
        }
    }

    @Test
    fun setTimeZoneOffset() {
        Assertions.assertEquals(7, TimeZoneOffset)
        TimeZoneOffset = 6
        Assertions.assertEquals(6, TimeZoneOffset)
    }
}