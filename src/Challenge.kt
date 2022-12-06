import java.io.File

@Deprecated("Cool, but I think this just adds overhead to solving.")
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