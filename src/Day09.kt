package day09

import utils.string.asLines
import utils.numbers.justOne
import utils.readInputAsText
import utils.runSolver
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "09"
private val testSolution1: SolutionType? = 88
private val testSolution2: SolutionType? = 36

private class Knot(var tail: Knot? = null) {
    var x: Int = 0
    var y: Int = 0

    val last: Knot get() = tail?.last ?: this
    val asPair get() = x to y

    fun moveUp() {
        y += 1
        updateTail()
    }


    fun moveDown() {
        y -= 1
        updateTail()
    }


    fun moveLeft() {
        x -= 1
        updateTail()
    }


    fun moveRight() {
        x += 1
        updateTail()
    }

    private fun updateTail() {
        val t = tail ?: return
        val xDelta = (x - t.x)
        val yDelta = (y - t.y)

        if (xDelta.absoluteValue < 2 && yDelta.absoluteValue < 2) {
            return
        }

        t.x += xDelta.justOne()
        t.y += yDelta.justOne()

        t.updateTail()
    }

    companion object {
        fun builtRopeOfLength(length: Int): Knot {
            if(length < 1) throw IllegalArgumentException("Length must be 1 or more")

            var curKnot: Knot = Knot()
            repeat(length - 1) {
                val nextKnot = Knot(curKnot)
                curKnot = nextKnot
            }
            return curKnot
        }
    }
}

private fun readMoves(input: String, head: Knot, afterMove: () -> Unit) {
    input.asLines().forEach { line ->
        val (move, count) = line.split(" ")

        repeat(count.toInt()) {
            when (move) {
                "U" -> head.moveUp()
                "D" -> head.moveDown()
                "L" -> head.moveLeft()
                "R" -> head.moveRight()
                else -> println("Unknown move $move")
            }
            afterMove()
        }
    }
}

private fun doPartWithLength(input: String, length: Int): Int{
    val tailPositions = mutableSetOf<Pair<Int, Int>>()

    val rope = Knot.builtRopeOfLength(length)
    val tail = rope.last

    readMoves(input, rope) {
        tailPositions.add(tail.asPair)
    }

    return tailPositions.size
}


private fun part1(input: String): SolutionType = doPartWithLength(input, 2)

private fun part2(input: String): SolutionType = doPartWithLength(input, 10)

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)

    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
