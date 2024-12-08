package day02

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class Task01Test {

    @Test
    fun example() {
        val input = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent()
        val reports = parse(input)
        assertTrue(reports[0].isSafe())
        assertFalse(reports[1].isSafe())
        assertFalse(reports[2].isSafe())
        assertFalse(reports[3].isSafe())
        assertFalse(reports[4].isSafe())
        assertTrue(reports[5].isSafe())
    }

}