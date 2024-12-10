package day07

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class Day07Test {

    val input = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent()



    @Test
    fun t01() {
        val result = Task01.solve(input)

        assertEquals(3749, result)
    }

    @Test
    fun t02() {
        val result = Task02.solve(input)

        assertEquals(11387, result)
    }
}