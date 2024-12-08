package day03

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class Task02Test {

    @Test
    fun example() {
        val input = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
        val expected = 48

        val muls = parseWithConditionals(input)
        val sum = muls.sumOf(Mul::exec)

        assertEquals(expected, sum)
    }

}