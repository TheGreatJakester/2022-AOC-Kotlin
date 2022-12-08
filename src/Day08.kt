private val dayNumber: String = "08"
private val testSolution1 = 21
private val testSolution2 = null


private typealias Grid = List<List<Int>>
private typealias MutableGrid = MutableList<MutableList<Int>>

fun Grid.print() {
    forEach {
        println(it)
    }
    println()
}

fun buildMutableGrid(size: Int): MutableGrid {
    val grid = mutableListOf<MutableList<Int>>()

    repeat(size) {
        val nextLine = mutableListOf<Int>()
        repeat(size) {
            nextLine.add(0)
        }
        grid.add(nextLine)
    }

    return grid
}

fun translateGrid(grid: Grid): MutableGrid {
    val gridSize = grid.size
    val newGrid = buildMutableGrid(gridSize)

    for (i in 0 until gridSize) {
        for (k in 0 until gridSize) {
            newGrid[i][gridSize - (k + 1)] = grid[k][i]
        }
    }

    return newGrid
}

private fun scoreRight(grid: Grid, x: Int, y: Int): Int {
    val heightOfTreeHouse = grid[y][x]
    val row = grid[y].subList(x + 1, grid.size)

    row.forEachIndexed { index, it ->
        if(it >= heightOfTreeHouse){
            return index + 1
        }
    }
    return row.size.coerceAtLeast(1)
}

private fun rotate(x: Int, y: Int, size: Int): Pair<Int, Int> =
    size - (y + 1) to x

private fun rotate(pair: Pair<Int, Int>, size: Int): Pair<Int, Int> =
    rotate(pair.first, pair.second, size)

private fun part1(input: String): Int {
    var grid = input.asLines().map { it.toList().map { it.code - 48 } }
    var scoreGrid = buildMutableGrid(grid.size)

    repeat(4) {
        grid.forEachIndexed { line, list ->
            var tallest = -1
            list.forEachIndexed { tree, height ->
                if (height > tallest) {
                    scoreGrid[line][tree] = 1
                    tallest = height
                }
            }
        }
        grid = translateGrid(grid)
        scoreGrid = translateGrid(scoreGrid)
    }

    return scoreGrid.sumOf { it.sum() }
}

private fun part2(input: String): Int {
    val grid1 = input.asLines().map { it.toList().map { it.code - 48 } }
    val gridSize = grid1.size

    val grid2 = translateGrid(grid1)
    val grid3 = translateGrid(grid2)
    val grid4 = translateGrid(grid3)

    grid1.print()

    val scoreGrid = buildMutableGrid(gridSize)

    for (y in 0 until gridSize) {
        for (x in 0 until gridSize) {
            val right = scoreRight(grid1, x, y)
            val (x2, y2) = rotate(x, y, gridSize)
            val up = scoreRight(grid2, x2, y2)
            val (x3,y3) = rotate(x2,y2, gridSize)
            val left = scoreRight(grid3, x3, y3)
            val (x4,y4) = rotate(x3,y3, gridSize)
            val down = scoreRight(grid4, x4, y4)

            val total =  right * up * left * down

            scoreGrid[y][x] = total
        }
    }

    scoreGrid.print()

    return scoreGrid.maxOf { it.max() }
}

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)

    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}

