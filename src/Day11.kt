package day11

import utils.readInputAsText
import utils.runSolver
import utils.string.asLines
import utils.string.asParts

private typealias SolutionType = Long

private const val defaultSolution = 0

private const val dayNumber: String = "11"
private val testSolution1: SolutionType? = 10605
private val testSolution2: SolutionType? = 2713310158


class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    // True is multiplication, false is addition
    val operation: Boolean,
    val operationValue: Long?,

    val testValue: Int,

    val trueMonkeyId: Int,
    val falseMonkeyId: Int
) {

    var monkeyList: List<Monkey>? = null
        set(value) {
            field = value
            universalLimit = value?.map { it.testValue.toLong() }?.reduce { acc, i -> acc * i }!!
        }

    private var universalLimit = 0L

    val trueMonkey get() = monkeyList?.first { it.id == trueMonkeyId }!!
    val falseMonkey get() = monkeyList?.first { it.id == falseMonkeyId }!!


    fun doOperation(operating: Long): Long {
        val operationValue = operationValue ?: operating

        return if (operation) {
            operating * operationValue
        } else {
            operating + operationValue
        }
    }

    fun doTest(testing: Long): Boolean {
        return testing % testValue == 0L
    }

    var count: Long = 0
        private set

    private fun handleOneItem(item: Long) {
        count++
        val newItem = doOperation(item) % universalLimit
        val test = doTest(newItem)
        if (test) {
            trueMonkey.items.add(newItem)
        } else {
            falseMonkey.items.add(newItem)
        }
    }

    fun doRound() {
        items.forEach { handleOneItem(it) }
        items.clear()
    }

    companion object {
        var worryDivider = 1
    }
}


fun parseMonkeys(input: String): List<Monkey> {
    val mStrings = input.asParts()

    fun String.lastInt(): Int? = try {
        this.split(" ").last().toInt()
    } catch (ex: Exception) {
        null
    }

    val monkeys = mStrings.map {
        val lines = it.asLines()
        val (idLine, itemLine, opLine, testLine) = lines.take(4)
        val (trueLine, falseLine) = lines.takeLast(2)

        val id = idLine.split(" ")[1].dropLast(1).toInt()
        val items = itemLine.split(": ").last().trim().replace(" ", "")
            .split(",").map { it.toLong() }

        val opChar = opLine.split(" ")[6]
        val op = opChar == "*"
        val opVal = opLine.lastInt()?.toLong()

        val testVal = testLine.lastInt()
        val truth = trueLine.lastInt()
        val falsy = falseLine.lastInt()

        Monkey(
            id = id,
            items = items.toMutableList(),
            operation = op,
            operationValue = opVal,
            testValue = testVal!!,
            trueMonkeyId = truth!!,
            falseMonkeyId = falsy!!
        )
    }

    monkeys.forEach { it.monkeyList = monkeys }
    return monkeys
}

private fun part1(input: String): SolutionType {
    Monkey.worryDivider = 3
    val monkeys = parseMonkeys(input)

    repeat(20) {
        monkeys.forEach { it.doRound() }
    }

    val (m1, m2) = monkeys.sortedBy { it.count }.takeLast(2)
    return m1.count * m2.count
}

private fun part2(input: String): SolutionType {
    Monkey.worryDivider = 1
    val monkeys = parseMonkeys(input)

    fun round() = monkeys.forEach { it.doRound() }

    repeat(10_000) {
        round()
    }

    val (m1, m2) = monkeys.sortedBy { it.count }.takeLast(2)
    return m1.count * m2.count
}

//10592276768 too low

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)

    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}
