package day04

import kotlin.test.Test
import kotlin.test.assertEquals

class Task02Test {
    @Test
    fun example() {
        val input = """
            .M.S......
            ..A..MSMS.
            .M.S.MAA..
            ..A.ASMSM.
            .M.S.M....
            ..........
            S.S.S.S.S.
            .A.A.A.A..
            M.M.M.M.M.
            ..........
        """.trimIndent()

        assertEquals(9, Task02.count(input))
    }

}