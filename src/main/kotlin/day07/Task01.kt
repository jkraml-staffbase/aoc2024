package day07

import java.io.File

typealias Input = Map<Long, List<Long>>

fun parse(inputString: String): Input = inputString.lines().filter{s -> s.isNotBlank()}.mapNotNull { line ->
        val parts = line.split(":", " ").filter(String::isNotBlank)
        if (parts.size < 3) {
            println("Could not parse line: $line")
            return@mapNotNull null
        }
        val testVal = parts[0].toLong()
        val operands = parts.subList(1, parts.size).map(String::toLong)
        testVal to operands
    }.toMap()

object Task01 {

    fun canSolve(testVal: Long, operands: List<Long>): Boolean {
        return recursiveSolveCall(testVal, 0, operands)
    }

    fun recursiveSolveCall(testVal: Long, accumulator: Long, remainingOperands: List<Long>): Boolean {
        if (remainingOperands.isEmpty()) {
            return testVal == accumulator
        }
        if (accumulator > testVal) {
            return false
        }

        val next = remainingOperands.first()
        val rest = remainingOperands.subList(1, remainingOperands.size)

        return recursiveSolveCall(testVal, accumulator + next, rest)
                || recursiveSolveCall(testVal, accumulator * next, rest)
    }

    fun solve(inputString: String): Long {
        val input = parse(inputString)
        return input.filter{ e -> canSolve(e.key, e.value)}
            .keys
            .sum()
    }
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day07/data"
    val input = File(path).bufferedReader().readText()

    val count = Task01.solve(input)
    println(count)
}