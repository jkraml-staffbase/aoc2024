package day08

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {

    private val input = """
        ......#....#
        ...#....0...
        ....#0....#.
        ..#....0....
        ....0....#..
        .#....A.....
        ...#........
        #......#....
        ........A...
        .........A..
        ..........#.
        ..........#.
    """.trimIndent()
        .replace('#', '.') // remove antinode markers


    @Test
    fun task1Example() {
        val result = Day08.solveT1(input)

        assertEquals(14, result)
    }



    @Test
    fun task2Example() {
        val result = Day08.solveT2(input)

        assertEquals(34, result)
    }

}