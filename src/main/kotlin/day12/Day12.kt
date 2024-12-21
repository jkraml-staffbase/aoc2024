package day12

import java.io.File

object Day12 {

    data class PuzzleInput(
        val width: Int,
        val height: Int,
        val values: CharArray
    ) : Sequence<Position> {
        fun get(x: Int, y: Int): Char? {
            if (x !in 0 until width || y !in 0 until height) {
                return null
            }
            return values[y * width + x]
        }

        fun get(p: Position): Char? {
            return get(p.x, p.y)
        }

        override fun iterator(): Iterator<Position> {
            return iterator {
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        yield(Position(x, y))
                    }
                }
            }
        }

    }


    data class Position(
        val x: Int, val y: Int
    ) {
        fun move(d: Direction): Position {
            return when (d) {
                Direction.UP -> Position(x, y - 1)
                Direction.DOWN -> Position(x, y + 1)
                Direction.LEFT -> Position(x - 1, y)
                Direction.RIGHT -> Position(x + 1, y)
            }
        }
    }

    private fun parse(inputString: String): PuzzleInput {
        val lines = inputString.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .reversed()
            .toList()
        val values = mutableListOf<Char>()
        lines.forEach { l ->
            if (l.trim().isEmpty()) {
                return@forEach
            }
            l.trim().forEach { c ->
                values.add(c)
            }
        }
        val height = lines.size
        val width = values.size / height
        if (values.size % height != 0) {
            throw IllegalArgumentException("Invalid input")
        }
        return PuzzleInput(width, height, values.toCharArray())
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun nextClockwise(): Direction {
            return when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
        }

        fun nextCounterclockwise(): Direction {
            return when (this) {
                UP -> LEFT
                LEFT -> DOWN
                DOWN -> RIGHT
                RIGHT -> UP
            }
        }

        companion object {
            fun clockwise(): Sequence<Direction> {
                return sequenceOf(UP, RIGHT, DOWN, LEFT)
            }
        }
    }

    data class Borders(
        val width: Int,
        val height: Int,
        val values: BooleanArray
    ) {
        init {
            if (values.size != (idx(Position(width - 1, height - 1), Direction.DOWN) + 1)) {
                throw IllegalArgumentException("Invalid input")
            }
        }

        fun isBorder(p: Position, d: Direction): Boolean {
            return p.x in 0 until width
                    && p.y in 0 until height
                    && values[idx(p, d)]
        }

        fun setBorder(p: Position, d: Direction, value: Boolean) {
            values[idx(p, d)] = value
        }

        private fun idx(p: Position, d: Direction): Int {
            require(p.x in 0 until width && p.y in 0 until height) { "Invalid position $p" }
            return when (d) {
                Direction.UP -> p.y * (2 * width + 1) + p.x
                Direction.DOWN -> (p.y + 1) * (2 * width + 1) + p.x
                Direction.LEFT -> p.y * (2 * width + 1) + width + p.x
                Direction.RIGHT -> p.y * (2 * width + 1) + width + p.x + 1
            }
        }

        companion object {
            fun forSize(width: Int, height: Int): Borders {
                val maxPos = Position(width - 1, height - 1)
                val maxIdx = (maxPos.y + 1) * (2 * width + 1) + maxPos.x
                return Borders(width, height, BooleanArray(maxIdx + 1, { true }))
            }
        }
    }

    data class Region(
        val letter: Char,
        val positions: MutableSet<Position>
    ) : Sequence<Position> {
        override fun iterator(): Iterator<Position> {
            return positions.iterator()
        }

        fun area(): Int {
            return positions.size
        }

        fun borderPositions(): Set<Position> {
            return positions.filter { p ->
                Direction.entries.any { d ->
                    val neighbor = p.move(d)
                    neighbor !in positions
                }
            }.toSet()
        }
    }

    private fun collectRegions(input: PuzzleInput, b: Borders): Set<Region> {
        val regions = mutableSetOf<Region>()
        val visitedPositions = mutableSetOf<Position>()

        input.forEach { p ->
            if (visitedPositions.contains(p)) {
                return@forEach
            }
            val c = input.get(p) ?: return@forEach
            val region = Region(c, mutableSetOf(p))
            regions.add(region)

            expandRegions(region, p, input, b, visitedPositions)
        }

        return regions
    }

    private fun expandRegions(
        r: Region,
        p: Position,
        input: PuzzleInput,
        borders: Borders,
        visitedPositions: MutableSet<Position>
    ) {
        if (visitedPositions.contains(p)) {
            return
        }
        if (input.get(p) != r.letter) {
            return
        }
        visitedPositions.add(p)
        r.positions.add(p)

        Direction.entries.forEach { d ->
            val neighbor = p.move(d)
            if (input.get(neighbor) == r.letter) {
                borders.setBorder(p, d, false)
            }
            expandRegions(r, neighbor, input, borders, visitedPositions)
        }
    }

    fun solveT1(inputString: String): Long {
        val input = parse(inputString)
        val borders = Borders.forSize(input.width, input.height)
        val regions = collectRegions(input, borders)

        return regions.sumOf { r -> countFences(r, borders) * r.area() }.toLong()
    }

    private fun countFences(r: Region, borders: Borders): Int {
        return r.sumOf { p ->
            Direction.entries.count { d ->
                borders.isBorder(p, d)
            }
        }
    }


    fun solveT2(inputString: String): Long {
        val input = parse(inputString)
        val borders = Borders.forSize(input.width, input.height)
        val regions = collectRegions(input, borders)

        return regions.sumOf { r -> countFenceSegments(r, borders) * r.area() }.toLong()
    }

    private fun countFenceSegments(r: Region, b: Borders): Int {
        var segmentCount = 0
        Direction.entries.forEach { d ->
            val positionWithBorderOnThatSide = r.positions
                .filter { b.isBorder(it, d) }
                .toMutableSet()

            while (positionWithBorderOnThatSide.isNotEmpty()) {
                // remove a position from set
                val randomSegmentPosition = positionWithBorderOnThatSide.first()
                positionWithBorderOnThatSide.remove(randomSegmentPosition)

                // remove all positions reachable by following segment by right-hand rule (ie CCW)
                var currentSegment = randomSegmentPosition
                while (true) {
                    val candidate = currentSegment.move(d.nextCounterclockwise())
                    if (!positionWithBorderOnThatSide.remove(candidate)) {
                        break
                    }
                    currentSegment = candidate
                }

                // remove all positions reachable by following segment by left-hand rule (ie CW)
                currentSegment = randomSegmentPosition
                while (true) {
                    val candidate = currentSegment.move(d.nextClockwise())
                    if (!positionWithBorderOnThatSide.remove(candidate)) {
                        break
                    }
                    currentSegment = candidate
                }

                segmentCount++
            }
        }
        return segmentCount
    }
}

fun main() {
    val testInput = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()
    val t1Expected = 1930L
    val t2Expected = 1206L

    val puzzleInput: String by lazy {
        val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day12/data"
        File(path).bufferedReader().readText()
    }

    val t1res = Day12.solveT1(testInput)
    if (t1res == t1Expected) {
        println("Example 1 passed")
    } else {
        println("Example 1 failed, was $t1res, should've been $t1Expected")
        return
    }
    println("Task 1: ${Day12.solveT1(puzzleInput)}")

    val t2res = Day12.solveT2(testInput)
    if (t2res == t2Expected) {
        println("Example 2 passed")
    } else {
        println("Example 2 failed, was $t2res, should've been $t2Expected")
        return
    }
    println("Task 2: ${Day12.solveT2(puzzleInput)}")

}