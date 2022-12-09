package day09

import asLines
import readInputAsText
import runSolver
import kotlin.math.absoluteValue

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "09"
private val testSolution1: SolutionType? = 88
private val testSolution2: SolutionType? = 36


private typealias RopeEnd = Pair<Int, Int>

fun RopeEnd.delta(x: Int, y: Int): RopeEnd {
    return first + x to second + y
}

fun Int.justOne() = this.coerceIn(-1, 1)

/**
 * @return new position of tail
 */
fun tailFollowsHead(head: RopeEnd, tail: RopeEnd): RopeEnd {
    val xDelta = (head.first - tail.first)
    val yDelta = (head.second - tail.second)

    if (xDelta.absoluteValue < 2 && yDelta.absoluteValue < 2) {
        return tail
    }

    return tail.delta(xDelta.justOne(), yDelta.justOne())
}


private fun part1(input: String): SolutionType {
    val tailPositions = mutableSetOf<RopeEnd>()

    val moves = input.asLines()

    var head = 0 to 0
    var tail = 0 to 0

    fun processMove(xDelta: Int, yDelta: Int) {
        head = head.delta(xDelta, yDelta)
        tail = tailFollowsHead(head, tail)
        tailPositions.add(tail)
    }

    moves.forEach { line ->
        val (move, count) = line.split(" ")

        repeat(count.toInt()) {
            when (move) {
                "U" -> processMove(0, 1)
                "D" -> processMove(0, -1)
                "L" -> processMove(-1, 0)
                "R" -> processMove(1, 0)
                else -> println("Unknown move $move")
            }
        }
    }


    return tailPositions.size
}

private fun part2(input: String): SolutionType {
    val tailPositions = mutableSetOf<RopeEnd>()

    val moves = input.asLines()

    val knots = mutableListOf(
        0 to 0,
        0 to 0,
        0 to 0,
        0 to 0,
        0 to 0,
        0 to 0,
        0 to 0,
        0 to 0,
        0 to 0,
        0 to 0
    )

    fun processMove(xDelta: Int, yDelta: Int) {
        knots[0] = knots.first().delta(xDelta, yDelta)

        for (i in 0 until knots.size) {
            val lead = knots.getOrNull(i) ?: continue
            val follow = knots.getOrNull(i + 1) ?: continue

            knots[i + 1] = tailFollowsHead(lead, follow)
        }

        tailPositions.add(knots.last())
    }

    moves.forEach { line ->
        val (move, count) = line.split(" ")

        repeat(count.toInt()) {
            when (move) {
                "U" -> processMove(0, 1)
                "D" -> processMove(0, -1)
                "L" -> processMove(-1, 0)
                "R" -> processMove(1, 0)
                else -> println("Unknown move $move")
            }
        }
    }


    return tailPositions.size
}

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)

    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
