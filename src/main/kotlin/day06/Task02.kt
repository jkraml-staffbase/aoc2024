package day06

import java.io.File

object Task02 {

    fun alternativeInputsWithObstacle(original: Input) = sequence<Input> {
        var i = 0
        for (x in 0 until original.maze.width) {
            for (y in 0 until original.maze.height) {
                val pos = Position(x, y)
                if (original.maze.isObstacle(pos)) {
                    continue // there's already an obstacle here
                }
                if (original.start.position == pos) {
                    continue // can't put obstacle on guard
                }

                val modifiedMaze = original.maze
                    .copy()
                    .apply { addObstacle(pos) }

                // some debug output, it took so long to run
                val perc = (i.toDouble() / (original.maze.width * original.maze.height)) * 100
                println("yield done $perc%")
                i++

                yield(Input(
                    modifiedMaze,
                    original.start
                ))
            }
        }
    }

    fun solve(inputString: String): Int {
        val input = parseInput(inputString)

        return alternativeInputsWithObstacle(input)
            .map { walkGuard(it) }
            .filter{ r -> r.result == Result.LOOP }
            .count()
    }
}

fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day06/data"
    val input = File(path).bufferedReader().readText()

    val count = Task02.solve(input)
    println(count)
}