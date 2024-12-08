package org.example.day01

import java.io.File
import java.util.regex.Pattern
import kotlin.math.abs


fun main() {

    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/data/day_01_task_01/numbers"
//val path = "/home/jkraml/workspace/playground/2024-advent_of_code/data/day_01_task_01/example"
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
    numbersLeft.sort()
    numbersRight.sort()
    if (numbersLeft.size != numbersRight.size) {
        throw RuntimeException()
    }
    var acc = 0
    numbersLeft.forEachIndexed { i, value ->
        acc += abs(value - numbersRight[i])
    }

    println(acc)
}