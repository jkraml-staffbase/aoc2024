package day02

import java.io.File

typealias Level = Int

data class Report(
    val levels: List<Level>
) {
    fun isSafe(): Boolean {
        if (levels.size < 2) {
            return true
        }
        val first = levels[0]
        val rest = levels.subList(1, levels.size)

        val second = levels[1]
        val ascending = first < second

        var current = first
        rest.forEach { next ->
            if (ascending) {
                if (next < current+1 || next > current+3) {
                    return false
                }
            } else {
                if (next > current-1 || next < current-3) {
                    return false
                }
            }
            current = next
        }
        return true
    }
}

fun parse(input: String): List<Report> {
    return input.split("\n").mapNotNull() { line ->
        if (line.isBlank()) {
            return@mapNotNull null
        }
        val levels: List<Level> = line.split(" ").map(String::toInt)
        return@mapNotNull Report(levels)
    }
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day02/task01.data"
    val input = File(path).bufferedReader().readText()
    val reports = parse(input)
    println(reports.count(Report::isSafe))
}