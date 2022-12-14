package day14

import utils.readInputAsText
import utils.runSolver
import utils.string.asLines

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "14"
private val testSolution1: SolutionType? = 24
private val testSolution2: SolutionType? = 93


typealias Point = Pair<Int, Int>

fun Point.pointsBellow() = listOf(
    first to second + 1,
    first - 1 to second + 1,
    first + 1 to second + 1
)

val generationPoint = 500 to 0

private fun getCaveFromInput(input: String): MutableSet<Point> {
    val linePairs = input.asLines().map { line ->
        line.split(" -> ").map { pointText ->
            val c = pointText.split(",")
            c[0].toInt() to c[1].toInt()
        }
    }

    val cavePoints = mutableSetOf<Point>()

    infix fun Int.overTo(other: Int): IntRange {
        if (this > other) {
            return other..this
        }
        return this..other
    }

    fun addLine(from: Point, to: Point) {
        // for vertical lines
        if (from.first == to.first) {
            for (y in from.second overTo to.second) {
                cavePoints.add(from.first to y)
            }
            return
        }

        val slope = (from.second - to.second).toFloat() / (from.first - to.first)

        for (x in from.first overTo to.first) {
            val y = (slope * (x - from.first)).toInt() + to.second
            cavePoints.add(x to y)
        }
    }


    linePairs.forEach { line ->
        line.windowed(2) {
            addLine(it[0], it[1])
        }
    }


    return cavePoints
}

private fun part1(input: String): SolutionType {
    val filledPoints = getCaveFromInput(input)

    var sandCount = 0

    fun isSandDeep(sand: Point) = sand.second > 1000

    tailrec fun nextSandPoint(sand: Point = generationPoint): Point {
        if (isSandDeep(sand)) return sand

        sand.pointsBellow().firstOrNull {
            !filledPoints.contains(it)
        }?.let {
            return nextSandPoint(it)
        }

        return sand
    }

    while (true) {
        val sand = nextSandPoint()
        if (isSandDeep(sand)) break

        filledPoints.add(sand)
        sandCount += 1
    }

    return sandCount
}

private fun part2(input: String): SolutionType {
    val filledPoints = getCaveFromInput(input)
    var sandCount = 1
    val floorLevel = filledPoints.maxOf { it.second } + 2
    fun isSandOnFloor(sand: Point) = sand.second + 1 == floorLevel

    tailrec fun nextSandPoint(sand: Point = generationPoint): Point {
        if (isSandOnFloor(sand)) return sand

        sand.pointsBellow().firstOrNull {
            !filledPoints.contains(it)
        }?.let {
            return nextSandPoint(it)
        }

        return sand
    }

    while (true) {
        val sand = nextSandPoint()
        if (sand == generationPoint) break

        filledPoints.add(sand)
        sandCount += 1
    }

    return sandCount
}


fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)

    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
