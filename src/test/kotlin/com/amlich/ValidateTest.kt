package com.amlich

import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Validate")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ValidateTest {

    @Test
    fun testIsValid() {
        // leap month
        Assertions.assertEquals(true, isValid(LunarDate(2025, 6, 30, false)))
        Assertions.assertEquals(true, isValid(LunarDate(2025, 6, 20, true)))
        Assertions.assertEquals(false, isValid(LunarDate(2025, 6, 30, true)))
        Assertions.assertEquals(true, isValid(LunarDate(2025, 6, 29, true)))
    }
}