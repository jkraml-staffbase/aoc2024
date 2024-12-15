package day09

import java.io.File

object Day09 {

    data class PuzzleInputT1(
        val blocks: MutableList<Int?>
    )

    private fun parseForT1(inputString: String): PuzzleInputT1 {
        val blocks = mutableListOf<Int?>()

        var isFile = true
        var idCounter = 0
        inputString.trim().forEach { c ->
            val value = "$c".toInt()
            if (isFile) {
                (0 until value).forEach { i ->
                    blocks.add(idCounter)
                }
                idCounter++
            } else {
                (0 until value).forEach { i ->
                    blocks.add(null)
                }
            }
            isFile = !isFile
        }

        return PuzzleInputT1(blocks)
    }

    private fun rearrangeT1(blocks: MutableList<Int?>) {
        var lastFileBlockIdx = blocks.indexOfLast { v -> v != null }
        for (i in 0 until blocks.size) {
            if (i >= lastFileBlockIdx) {
                break
            }

            val block = blocks[i]
            if (block != null) {
                continue
            }
            // found free space

            // swap with last file block
            val lastFileBlock = blocks[lastFileBlockIdx]
            blocks[i] = lastFileBlock
            blocks[lastFileBlockIdx] = null

            // find next file block at end
            while (blocks[lastFileBlockIdx] == null) {
                lastFileBlockIdx--
            }
        }
    }

    private fun hash(blocks: List<Int?>): Long {
        var hash = 0L
        blocks.forEachIndexed { i, v ->
            if (v == null) {
                return@forEachIndexed
            }
            hash += i * v
        }
        return hash
    }

    fun solveT1(inputString: String): Long {
        val input = parseForT1(inputString)

        rearrangeT1(input.blocks)

        return hash(input.blocks)
    }

    data class Block(
        val id: Int?,
        val start: Int,
        val length: Int
    )

    data class PuzzleInputT2(
        val files: MutableMap<Int, Block>,
        val spaces: MutableList<Block>  // ordered by start
    )

    private fun parseForT2(inputString: String): PuzzleInputT2 {
        val puzzle = PuzzleInputT2(mutableMapOf(), mutableListOf())

        var isFile = true
        var idCounter = 0
        var pos = 0
        inputString.trim().forEach { c ->
            val length = "$c".toInt()
            if (isFile) {
                puzzle.files.put(idCounter, Block(idCounter, pos, length))
                idCounter++
            } else {
                puzzle.spaces.add(Block(null, pos, length))
            }
            isFile = !isFile
            pos += length
        }

        return puzzle
    }

    private fun rearrangeT2(input: PuzzleInputT2) {
        val fileIds = input.files.keys.toList().sorted().reversed()
        fileIds.map(input.files::get).forEach { fileBlock ->
//            prettyPrint(linearRepresentation(input))
            if (fileBlock == null) {
                return@forEach // not actually possible
            }

            val fileSize = fileBlock.length
            val spaceBlockIdx = input.spaces.indexOfFirst { it.start < fileBlock.start && it.length >= fileSize }
            if (spaceBlockIdx == -1) {
                return@forEach // no free space
            }
            val spaceBlock = input.spaces[spaceBlockIdx]

            val replacementFileBlock = fileBlock.copy(start = spaceBlock.start)
            input.files.put(replacementFileBlock.id!!, replacementFileBlock)

            if (spaceBlock.length == fileSize) {
                input.spaces.removeAt(spaceBlockIdx)
            } else {
                val replacementSpaceBlock = Block(null, spaceBlock.start + fileSize, spaceBlock.length - fileSize)
                input.spaces[spaceBlockIdx] = replacementSpaceBlock
            }
            val freeSpaceBlock = Block(null, fileBlock.start, fileBlock.length)
            val freeSpaceIdx = input.spaces.indexOfFirst() { it.start > freeSpaceBlock.start }
            if (freeSpaceIdx == -1) {
                input.spaces.add(freeSpaceBlock)
            } else {
                input.spaces.add(freeSpaceIdx, freeSpaceBlock)
                if (freeSpaceIdx > 0) {
                    val prevSpace = input.spaces[freeSpaceIdx - 1]
                    if (prevSpace.start+prevSpace.length == freeSpaceBlock.start) {
                        val mergedSpace = prevSpace.copy(length = prevSpace.length + freeSpaceBlock.length)
                        input.spaces[freeSpaceIdx - 1] = mergedSpace
                        input.spaces.removeAt(freeSpaceIdx)
                    }
                }
            }
        }
    }

    private fun linearRepresentation(input: PuzzleInputT2): List<Int?> {
        val ret = mutableListOf<Int?>()
        var pos = 0
        input.files.values.toList().sortedBy(Block::start).forEach { fileBlock ->
            while (pos < fileBlock.start) {
                ret.add(null)
                pos++
            }
            (0 until fileBlock.length).forEach {
                ret.add(fileBlock.id)
                pos++
            }
        }

        return ret
    }

    fun solveT2(inputString: String): Long {
        val input = parseForT2(inputString)

        rearrangeT2(input)

        val linear = linearRepresentation(input)
        return hash(linear)
    }

    fun prettyPrint(data: List<Int?>) {
        val sb = StringBuilder()
        data.forEach { v ->
            if (v == null) {
                sb.append(".")
            } else {
                sb.append((v % 10).toString(16))
            }
        }
        println(sb.toString())
    }
}

fun main() {
    val testInput = """
        2333133121414131402
    """.trimIndent()
    val t1Expected = 1928L
    val t2Expected = 2858L

    val puzzleInput: String by lazy {
        val path = "/home/jkraml/workspace/playground/2024-advent_of_code/src/main/kotlin/day09/data"
        File(path).bufferedReader().readText()
    }

    val t1res = Day09.solveT1(testInput)
    if (t1res == t1Expected) {
        println("Example 1 passed")
    } else {
        println("Example 1 failed, was $t1res, should've been $t1Expected")
        return
    }
    println("Task 1: ${Day09.solveT1(puzzleInput)}")

    val t2res = Day09.solveT2(testInput)
    if (t2res == t2Expected) {
        println("Example 2 passed")
    } else {
        println("Example 2 failed, was $t2res, should've been $t2Expected")
        return
    }
    println("Task 2: ${Day09.solveT2(puzzleInput)}")  // 8654184283366 is too high

}