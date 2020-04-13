package com.amlich

import org.junit.jupiter.api.*
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AmlichTest {
    @Test
    fun testToday() {
        val expected = LocalDateTime.now()
        val actual = today().solarTime()
        // unlucky if execute over the midnight
        Assertions.assertEquals(expected.dayOfMonth, actual.dayOfMonth)
        Assertions.assertEquals(expected.month, actual.month)
        Assertions.assertEquals(expected.year, actual.year)
    }
}