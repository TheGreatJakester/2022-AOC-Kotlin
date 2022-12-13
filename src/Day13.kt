package day13

import utils.forForEach
import utils.readInputAsText
import utils.runSolver
import utils.string.asLines
import utils.string.asParts

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "13"
private val testSolution1: SolutionType? = 13
private val testSolution2: SolutionType? = 140


sealed class Member : Comparable<Member>
private data class ListMember(val list: MutableList<Member> = mutableListOf()) : MutableList<Member> by list, Member() {


    override fun compareTo(other: Member): Int {
        if (other is IntMember) {
            return compareTo(other.asListMember())
        } else if (other is ListMember) {
            for (i in list.indices) {
                val here = list[i]
                val there = other.list.getOrNull(i) ?: return 1
                val comparison = here.compareTo(there)
                if (comparison != 0) {
                    return comparison
                }
            }
            return list.size - other.list.size
        }
        throw Exception("Shouldn't")
    }
}

private data class IntMember(val value: Int) : Member() {

    fun asListMember(): ListMember {
        return ListMember(mutableListOf(this))
    }

    override fun compareTo(other: Member): Int {
        if (other is IntMember) {
            return this.value - other.value
        } else if (other is ListMember) {
            return this.asListMember().compareTo(other)
        }
        throw Exception("Shouldn't")
    }
}

private typealias PacketPair = Pair<ListMember, ListMember>

private fun PacketPair.isValid(): Boolean = first < second

private fun parsePairs(input: String): List<PacketPair> {
    val pairString = input.asParts()
    return pairString.map(::parsePair)
}

private fun parsePair(input: String): PacketPair {
    val (left, right) = input.asLines()
    return parsePacket(left) to parsePacket(right)
}

private fun parsePacket(input: String): ListMember {
    val deque = ArrayDeque<ListMember>()
    var curNumberString = ""

    var out = ListMember()

    fun pushNumber() {
        if (curNumberString.isEmpty()) return
        val number = curNumberString.toInt()
        deque.first().add(IntMember(number))
        curNumberString = ""
    }

    fun startNew() {
        deque.add(0, ListMember())
    }

    fun end() {
        pushNumber()
        val oldFirst = deque.removeFirst()
        deque.firstOrNull()?.add(oldFirst)
        out = oldFirst
    }

    input.forEach {
        when (it) {
            '[' -> startNew()
            ']' -> end()
            ',' -> pushNumber()
            else -> curNumberString += it
        }
    }

    return out
}

private fun part1(input: String): SolutionType {
    val pairs = parsePairs(input)

//    val four = pairs[3]
//    four.isValid()


    val values = pairs.mapIndexed { index, pair ->
        if (pair.isValid()) index + 1 else 0
    }
    return values.sum()
    //6499 too high

    return defaultSolution
}

private fun part2(input: String): SolutionType {
    val packets = input.replace("\n\n", "\n")
        .asLines().map(::parsePacket).toMutableList()

    val div1 = ListMember(mutableListOf(ListMember(mutableListOf(IntMember(2)))))
    val div2 = ListMember(mutableListOf(ListMember(mutableListOf(IntMember(6)))))

    packets.add(div1)
    packets.add(div2)

    val sortedPackets = packets.sorted()

    val di1 = sortedPackets.indexOf(div1) + 1
    val di2 = sortedPackets.indexOf(div2) + 1


    return di1 * di2
}


fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)

    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
