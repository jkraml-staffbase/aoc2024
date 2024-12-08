package day04

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class Task01Test {

    @Test
    fun example() {
        val input = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent()

        val result = Task01.count(input)

        assertEquals(18, result)
    }

}