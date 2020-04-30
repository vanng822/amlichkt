package com.amlich

import org.junit.jupiter.api.*
import java.time.Month
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Validate")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class CalendarTest {

    @Test
    fun testCalendarSelectedDate() {
        val d = VNDate.ofUTC(2020, Month.JULY, 31, 12, 0, 0, 0)
        val c = VNCalendar.of(d)
        Assertions.assertEquals(11, c.dayOfMonth)
        Assertions.assertEquals(Month.JUNE, c.month)
        Assertions.assertEquals(2020, c.year)

        Assertions.assertEquals(d.solarDateTime().month, c.solarDateTime.month)
        Assertions.assertEquals(31, c.solarDateTime.dayOfMonth)
        c.nextMonth()
        Assertions.assertEquals(d.solarDateTime().month+1, c.solarDateTime.month)
        Assertions.assertEquals(31, c.solarDateTime.dayOfMonth)
        c.previousMonth()
        Assertions.assertEquals(d.solarDateTime().month, c.solarDateTime.month)
        Assertions.assertEquals(31, c.solarDateTime.dayOfMonth)
    }

    @Test
    fun testCalendarDateStep() {
        val c = VNCalendar.ofUTC(2020, Month.MARCH)
        Assertions.assertEquals(Month.MARCH, c.solarDateTime.month)
        Assertions.assertEquals(1, c.solarDateTime.dayOfMonth)
        c.nextDate()
        Assertions.assertEquals(Month.MARCH, c.solarDateTime.month)
        Assertions.assertEquals(2, c.solarDateTime.dayOfMonth)
        c.previousDate()
        c.previousDate()
        Assertions.assertEquals(Month.FEBRUARY, c.solarDateTime.month)
        Assertions.assertEquals(29, c.solarDateTime.dayOfMonth)
    }
}