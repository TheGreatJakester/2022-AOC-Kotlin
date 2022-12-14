package day15

import utils.forForEach
import utils.readInputAsText
import utils.runSolver
import utils.string.asLines
import utils.string.asParts

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "14"
private val testSolution1: SolutionType? = null
private val testSolution2: SolutionType? = null

private fun part1(input: String): SolutionType {
    return defaultSolution
}

private fun part2(input: String): SolutionType {
    return defaultSolution
}


fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)

    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
