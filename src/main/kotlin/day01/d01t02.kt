package org.example.day01

import java.io.File
import java.util.regex.Pattern


fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/data/day_01_task_02/numbers"
//    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/data/day_01_task_02/example"

    val numbersLeft = mutableListOf<Int>()
    val numbersRight = mutableListOf<Int>()
    File(path).bufferedReader().readLines().forEach { l ->
        if (l.isEmpty()) {
            return@forEach
        }
        val parts = l.split(Pattern.compile(" +"))
        numbersLeft.add(parts[0].toInt())
        numbersRight.add(parts[1].toInt())
    }

    val rightGroup = numbersRight.groupBy { i -> i }
    var acc = 0
    numbersLeft.forEach { value ->
        val occurrences = rightGroup[value]?.size ?: 0
        acc += value * occurrences
    }

    println(acc)
}