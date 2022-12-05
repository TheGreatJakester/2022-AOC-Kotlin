import java.lang.Exception

fun <T> MutableList<T>.pop(): T? {
    if (this.isEmpty()) return null
    val top = this[0]
    removeAt(0)
    return top
}

fun <T> MutableList<T>.push(element: T) = this.add(0, element)

data class Move(val count: Int, val from: Int, val to: Int)

fun main() {

    fun readInTowers(input: List<String>, towerCount: Int): List<MutableList<Char>> {
        val towers = List(towerCount) { mutableListOf<Char>() }
        input.forEach { line ->
            for (towerIndex in 0 until towerCount) {
                val letterIndex = towerIndex * 4 + 1
                if (letterIndex > line.length) break

                val letter = line[letterIndex]
                if (letter != ' ') {
                    towers[towerIndex].add(letter)
                }
            }
        }

        return towers
    }

    fun readInMoves(input: List<String>): List<Move> {
        return input.map {
            val broken = it.split(" ")
            Move(broken[1].toInt(), broken[3].toInt() - 1, broken[5].toInt() - 1)
        }
    }

    fun moveSingle(from: MutableList<Char>, to: MutableList<Char>, count: Int) {
        repeat(count) {
            from.pop()?.let { to.push(it) }
        }
    }

    fun moveMultiple(from: MutableList<Char>, to: MutableList<Char>, count: Int) {
        val top = from.take(count)
        repeat(count) {
            from.pop()
        }
        to.addAll(0, top)
    }


    fun part1(input: List<String>): String {
        val indexOfSplit = input.indexOf("")
        val towerCount = input[indexOfSplit - 1].length / 4 + 1

        val towerState = input.subList(0, indexOfSplit - 1)
        val movesList = input.subList(indexOfSplit + 1, input.size)

        val towers = readInTowers(towerState, towerCount)
        val moves = readInMoves(movesList)

        moves.forEach {
            moveSingle(towers[it.from], towers[it.to], it.count)
        }

        return towers.joinToString(separator = "") { "${it.pop()}" }
    }

    fun part2(input: List<String>): String {
        val indexOfSplit = input.indexOf("")
        val towerCount = input[indexOfSplit - 1].length / 4 + 1

        val towerState = input.subList(0, indexOfSplit - 1)
        val movesList = input.subList(indexOfSplit + 1, input.size)

        val towers = readInTowers(towerState, towerCount)
        val moves = readInMoves(movesList)

        moves.forEach {
            moveMultiple(towers[it.from], towers[it.to], it.count)
        }

        return towers.joinToString(separator = "") { "${it.pop()}" }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val pt1 = part1(testInput)
    check(pt1 == "CMZ")
    val pt2 = part2(testInput)
    check(pt2 == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}