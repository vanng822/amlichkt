package com.amlich

import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class UtilTest {

    @Test
    fun testPadd() {
        for (i in 1..9) {
            var result = padd(i)
            Assertions.assertEquals("0${i}", result)
        }

        for (i in 10..11) {
            var result = padd(i)
            Assertions.assertEquals("${i}", result)
        }
    }
}