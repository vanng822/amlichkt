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
        val actual = today().solarTime()
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
    fun getYearMonthDates() {
        Assertions.assertEquals( 12, getYearMonthDates(2016).count())
    }
}