fun main() {
    fun part1(input: List<String>): Int {
        val hands = input
            .map { it.split(" ") }
            .map { (theirs, yours) ->
                val theirHand = Hand.values().first { it.theirs == theirs }
                val yourHand = Hand.values().first { it.yours == yours }

                val winScore = if (theirHand.beats == yourHand) {
                    0
                } else if (yourHand.beats == theirHand) {
                    6
                } else {
                    3
                }

                winScore + yourHand.score
            }

        return hands.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 13)

    val input = readInput("Day02")
    println(part1(input.take(3)))
    println(part2(input))
}


sealed class Hand(val theirs: String, val yours: String, val score: Int, val beats: Hand) {
    object Rock : Hand("A", "X", 1, Scissors)
    object Paper : Hand("B", "Y", 2, Rock)
    object Scissors : Hand("C", "Z", 3, Paper)

    companion object {
        fun values(): Array<Hand> {
            return arrayOf(Rock, Paper, Scissors)
        }

        fun valueOf(value: String): Hand {
            return when (value) {
                "Rock" -> Rock
                "Paper" -> Paper
                "Scissors" -> Scissors
                else -> throw IllegalArgumentException("No object Hand.$value")
            }
        }
    }
}