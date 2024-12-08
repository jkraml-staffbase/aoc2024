package day05

import java.io.File

data class Rule(
    val earlier: Int,
    val later: Int,
)

typealias PrintJob = List<Int>

fun (PrintJob).satifies(rule: Rule): Boolean {
    if (!this.contains(rule.earlier) || !this.contains(rule.later)) {
        // rule does not apply -> is satisfied by default
        return true
    }

    val earlierIndex = this.indexOf(rule.earlier)
    val laterIndex = this.indexOf(rule.later)
    return earlierIndex < laterIndex
}

data class Assignment(
    val rules: Set<Rule>,
    val printJobs: List<PrintJob>
)

fun parseInput(input: String): Assignment {
    val (rules, printJobs) = input.trim().split("\n\n")
    return Assignment(
        rules = rules.split("\n").filter{ s -> s.isNotBlank() }.map { rule ->
            val (earlier, later) = rule.split("|")
            Rule(
                earlier = earlier.toInt(),
                later = later.toInt()
            )
        }.toSet(),
        printJobs = printJobs.split("\n").filter{ s -> s.isNotBlank() }.map { printJob ->
            printJob.split(",").map { it.toInt() }
        }
    )
}

object Task01 {

    fun solve(inputString: String): Int {
        val assignment = parseInput(inputString)

        val correctJobs = assignment.printJobs.filter { printJob ->
            assignment.rules.all { rule ->
                printJob.satifies(rule)
            }
        }

        val middlePageNumbers = correctJobs.map { printJob ->
            printJob[printJob.size / 2]  // I was lucky this produces the middle index as expected - I didn't both checking the math first
        }

        return middlePageNumbers.sum()
    }
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day05/data"
    val input = File(path).bufferedReader().readText()

    val count = Task01.solve(input)
    println(count)
}