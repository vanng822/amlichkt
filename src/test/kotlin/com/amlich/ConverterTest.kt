package com.amlich

import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ConverterTest {

    @Test
    fun solar2lunar() {
        val result = solar2lunar(2014, 9, 23, 7)
        Assertions.assertEquals(30, result.day)
        Assertions.assertEquals(8, result.month)
        Assertions.assertEquals(2014, result.year)
        Assertions.assertEquals(false,result.leap)
    }

    @Test
    fun lunar2solar() {
        val result = lunar2solar(2014, 8, 30, false, 7)
        Assertions.assertEquals(23, result.day)
        Assertions.assertEquals(9, result.month)
        Assertions.assertEquals(2014, result.year)
    }

    @Test
    fun solar2lunar20250724() {
        // this failed in pg-lunardate; the key is timeZoneOffset=7
        val result = solar2lunar(2025, 7, 24, 7)
        Assertions.assertEquals(30, result.day)
        Assertions.assertEquals(6, result.month)
        Assertions.assertEquals(2025, result.year)
        Assertions.assertEquals(false,result.leap)

        val result2 = solar2lunar(2025, 8, 20, 7)
        Assertions.assertEquals(27, result2.day)
        Assertions.assertEquals(6, result2.month)
        Assertions.assertEquals(2025, result2.year)
        Assertions.assertEquals(true,result2.leap)
    }

    @Test
    fun solar2LunarLeapMonth() {
        var lunarDate = solar2lunar(2006, 9, 12, 7)
        Assertions.assertEquals(20, lunarDate.day)
        Assertions.assertEquals(7, lunarDate.month)
        Assertions.assertEquals(2006, lunarDate.year)
        Assertions.assertEquals(true, lunarDate.leap)

        lunarDate = solar2lunar(2012, 6, 12, 7)
        Assertions.assertEquals(23, lunarDate.day)
        Assertions.assertEquals(4, lunarDate.month)
        Assertions.assertEquals(2012, lunarDate.year)
        Assertions.assertEquals(true, lunarDate.leap)
    }

    @Test
    fun lunar2SolarLeapMonth() {
        var solarDate = lunar2solar(2006, 7, 20, true, 7)
        Assertions.assertEquals(12, solarDate.day)
        Assertions.assertEquals(9, solarDate.month)
        Assertions.assertEquals(2006, solarDate.year)

        solarDate = lunar2solar(2012, 4, 23, true, 7)
        Assertions.assertEquals(12, solarDate.day)
        Assertions.assertEquals(6, solarDate.month)
        Assertions.assertEquals(2012, solarDate.year)
    }
}