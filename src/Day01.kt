import utils.readInputAsLines

fun main() {
    fun part1(input: List<String>): Int {
        val elves = buildElfList(input)
        return elves.maxOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        val elves = buildElfList(input)
        return elves.map { it.sum() }.sorted().takeLast(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsLines("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInputAsLines("Day01")
    println(part1(input))
    println(part2(input))
}

fun buildElfList(input: List<String>): List<List<Int>> {
    val elves = mutableListOf<List<Int>>()
    var currentList = mutableListOf<Int>()

    input.forEach {
        if (it.isEmpty()){
            elves.add(currentList)
            currentList = mutableListOf()
        } else {
            currentList.add(it.toInt())
        }
    }
    return elves
}
