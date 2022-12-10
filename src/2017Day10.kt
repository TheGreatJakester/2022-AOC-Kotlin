package day2017_10

import utils.runSolver

private typealias SolutionType = Int

private const val defaultSolution = 0

private const val dayNumber: String = "10"
private val testSolution1: SolutionType? = 88
private val testSolution2: SolutionType? = 36

class Node(val number: Int, var node: Node? = null) {
    fun getNodeAt(index: Int): Node {
        if (index == 0) return this
        return node!!.getNodeAt(index - 1)
    }

    fun getCut(length: Int) = getCut(length, mutableListOf())
    private fun getCut(
        length: Int,
        mutableList: MutableList<Node>
    ): List<Node> {
        if (length > 0) {
            mutableList.add(this)
            return node!!.getCut(length - 1, mutableList)
        }
        return mutableList
    }

    fun reverse(previous: Node, newLast: Node, length: Int){

    }

    override fun toString(): String = number.toString()

    private fun toRecursiveString(start: Node = this): String {
        val n = node ?: return number.toString()
        if (node == start) return number.toString()
        return number.toString() + ", " + n.toRecursiveString(start)
    }
}

private fun part1(input: String): SolutionType {
    val firstNode = Node(0)
    var curNode = firstNode
    repeat(254) {
        val next = Node(it + 1)
        curNode.node = next
        curNode = next
    }
    curNode.node = firstNode

    curNode = firstNode

    val cut = firstNode.getCut(4)
    println(cut)

    return defaultSolution
}

private fun part2(input: String): SolutionType {
    return defaultSolution
}

fun main() {
    val input = "97,167,54,178,2,11,209,174,119,248,254,0,255,1,64,190"

    runSolver("Part 1", input, null, ::part1)
    runSolver("Part 2", input, null, ::part2)
}
