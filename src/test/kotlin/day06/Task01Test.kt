package day06

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class Task01Test {

    @Test
    fun example() {
        val input = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
        """.trimIndent()

        val result = Task01.solve(input)
        assertEquals(41, result)
    }

}