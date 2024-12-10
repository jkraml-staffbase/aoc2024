package day07

import java.io.File

object Task02 {

    fun canSolve(testVal: Long, operands: List<Long>): Boolean {
        val next = operands.first()
        val rest = operands.subList(1, operands.size)
        return recursiveSolveCall(testVal, next, rest) // this was the problem, we can't start with zero
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
                || recursiveSolveCall(testVal, decimalConcat(accumulator, next), rest)
    }

    fun decimalConcat(a: Long, b: Long): Long = (a.toString() + b.toString()).toLong()

    fun solve(inputString: String): Long {
        val input = parse(inputString)
        return input.filter { e -> canSolve(e.key, e.value) }
            .keys
            .sum()
    }
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day07/data"
    val input = File(path).bufferedReader().readText()

    val count = Task02.solve(input)
    // got 340362531521793, but it is too high,
    // 3598800864292 is too low (accidentally pasted t01 answer)
    // too high -> we consider too many equations solvable - but the test is green
    // solved! starting with an accumulator of 0 was the issue
    println(count)
}