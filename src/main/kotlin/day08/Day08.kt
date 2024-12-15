package day08

import java.io.File
import kotlin.math.max

typealias Frequency = Char

object Day08 {

    data class Position(
        val x: Int, val y: Int
    ) {
        operator fun plus(vector: Vector): Position {
            return Position(x + vector.dx, y + vector.dy)
        }

        operator fun minus(vector: Vector): Position {
            return Position(x - vector.dx, y - vector.dy)
        }
    }

    data class Vector(
        val dx: Int,
        val dy: Int,

        ) {
        operator fun plus(other: Vector): Vector {
            return Vector(dx + other.dx, dy + other.dy)
        }

        operator fun times(scalar: Int): Vector {
            return Vector(dx * scalar, dy * scalar)
        }

        companion object {
            fun between(a: Position, b: Position): Vector {
                return Vector(b.x - a.x, b.y - a.y)
            }
        }
    }

    data class PuzzleInput(
        val width: Int, val height: Int, val antennas: Map<Frequency, Set<Position>>
    )

    private fun parse(inputString: String): PuzzleInput {
        val lines = inputString.lines()
            .filter { s -> s.isNotBlank() }
            .reversed() // so y=0 is at the bottom
        val height = lines.size
        var width = 0
        val antennas = mutableMapOf<Frequency, MutableSet<Position>>()
        lines.forEachIndexed { y, l ->
            width = max(width, l.length)
            l.forEachIndexed { x, c ->
                if (c == '.') return@forEachIndexed
                val p = Position(x, y)
                antennas.computeIfAbsent(c) { mutableSetOf() }.add(p)
            }
        }
        return PuzzleInput(width, height, antennas)
    }

    private fun <T> allPairs(elements: Collection<T>): Sequence<Pair<T, T>> {
        val elementsAsList = elements.toList()
        return sequence {
            for (i in elements.indices) {
                for (j in i + 1 until elements.size) {
                    yield(elementsAsList[i] to elementsAsList[j])
                }
            }
        }
    }

    fun solveT1(inputString: String): Long {
        val puzzle = parse(inputString)

        val antinodes = mutableSetOf<Position>()
        puzzle.antennas.forEach { f, poss ->
            val pairs = allPairs(poss)
            pairs.forEach { (a, b) ->
                val v = Vector.between(a, b)
                val candidates = listOf(b + v, a - v) // the antinodes on either side
                candidates.forEach { c ->
                    if (isWithinGrid(c, puzzle)) {
                        antinodes.add(c)
                    }
                }
            }
        }

        return antinodes.size.toLong()
    }

    fun solveT2(inputString: String): Long {
        val puzzle = parse(inputString)

        val antinodes = mutableSetOf<Position>()
        puzzle.antennas.forEach { f, poss ->
            val pairs = allPairs(poss)
            pairs.forEach { (a, b) ->
                val v = Vector.between(a, b)

                // iterate in one direction
                var candidate = a
                while (isWithinGrid(candidate, puzzle)) {
                    antinodes.add(candidate)
                    candidate -= v
                }

                // iterate in the other direction
                candidate = b
                while (isWithinGrid(candidate, puzzle)) {
                    antinodes.add(candidate)
                    candidate += v
                }
            }
        }

        return antinodes.size.toLong()

    }

    private fun isWithinGrid(c: Position, puzzle: PuzzleInput): Boolean =
        c.x in 0 until puzzle.width && c.y in 0 until puzzle.height
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day08/data"
    val input = File(path).bufferedReader().readText()
    println("Task 1: ${Day08.solveT1(input)}")
    println("Task 2: ${Day08.solveT2(input)}")
}