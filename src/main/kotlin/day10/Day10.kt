package day10

import java.io.File

object Day10 {

    data class PuzzleInput(
        val width: Int,
        val height: Int,
        val values: IntArray
    ) : Sequence<Pair<Position, Int>> {
        fun get(x: Int, y: Int): Int? {
            if (x !in 0 until width || y !in 0 until height) {
                return null
            }
            return values[y * width + x]
        }

        fun get(p: Position) {
            get(p.x, p.y)
        }

        override fun iterator(): Iterator<Pair<Position, Int>> {
            return iterator {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        yield(Position(x, y) to get(x, y)!!)
                    }
                }
            }
        }

    }

    data class Position(
        val x: Int, val y: Int
    )

    data class State(
        val x: Int,
        val y: Int,
        val currentHeight: Int
    )

    private fun parse(inputString: String): PuzzleInput {
        val values = mutableListOf<Int>()
        inputString.trim().lines().reversed().forEach { l ->
            l.trim().forEach { c ->
                values.add("$c".toInt())
            }
        }
        val height = inputString.lines().size
        val width = values.size / height
        if (values.size % height != 0) {
            throw IllegalArgumentException("Invalid input")
        }
        return PuzzleInput(width, height, values.toIntArray())
    }

    fun solveT1(inputString: String): Long {
        val input = parse(inputString)
        val zeroStates = input
            .filter { (_, v) -> v == 0 }
            .map { (p, v) -> State(p.x, p.y, v) }
            .toList()
        val trailCounts = zeroStates
            .map { state -> trailHeads(input, state) }
            .toList()
        return trailCounts.sumOf { it.size }.toLong()
    }

    private fun trailHeads(input: PuzzleInput, state: State): Set<Position> {
        if (state.currentHeight == 9) {
            return setOf(Position(state.x, state.y))
        }
        var acc = sequenceOf(
            Pair(state.x - 1, state.y),
            Pair(state.x, state.y + 1),
            Pair(state.x + 1, state.y),
            Pair(state.x, state.y - 1),
        )
            .filter { (x, y) -> input.get(x, y) == state.currentHeight + 1 }
            .flatMap { (x, y) ->
                trailHeads(input, State(x, y, state.currentHeight + 1))
            }.toSet()

        return acc;
    }

    private fun countTrails(input: PuzzleInput, state: State): Int {
        if (state.currentHeight == 9) {
            return 1
        }
        var acc = sequenceOf(
            Pair(state.x - 1, state.y),
            Pair(state.x, state.y + 1),
            Pair(state.x + 1, state.y),
            Pair(state.x, state.y - 1),
        )
            .filter { (x, y) -> input.get(x, y) == state.currentHeight + 1 }
            .sumOf { (x, y) ->
                countTrails(input, State(x, y, state.currentHeight + 1))
            }

        return acc;
    }


    fun solveT2(inputString: String): Long {
        val input = parse(inputString)
        return input
            .filter { (_, v) -> v == 0 }
            .map { (p, v) -> State(p.x, p.y, v) }
            .sumOf { state -> countTrails(input, state) }
            .toLong()
    }

}

fun main() {
    val testInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent()
    val t1Expected = 36L
    val t2Expected = 81L

    val puzzleInput: String by lazy {
        val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day10/data"
        File(path).bufferedReader().readText()
    }

    val t1res = Day10.solveT1(testInput)
    if (t1res == t1Expected) {
        println("Example 1 passed")
    } else {
        println("Example 1 failed, was $t1res, should've been $t1Expected")
        return
    }
    println("Task 1: ${Day10.solveT1(puzzleInput)}")

    val t2res = Day10.solveT2(testInput)
    if (t2res == t2Expected) {
        println("Example 2 passed")
    } else {
        println("Example 2 failed, was $t2res, should've been $t2Expected")
        return
    }
    println("Task 2: ${Day10.solveT2(puzzleInput)}")

}