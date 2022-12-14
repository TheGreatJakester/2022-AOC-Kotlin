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

private fun getGridFrom(input: String): MutableMap<Point, Boolean> {
    val linePairs = input.asLines().map { line ->
        line.split(" -> ").map { pointText ->
            val c = pointText.split(",")
            c[0].toInt() to c[1].toInt()
        }
    }

    val grid = mutableMapOf<Point, Boolean>()

    infix fun Int.overTo(other: Int): IntRange {
        if (this > other) {
            return other..this
        }
        return this..other
    }

    fun addLine(from: Point, to: Point) {
        if (from.first == to.first) {
            for (y in from.second overTo to.second) {
                grid[from.first to y] = true
            }
        } else if (from.second == to.second) {
            for (x in from.first overTo to.first) {
                grid[x to from.second] = true
            }
        } else {
            throw Exception("I don't do diagonals")
        }
    }


    linePairs.forEach { line ->
        line.windowed(2) {
            addLine(it[0], it[1])
        }
    }

    return grid
}

private fun part1(input: String): SolutionType {
    val grid = getGridFrom(input)
    val generationPoint = 500 to 0

    var sand: Point = generationPoint
    var sandCount = 0

    fun isSandDeep() = sand.second > 1000

    fun updateSand(): Boolean {
        if (isSandDeep()) return false

        val down = sand.first to sand.second + 1
        val left = sand.first - 1 to sand.second + 1
        val right = sand.first + 1 to sand.second + 1

        fun Point.isOpen() = !grid.getOrDefault(this, false)

        if (down.isOpen()) {
            sand = down
            return true
        } else if (left.isOpen()) {
            sand = left
            return true
        } else if (right.isOpen()) {
            sand = right
            return true
        } else return false
    }

    while (true) {
        while (updateSand()) {
        }
        if (isSandDeep()) break

        grid[sand] = true
        sand = generationPoint
        sandCount += 1
    }

    return sandCount
}

private fun part2(input: String): SolutionType {
    val grid = getGridFrom(input)
    val generationPoint = 500 to 0

    var sand: Point = generationPoint
    var sandCount = 1


    val floorLevel = grid.keys.maxOf { it.second } + 2
    fun isSandOnFloor() = sand.second + 1 == floorLevel

    fun updateSand(): Boolean {
        val down = sand.first to sand.second + 1
        val left = sand.first - 1 to sand.second + 1
        val right = sand.first + 1 to sand.second + 1

        fun Point.isOpen() = !grid.getOrDefault(this, false)

        if (isSandOnFloor()) {
            return false
        } else if (down.isOpen()) {
            sand = down
            return true
        } else if (left.isOpen()) {
            sand = left
            return true
        } else if (right.isOpen()) {
            sand = right
            return true
        } else return false
    }

    while (true) {
        while (updateSand()) { }

        if(sand == generationPoint) break

        grid[sand] = true
        sand = generationPoint
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
