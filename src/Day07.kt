typealias SolutionType = String

private val dayNumber: String = "07"
private val testSolution1: SolutionType? = null
private val testSolution2: SolutionType? = null

fun part1(input: String): SolutionType {
    TODO()
}

fun part2(input: String): SolutionType {
    TODO()
}

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)

    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}

