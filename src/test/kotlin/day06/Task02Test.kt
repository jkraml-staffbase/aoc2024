package day06

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class Task02Test {

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

        val result = Task02.solve(input)
        assertEquals(6, result)
    }

}