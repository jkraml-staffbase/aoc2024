package day11

import java.io.File
import java.util.regex.Pattern

typealias PuzzleState = List<Long>


object Day11 {

    private fun parse(inputString: String): PuzzleState {
        return inputString.trim().split(Pattern.compile(" +")).map(String::toLong)
    }

    data class CacheKey(val e: Long, val iterations: Int)

    private val betterCache = mutableMapOf<CacheKey, Long>()

    private fun betterApplyRules(e: Long, iterations: Int): Long {
        val cacheKey = CacheKey(e, iterations)
        if (betterCache.containsKey(cacheKey)) {
            return betterCache[cacheKey]!!
        }
        val evaluated = applyRules(e)
        val ret: Long
        if (iterations == 1) {
            ret = evaluated.size.toLong()
        } else {
            ret = evaluated.sumOf { e -> betterApplyRules(e, iterations - 1) }
        }
        betterCache[cacheKey] = ret
        return ret
    }

    private fun applyRules(e: Long): PuzzleState {
        if (e == 0L) {
            return listOf(1L)
        }
        val s = "$e"
        if (s.length % 2 == 0) {
            val leftHalf = s.substring(0, s.length / 2)
            val rightHalf = s.substring(s.length / 2)
            return listOf(leftHalf.toLong(), rightHalf.toLong())
        }

        return listOf(e * 2024)
    }

    fun solveT1(inputString: String): Long {
        var state = parse(inputString)
        return solvePerPartes(state, 25)
    }

    private fun solvePerPartes(state: PuzzleState, iterations: Int): Long {
        return state.sumOf { p ->
            betterApplyRules(p, iterations)
        }
    }

    fun solveT2(inputString: String): Long {
        var state = parse(inputString)
        return solvePerPartes(state, 75)
    }

}

fun main() {
    val testInput = """
        125 17
    """.trimIndent()
    val t1Expected = 55312L
//    val t2Expected = 42L

    val puzzleInput: String by lazy {
        val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day11/data"
        File(path).bufferedReader().readText()
    }

    val t1res = Day11.solveT1(testInput)
    if (t1res == t1Expected) {
        println("Example 1 passed")
    } else {
        println("Example 1 failed, was $t1res, should've been $t1Expected")
        return
    }
    println("Task 1: ${Day11.solveT1(puzzleInput)}")

//    val t2res = Day11.solveT2(testInput)
//    if (t2res == t2Expected) {
//        println("Example 2 passed")
//    } else {
//        println("Example 2 failed, was $t2res, should've been $t2Expected")
//        return
//    }
    println("Task 2: ${Day11.solveT2(puzzleInput)}")

}