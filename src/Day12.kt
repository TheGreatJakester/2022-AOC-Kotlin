package day12

import utils.forForEach
import utils.readInputAsText
import utils.runSolver
import utils.string.asLines

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "12"
private val testSolution1: SolutionType? = 31
private val testSolution2: SolutionType? = 29


private val startCode = 'S'.code
private val endCode = 'E'.code
private val lowcode = 'a'.code
private val highCode = 'z'.code

private class Node(val x: Int, val y: Int, code: Int) {
    var distance = Int.MAX_VALUE

    val elevation: Int
    val isStart: Boolean
    val isEnd: Boolean

    init {
        when (code) {
            startCode -> {
                isStart = true
                isEnd = false
                elevation = lowcode
            }

            endCode -> {
                isEnd = true
                isStart = false
                elevation = highCode
            }

            else -> {
                isStart = false
                isEnd = false
                elevation = code
            }
        }
    }

    lateinit var upHillEdges: List<Node>
    lateinit var downHillEdges: List<Node>


    fun setUp(grid: List<List<Node>>) {
        fun nodeAt(x: Int, y: Int): Node? = grid.getOrNull(y)?.getOrNull(x)
        val down = nodeAt(x, y + 1)
        val up = nodeAt(x, y - 1)
        val left = nodeAt(x - 1, y)
        val right = nodeAt(x + 1, y)

        val upHill = listOfNotNull(up, down, left, right).filter {
            return@filter it.elevation <= this.elevation + 1
        }

        val downHill = listOfNotNull(up, down, left, right).filter {
            return@filter it.elevation >= this.elevation - 1
        }

        upHillEdges = upHill
        downHillEdges = downHill
    }
}

/** @returns start [Node] */
private fun parseGraph(input: String): List<Node> {
    val lines = input.asLines()
    val nodes = lines.mapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Node(x, y, char.code)
        }
    }

    nodes.forForEach { _, _, el -> el.setUp(nodes) }
    return nodes.flatten()
}

private fun start(input: String): Node {
    return parseGraph(input).first { it.isStart }.apply { distance = 0 }
}

private fun end(input: String): Node {
    return parseGraph(input).first { it.isEnd }.apply { distance = 0 }
}


private fun part1(input: String): SolutionType {
    val firstNode = start(input)

    var searchQueue = ArrayDeque(listOf(firstNode))
    val searched = mutableSetOf<Node>()

    while (searchQueue.isNotEmpty()) {
        searchQueue = ArrayDeque(searchQueue.toSet().sortedBy { it.distance })
        val next = searchQueue.removeFirst()

        if (next.isEnd) {
            return next.distance
        }

        searched.add(next)

        val canGo = next.upHillEdges.filter { it !in searched }
        canGo.forEach { it.distance = next.distance + 1 }

        searchQueue.addAll(canGo)

    }

    throw Exception("No path found")
}

private fun part2(input: String): SolutionType {
    val firstNode = end(input)

    var searchQueue = ArrayDeque(listOf(firstNode))
    val searched = mutableSetOf<Node>()

    while (searchQueue.isNotEmpty()) {
        searchQueue = ArrayDeque(searchQueue.toSet().sortedBy { it.distance })
        val next = searchQueue.removeFirst()

        if (next.elevation == lowcode) {
            return next.distance
        }

        searched.add(next)

        val canGo = next.downHillEdges.filter { it !in searched }
        canGo.forEach { it.distance = next.distance + 1 }

        searchQueue.addAll(canGo)
    }

    throw Exception("No path found")
}

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)

    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
