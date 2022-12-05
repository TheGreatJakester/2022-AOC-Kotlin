import java.lang.Exception

private data class Move(val count: Int, val from: Int, val to: Int)
private typealias Tower = ArrayDeque<Char>

fun main() {
    fun readInTowers(input: List<String>, towerCount: Int): List<Tower> {
        val towers = List(towerCount) { ArrayDeque<Char>() }
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

    fun moveSingle(from: Tower, to: Tower, count: Int) {
        repeat(count) {
            from.removeFirst().let(to::addFirst)
        }
    }

    fun moveMultiple(from: Tower, to: Tower, count: Int) {
        val top = from.take(count)
        repeat(count) {
            from.removeFirst()
        }
        to.addAll(0, top)
    }

    object : Challenge<String>("Day05", "CMZ", "MCD") {
        fun Context.asTowersAndMoves(): Pair<List<Tower>, List<Move>> {
            val (towerInput, moveInput) = fileContents.asParts()

            val towerLines = towerInput.asLines().dropLast(1)
            val towerCount = towerLines.last().length / 4 + 1
            val towers = readInTowers(towerLines, towerCount)

            val moves = readInMoves(moveInput.asLines())

            return towers to moves
        }


        override fun Context.part1(): String {
            val (towers, moves) = asTowersAndMoves()
            moves.forEach {
                moveSingle(towers[it.from], towers[it.to], it.count)
            }
            return towers.joinToString(separator = "") { it.first().toString() }
        }

        override fun Context.part2(): String {
            val (towers, moves) = asTowersAndMoves()
            moves.forEach {
                moveMultiple(towers[it.from], towers[it.to], it.count)
            }
            return towers.joinToString(separator = "") { it.first().toString() }
        }
    }.trySolve()
}