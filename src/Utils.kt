import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.Exception

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun String.asLines() = split("\n")
fun String.asParts() = split("\n\n")

abstract class Challenge<SolutionType : Any>(
    val day: String,
    val testSolution1: SolutionType? = null,
    val testSolution2: SolutionType? = null
) {
    abstract fun Context.part1(): SolutionType
    abstract fun Context.part2(): SolutionType


    fun trySolve() {
        val testContext = Context("${day}_test.txt")

        try {
            val solution = testContext.part1()
            if (testSolution1 == null) {
                println("Test 1 finished: $solution")
            } else if (solution == testSolution1) {
                println("Test 1 passed: $solution")
            } else {
                println("Test 1 failed: $solution, expected: $testSolution1")
            }
        } catch (ex: Exception) {
            println("Test 1 failed to run")
            throw ex
        }

        try {
            val solution = testContext.part2()
            if (testSolution2 == null) {
                println("Test 2 finished: $solution")
            } else if (solution == testSolution2) {
                println("Test 2 passed: $solution")
            } else {
                println("Test 2 failed: $solution, expected: $testSolution2")
            }
        } catch (ex: Exception) {
            println("Test 2 failed to run")
            throw ex
        }


        val liveFileContext = Context("${day}.txt")
        try {
            println(liveFileContext.part1())
        } catch (ex: Exception) {
            println("Failed part 1")
            throw ex
        }

        try {
            println(liveFileContext.part2())
        } catch (ex: Exception) {
            println("Failed part 2")
            throw ex
        }


    }

    inner class Context(fileName: String) {
        private val file = File("src", fileName)
        val fileContents get() = file.readText()
        val fileLines get() = file.readLines()
    }
}