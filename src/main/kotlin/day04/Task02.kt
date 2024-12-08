package day04

import java.io.File

object Task02 {
    fun count(inputString: String): Int {
        val input = parse(inputString)

        var acc = 0
        for (y in 0 until input.height()) {
            for (x in 0 until input.width()) {
                val position = Position(x, y)
                if (XmasKernel.matches(input, position)) {
                    acc++
                }
            }
        }

        return acc
    }

    object XmasKernel{

        private val topLeft = Slope(-1, -1)
        private val topRight = Slope(1, -1)
        private val bottomLeft = Slope(-1, 1)
        private val bottomRight = Slope(1, 1)

        fun matches(input: Input, position: Position): Boolean {
            if (position.x < 1 || // too far left
                position.y < 1 || // too far up
                position.x > input.width() - 2 || // too far right
                position.y > input.height() - 2) { // too far down
                return false
            }

            if (input.charAt(position) != 'A') { // center must be an 'A'
                return false
            }

            val diagonal1 = setOf<Char>(
                input.charAt(position.move(topLeft)),
                input.charAt(position.move(bottomRight))
            )
            if (diagonal1 != setOf('M', 'S')) {
                return false
            }

            val diagonal2 = setOf<Char>(
                input.charAt(position.move(topRight)),
                input.charAt(position.move(bottomLeft))
            )
            if (diagonal2 != setOf('M', 'S')) {
                return false
            }

            return true
        }
    }
}


fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day04/data"
    val input = File(path).bufferedReader().readText()

    val count = Task02.count(input)
    println(count)
}