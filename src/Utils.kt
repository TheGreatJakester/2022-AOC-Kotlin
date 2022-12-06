import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.Exception
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.io.Closeable
import java.io.FileNotFoundException
import java.time.Duration
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*
import javax.naming.TimeLimitExceededException

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


// Milliseconds after release time that we will wait, before trying to grab input.
private const val WAIT_MIN = 900L
// Will pick and random value from (0, WAIT_MUL) and add that to the wait time above, to limit blocking in case
// other people are running this or similar schemes from similar locations.
private const val WAIT_MUL = 500.0
// Seconds under which we switch into wait and pounce mode.
private const val DELAY_TIME = 120L

suspend fun checkOrGetInput(year: Int, day: Int, dataDir: File) : String {
    val dayFileName = String.format("day%02d.txt", day)
    val dataFile = File(dataDir, dayFileName)
    if (dataFile.exists()) {
        return dataFile.readText()
    }
    val tokenFile = File(dataDir, "sessionToken.txt")
    if (!tokenFile.exists()) {
        throw FileNotFoundException("You don't have a day input, but you don't have a sessionToken.txt either.")
    }
    val token = tokenFile.readText()
    val est = ZoneOffset.ofHours(-5)
    val timeNowEST = ZonedDateTime.now().withZoneSameInstant(est)
    val timePuzzle = ZonedDateTime.of(year, 12, day, 0, 0, 0, 0, est)
    val difference = Duration.between(timeNowEST, timePuzzle)
    if (difference.seconds > DELAY_TIME) {
        throw TimeLimitExceededException("You can't time-travel, and it's too soon to wait to download the input.")
    }
    // We're committed to the download attempt
    println("Fetching...")
    val scraper = AoCWebScraper(token)
    if (difference.seconds > 0) {
        println("Waiting until puzzle is out...")
        delay(1000L * difference.seconds + WAIT_MIN + (Math.random() * WAIT_MUL).toLong())
    }
    val data = scraper.use {
        it.grabInput(year, day)
    }
    dataFile.writeText(data.dropLastWhile { it == '\n' })
    return data
}

class AoCWebScraper(private val sessionToken: String) : Closeable {

    private val client = HttpClient(OkHttp) {
        install(ContentEncoding) {
            deflate()
            gzip()
        }
    }

    @Throws(ResponseException::class)
    suspend fun grabInput(year: Int, day: Int) : String {
        val data : String
        val response = client.get("https://adventofcode.com/$year/day/$day/input") {
            headers {
                append(
                    "User-Agent",
                    "github.com/CognitiveGear/AdventOfCode-Kotlin by cogntive.gear@gmail.com"
                )
                append(
                    "cookie",
                    "session=$sessionToken"
                )
            }
        }
        when (response.status) {
            HttpStatusCode.Accepted, HttpStatusCode.OK -> data = response.body()
            else -> throw ResponseException(response, "AoC:: " + response.body())
        }
        return data
    }
    override fun close() {
        client.close()
    }
}