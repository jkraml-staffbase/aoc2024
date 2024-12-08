package day02

import java.io.File

fun (Report).isSafeIfDampened(): Boolean {
    if (isSafe()) {
        return true
    }

    for (i in 0 until levels.size) {
        val variant = levels.toMutableList()
        variant.removeAt(i)
        if (Report(variant).isSafe()) {
            return true
        }
    }
    return false
}


fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day02/task01.data"
    val input = File(path).bufferedReader().readText()
    val reports = parse(input)
    println(reports.count {r -> r.isSafeIfDampened()})
}