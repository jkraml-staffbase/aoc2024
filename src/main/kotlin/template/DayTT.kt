package template

import day09.Day09
import java.io.File

object DayTT {

    data class PuzzleInput(
        val something: Int
    )

    private fun parse(inputString: String): PuzzleInput {
        TODO()
    }

    fun solveT1(inputString: String): Long {
        val input = parse(inputString)
        TODO()
    }

    fun solveT2(inputString: String): Long {
        val input = parse(inputString)
        TODO()
    }

}

fun main() {
    val testInput = """
        blah
    """.trimIndent()
    val t1Expected = 42L
    val t2Expected = 42L

    val puzzleInput: String by lazy {
        val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/dayTT/data"
        File(path).bufferedReader().readText()
    }

    val t1res = DayTT.solveT1(testInput)
    if (t1res == t1Expected) {
        println("Example 1 passed")
    } else {
        println("Example 1 failed, was $t1res, should've been $t1Expected")
        return
    }
    println("Task 1: ${DayTT.solveT1(puzzleInput)}")

    val t2res = DayTT.solveT2(testInput)
    if (t2res == t2Expected) {
        println("Example 2 passed")
    } else {
        println("Example 2 failed, was $t2res, should've been $t2Expected")
        return
    }
    println("Task 2: ${DayTT.solveT2(puzzleInput)}")

}