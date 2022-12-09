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


private typealias Knot = Pair<Int, Int>

fun Knot.delta(x: Int, y: Int): Knot {
    return first + x to second + y
}

fun Int.justOne() = this.coerceIn(-1, 1)

/**
 * @return new position of tail
 */
fun tailAfterFollowingHead(head: Knot, tail: Knot): Knot {
    val xDelta = (head.first - tail.first)
    val yDelta = (head.second - tail.second)

    if (xDelta.absoluteValue < 2 && yDelta.absoluteValue < 2) {
        return tail
    }

    return tail.delta(xDelta.justOne(), yDelta.justOne())
}

fun readMoves(input: String, processMove: (x: Int, y: Int) -> Unit) {
    input.asLines().forEach { line ->
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
}


private fun part1(input: String): SolutionType {
    val tailPositions = mutableSetOf<Knot>()

    var head = 0 to 0
    var tail = 0 to 0

    readMoves(input) { x, y ->
        head = head.delta(x, y)
        tail = tailAfterFollowingHead(head, tail)
        tailPositions.add(tail)
    }

    return tailPositions.size
}

private fun part2(input: String): SolutionType {
    val tailPositions = mutableSetOf<Knot>()

    val knots = List<Knot>(10) { 0 to 0 }.toMutableList()

    readMoves(input) { x, y ->
        knots[0] = knots.first().delta(x, y)

        for (headIndex in 0 until knots.size - 1) {
            val tailIndex = headIndex + 1
            val lead = knots[headIndex]
            val follow = knots[tailIndex]

            knots[tailIndex] = tailAfterFollowingHead(lead, follow)
        }

        tailPositions.add(knots.last())
    }

    return tailPositions.size
}

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)

    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
