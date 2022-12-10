package day2017_10_2

import utils.runSolver

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "10"
private val testSolution1: SolutionType? = 88
private val testSolution2: SolutionType? = 36


class CircularList(private val backingList: MutableList<Int>) : List<Int> by backingList {
    override operator fun get(index: Int): Int = backingList[index % backingList.size]
    fun set(index: Int, element: Int): Int = backingList.set(index % backingList.size, element)
    fun replace(index: Int, collection: Collection<Int>) {
        collection.forEachIndexed { curIndex, el ->
            set(index + curIndex, el)
        }
    }

    fun cut(index: Int, length: Int): List<Int> {
        val out = mutableListOf<Int>()
        for (curIndex in index until length + index) {
            out.add(get(curIndex))
        }
        return out
    }

    override fun toString() = backingList.toString()

}


private fun part1(input: Pair<List<Int>, Int>): SolutionType {
    val (lengths, loopSize) = input
    val list = CircularList(List(loopSize) { it }.toMutableList())

    var skip = 0
    var curIndex = 0

    lengths.forEach { length ->
        val cut = list.cut(curIndex, length)
        list.replace(curIndex, cut.asReversed())
        curIndex += length + skip
        skip += 1
    }

    println(list)

    return list[0] * list[1]
}

private fun part2(index: String): String {
    val lengths = index.map { it.code } + listOf(17, 31, 73, 47, 23)
    val allLengths = List(64) { lengths }.flatten()

    val list = CircularList(List(256) { it }.toMutableList())

    var skip = 0
    var curIndex = 0

    allLengths.forEach { length ->
        val cut = list.cut(curIndex, length)
        list.replace(curIndex, cut.asReversed())
        curIndex += length + skip
        skip += 1
    }

    val chunks = list.chunked(16)
    val denseList = chunks.map {
        it.reduce { acc, i ->
            acc.xor(i)
        }
    }

    return denseList.joinToString(separator = "") { it.toString(16) }
}

fun main() {
    val part1TestInput = listOf(3, 4, 1, 5)
    val part1Input = listOf(97, 167, 54, 178, 2, 11, 209, 174, 119, 248, 254, 0, 255, 1, 64, 190)

    val part2TestInput = "AoC 2017"
    val part2Input = "97,167,54,178,2,11,209,174,119,248,254,0,255,1,64,190"


    runSolver("Test 1", part1TestInput to 5, 12, ::part1)
    runSolver("Test 2 $part2TestInput", part2TestInput, "33efeb34ea91902bb2f59c9920caa6cd", ::part2)

    runSolver("Part 1", part1Input to 256, null, ::part1)
    runSolver("Part 2", part2Input, null, ::part2)
}
