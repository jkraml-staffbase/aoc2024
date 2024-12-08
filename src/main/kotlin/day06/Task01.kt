package day06

import java.io.File
import kotlin.collections.toMutableSet

data class Position(
    val x: Int,
    val y: Int
)

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun turnRight(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }
}

data class Waypoint(
    val position: Position,
    val direction: Direction
)

class Path(
    initialWaypoints: List<Waypoint>
) {
    private val waypoints = initialWaypoints.toMutableList()
    private val visitedWaypoints = waypoints.toMutableSet<Waypoint>() // for faster lookup
    private val visitedPositions = waypoints.map { it.position }.toMutableSet<Position>()

    fun start() = waypoints.first()
    fun end() = waypoints.last()
    fun next(): Waypoint {
        val end = end()
        val nextPos = when (end.direction) {
            Direction.NORTH -> Position(end.position.x, end.position.y + 1)
            Direction.EAST -> Position(end.position.x + 1, end.position.y)
            Direction.SOUTH -> Position(end.position.x, end.position.y - 1)
            Direction.WEST -> Position(end.position.x - 1, end.position.y)
        }
        return Waypoint(nextPos, end.direction)
    }

    fun replaceLast(waypoint: Waypoint) {
        waypoints[waypoints.size - 1] = waypoint
    }

    fun add(waypoint: Waypoint) {
        // we don't actually check this makes sense (e.g. is adjacent to current last position)

        waypoints.add(waypoint)
        visitedPositions.add(waypoint.position)
        visitedWaypoints.add(waypoint)
    }

    fun distinctVisitedPositions() = visitedPositions.size

    fun alreadyVisitedInDirection(waypoint: Waypoint) =
        visitedPositions.contains(waypoint.position) && // first condition is a fast check
                visitedWaypoints.contains(waypoint) // this also includes directions
}

/**
 * Sparse maze representation.
 */
class Maze {
    val width: Int
    val height: Int
    private val obstacles: MutableMap<Int, MutableSet<Int>> = mutableMapOf()

    constructor(width: Int, height: Int, obstacles: List<Position>) {
        this.height = height
        this.width = width
        obstacles.forEach(::addObstacle)
    }

    fun isObstacle(pos: Position): Boolean = obstacles[pos.x]?.contains(pos.y) ?: false

    fun isWithinBounds(pos: Position): Boolean = pos.x in 0 until width && pos.y in 0 until height

    fun addObstacle(pos: Position) {
        // no validation of positions
        obstacles.computeIfAbsent(pos.x) { _ -> mutableSetOf() }.add(pos.y)
    }

    fun copy(): Maze {
        val copy = Maze(width, height, emptyList())
        obstacles.forEach { (x, ys) ->
            ys.forEach { y -> copy.addObstacle(Position(x, y)) }
        }
        return copy
    }
}

data class Input(
    val maze: Maze,
    val start: Waypoint
)

fun parseInput(inputString: String): Input {
    val lines = inputString.lines().filter(String::isNotBlank).reversed() // reverse to have y=0 at the bottom
    val obstacles = mutableListOf<Position>()
    var start: Waypoint? = null // we assume there is exactly one start position in the input

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            when (c) {
                '#' -> obstacles.add(Position(x, y))
                '^' -> start = Waypoint(Position(x, y), Direction.NORTH)
                'v' -> start = Waypoint(Position(x, y), Direction.SOUTH)
                '<' -> start = Waypoint(Position(x, y), Direction.EAST)
                '>' -> start = Waypoint(Position(x, y), Direction.WEST)
            }
        }
    }

    val height= lines.size
    val width = lines.maxOf { it.length } // we don't check they're all the same length
    return Input(Maze(width, height, obstacles), start!!)
}

enum class Result {
    LEFT_AREA, LOOP
}

data class WalkResult(
    val result: Result,
    val path: Path
)

fun walkGuard(input: Input): WalkResult {
    val maze = input.maze
    val path = Path(listOf(input.start))

    while (true) {
        val next = path.next()
        if (!maze.isWithinBounds(next.position)) {
            return WalkResult(Result.LEFT_AREA, path)
        }

        if (path.alreadyVisitedInDirection(next)) {
            return WalkResult(Result.LOOP, path)
        }

        if (!maze.isObstacle(next.position)) {
            path.add(next)
            continue
        }

        val nextDirection = next.direction.turnRight()
        path.replaceLast(Waypoint(path.end().position, nextDirection))
    }
}

object Task01 {

    fun solve(inputString: String): Int {
        val input = parseInput(inputString)
        val walkResult = walkGuard(input)
        return walkResult.path.distinctVisitedPositions()
    }

}


fun main() {
    val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day06/data"
    val input = File(path).bufferedReader().readText()

    val count = Task01.solve(input)
    println(count)
}