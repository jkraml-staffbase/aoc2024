package day03

import java.io.File
import java.util.regex.Pattern

val combinedRegex = Pattern.compile("(mul\\((?<op1>\\d{1,3}),(?<op2>\\d{1,3})\\))|(do\\(\\))|(don't\\(\\))")

class Do : Instruction
class Dont : Instruction


fun parseWithConditionals(input: String): List<Mul> {
    val instructions = mutableListOf<Instruction>()
    val matcher = combinedRegex.matcher(input)
    while (matcher.find()) {
        if (matcher.group() == "do()") {
            instructions.add(Do())
        } else if (matcher.group() == "don't()") {
            instructions.add(Dont())
        } else {
            val op1 = matcher.group("op1")!!.toInt()
            val op2 = matcher.group("op2")!!.toInt()
            instructions.add(Mul(op1, op2))
        }
    }

    var active = true
    val activeInstructions = mutableListOf<Mul>()
    for (instruction in instructions) {
        if (instruction is Do) {
            active = true
        } else if (instruction is Dont) {
            active = false
        } else if (active) {
            activeInstructions.add(instruction as Mul)
        }
    }
    return activeInstructions
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day03/data"
    val input = File(path).bufferedReader().readText()

    val muls = parseWithConditionals(input)
    val sum = muls.sumOf(Mul::exec)

    println(sum)
}