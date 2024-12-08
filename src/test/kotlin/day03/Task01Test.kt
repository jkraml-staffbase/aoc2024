package day03

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Task01Test {

    @Test
    fun example() {
        val input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

        val muls = parse(input)
        val sum = muls.sumOf(Mul::exec)

        assertEquals(161, sum)
    }

}