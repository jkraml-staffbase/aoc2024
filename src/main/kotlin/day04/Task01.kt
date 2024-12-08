package day04

import java.io.File
import java.util.regex.Pattern
import kotlin.collections.flatMap

typealias Input = Array<CharArray>

fun (Input).charAt(position: Position): Char {
    return this[position.y][position.x]
}

fun (Input).width(): Int {
    return this[0].size //assuming all lines have the same length
}

fun (Input).height(): Int {
    return this.size
}

fun (Input).isInBounds(position: Position): Boolean {
    return position.x >= 0 && position.x < this.width() && position.y >= 0 && position.y < this.height()
}

fun parse(input: String): Input {
    return input.trim().split("\n").filter { s -> s.isNotBlank() }.map { line ->
        line.toCharArray()
    }.toTypedArray()
}


data class Slope(val right: Int, val down: Int) {
    operator fun plus(other: Slope): Slope {
        return Slope(right + other.right, down + other.down)
    }
}

data class Position(val x: Int, val y: Int) {
    fun move(slope: Slope): Position {
        return Position(x + slope.right, y + slope.down)
    }
}

enum class Border {
    LEFT, RIGHT, TOP, BOTTOM,
}

fun getBorderPositions(i: Input, b: Border): Set<Position> {
    return when (b) {
        Border.LEFT ->
            (0 until i.height()).map { y -> Position(0, y) }

        Border.RIGHT ->
            (0 until i.height()).map { y -> Position(i.width() - 1, y) }

        Border.TOP ->
            (0 until i.width()).map { x -> Position(x, 0) }

        Border.BOTTOM ->
            (0 until i.width()).map { x -> Position(x, i.height() - 1) }
    }.toSet()
}

object Task01 {

    val xmasPattern = Pattern.compile("XMAS")

    fun count(input: String): Int {
        val input = parse(input)

        val stringsInDirection: List<String> =
            startPositionsAndWalkDirections(input).flatMap { e: Pair<Set<Position>, Slope> ->
                val (startPositions, direction) = e
                return@flatMap startPositions.map { p -> walk(input, p, direction) }
            }

        return stringsInDirection.sumOf { s -> countOccurrences(s, xmasPattern) }
    }

    private fun countOccurrences(s: String, pattern: Pattern): Int {
        val matcher = pattern.matcher(s)
        var count = 0
        while (matcher.find()) {
            count++
        }
        return count
    }

    private fun startPositionsAndWalkDirections(input: Input): List<Pair<Set<Position>, Slope>> {
        val topBorder = getBorderPositions(input, Border.TOP)
        val bottomBorder = getBorderPositions(input, Border.BOTTOM)
        val leftBorder = getBorderPositions(input, Border.LEFT)
        val rightBorder = getBorderPositions(input, Border.RIGHT)

        val downDirection = Slope(0, 1)
        val upDirection = Slope(0, -1)
        val leftDirection = Slope(-1, 0)
        val rightDirection = Slope(1, 0)

        return listOf(
            Pair(topBorder, downDirection),
            Pair(bottomBorder, upDirection),
            Pair(leftBorder, rightDirection),
            Pair(rightBorder, leftDirection),
            Pair(topBorder + leftBorder, downDirection + rightDirection),
            Pair(topBorder + rightBorder, downDirection + leftDirection),
            Pair(bottomBorder + rightBorder, upDirection + leftDirection),
            Pair(bottomBorder + leftBorder, upDirection + rightDirection),
        )
    }

    private fun walk(input: Input, start: Position, slope: Slope): String {
        val ret = StringBuilder()
        var current = start
        while (input.isInBounds(current)) {
            ret.append(input.charAt(current))
            current = current.move(slope)
        }
        return ret.toString()
    }
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day04/data"
    val input = File(path).bufferedReader().readText()

    val count = Task01.count(input)
    println(count)
}
