package day05

import java.io.File
import kotlin.math.min

object Task02 {

    fun solve(inputString: String): Int {
        val assignment = parseInput(inputString)

        val incorrectJobs = assignment.printJobs.filter { printJob ->
            assignment.rules.any { rule ->
                !printJob.satifies(rule)
            }
        }

        println("Incorrect jobs: ${incorrectJobs.size}")
        val fixedJobs = incorrectJobs.map({ printJob ->
            print(".")
            fix(printJob, assignment.rules)
        })
        println()

        val middlePageNumbers = fixedJobs.map { printJob ->
            printJob[printJob.size / 2]
        }

        return middlePageNumbers.sum()
    }


// FIRST ATTEMPT: just do all permutations and find first good one. Unfortunately on of the printJobs has 23 pages, and 23! is a _very_ large number :-D
//
//    fun fix(printJob: PrintJob, rules: Set<Rule>): PrintJob {
//        val candidates = permutations(printJob)
//
//        return candidates.first { candidates ->
//            rules.all { rule ->
//                candidates.satifies(rule)
//            }
//        }
//    }
//
//    fun <T> permutations(l: List<T>): Sequence<List<T>> { // not sure if this lazy-evals or first creates all the permutations (that would be wasteful)
//        if (l.size == 1) {
//            return sequenceOf(l)
//        }
//
//        return l.asSequence().flatMap { e ->
//            val rest = l-e // here we assume there are no duplicates in the list
//            permutations(rest).map { listOf(e) + it }
//        }
//    }

    class RuleIndex(rules: Set<Rule>) {
        private val rulesByLowerAndThenByOther: Map<Int, Map<Int, Rule>> = rules
            .groupBy { min(it.earlier, it.later) }
            .mapValues {
                val lower = it.key
                val ret = mutableMapOf<Int, Rule>()
                it.value.forEach { rule ->
                    val other = if (rule.earlier == lower) rule.later else rule.earlier
                    ret[other] = rule
                }
                ret
            }

        operator fun get(a: Int, b: Int): Rule? {
            val lower = min(a, b)
            val other = if (a == lower) b else a
            return rulesByLowerAndThenByOther[lower]?.get(other)
        }
    }

    fun fix(printJob: PrintJob, rules: Set<Rule>): PrintJob {
        val idx = RuleIndex(rules)

        val ruleBasedComparator = object : Comparator<Int> {
            override fun compare(a: Int, b: Int): Int {
                return idx[a, b]?.let { if (it.earlier == a) -1 else 1 } ?: 0
            }
        }

        return printJob.sortedWith(ruleBasedComparator)
    }
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day05/data"
    val input = File(path).bufferedReader().readText()

    val count = Task02.solve(input)
    println(count)
}