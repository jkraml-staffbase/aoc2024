package day03

import java.io.File
import java.util.regex.Pattern

val regex = Pattern.compile("mul\\((?<op1>\\d{1,3}),(?<op2>\\d{1,3})\\)")

interface Instruction

data class Mul(
    val op1: Int,
    val op2: Int
) : Instruction {
    fun exec(): Int {
        return op1 * op2
    }
}

fun parse(input: String): List<Mul> {
    val ret = mutableListOf<Mul>()
    val matcher = regex.matcher(input)
    while (matcher.find()) {
        val op1 = matcher.group("op1")!!.toInt()
        val op2 = matcher.group("op2")!!.toInt()
        ret.add(Mul(op1, op2))
    }
    return ret
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day03/data"
    val input = File(path).bufferedReader().readText()

    val muls = parse(input)
    val sum = muls.sumOf(Mul::exec)

    println(sum)
}