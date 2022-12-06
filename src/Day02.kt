val possibleHands = mapOf(
    "A X" to 4,
    "B X" to 1,
    "C X" to 7,

    "A Y" to 8,
    "B Y" to 5,
    "C Y" to 2,

    "A Z" to 3,
    "B Z" to 9,
    "C Z" to 6
)

val possibleHandsStrat2 = mapOf(
    "A X" to 3,
    "B X" to 1,
    "C X" to 2,

    "A Y" to 4,
    "B Y" to 5,
    "C Y" to 6,

    "A Z" to 6+2,
    "B Z" to 6+3,
    "C Z" to 6+1
)
fun main() {
    fun part1(input: List<String>): Int {
        val hands = input.mapNotNull { possibleHands[it] }
        return hands.sum()
    }

    fun part2(input: List<String>): Int {
        val hands = input.mapNotNull { possibleHandsStrat2[it] }
        return hands.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsLines("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInputAsLines("Day02")
    println(part1(input))
    println(part2(input))
}

