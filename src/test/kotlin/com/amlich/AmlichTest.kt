package com.amlich

import org.junit.jupiter.api.*
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
}