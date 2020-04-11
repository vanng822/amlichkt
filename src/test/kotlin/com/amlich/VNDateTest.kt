package com.amlich

import org.junit.jupiter.api.*
import java.time.LocalTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class VNDateTest {

    @Test
    fun testToString() {
        val d = VNDate(LocalTime.now(), LunarDate(2014,8, 23, false), 7)
        val actual = d.toString()
        val expected = "2014-08-23"
        Assertions.assertEquals(expected, actual)
    }
}