package day10

import utils.string.asLines
import utils.readInputAsText
import utils.runSolver
import utils.string.blockChar
import utils.string.squiggleChar

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "10"
private val testSolution1: SolutionType? = 13140
private val testSolution2: SolutionType? = null


private fun part1(input: String): SolutionType {
    val instructions = input.asLines()
    val cycleSteps = instructions.map {
        if (it == "noop") {
            listOf(0)
        } else {
            val s = it.split(" ")
            listOf(0, s[1].toInt())
        }
    }.flatten()


    val totals = mutableListOf<Int>()
    var runningTotal = 1

    cycleSteps.forEachIndexed { index, step ->
        val cycleIndex = index + 1
        if (cycleIndex == 20 || (cycleIndex - 20) % 40 == 0) {
            totals.add(cycleIndex * runningTotal)
        }
        runningTotal += step
    }

    return totals.sum()
}

private fun part2(input: String): SolutionType {
    val instructions = input.asLines()
    val cycleSteps = instructions.map {
        if (it == "noop") {
            listOf(0)
        } else {
            val s = it.split(" ")
            listOf(0, s[1].toInt())
        }
    }.flatten()

    val pixels = mutableListOf<Boolean>()
    var runningTotal = 1

    cycleSteps.forEachIndexed { index, step ->
        val screenPosition = index % 40

        if (runningTotal in (screenPosition - 1)..(screenPosition + 1)) {
            pixels.add(true)
        } else {
            pixels.add(false)
        }

        runningTotal += step
    }

    pixels.chunked(40) {
        println(it.joinToString(separator = "") { if (it) squiggleChar else " " })
    }

    return defaultSolution
}

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)

    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
