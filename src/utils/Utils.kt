package utils

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
import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Duration
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*
import javax.naming.TimeLimitExceededException

/**
 * Reads lines from the given input txt file.
 */
fun readInputAsLines(name: String) = File("src", "$name.txt")
    .readLines()

fun readInputAsText(name: String) = File("src", "$name.txt").readText()


fun <T> List<List<T>>.forForEach(lambda: (x: Int, y: Int, el: T) -> Unit) {
    this.forEachIndexed { y, sub ->
        sub.forEachIndexed { x, el ->
            lambda(x, y, el)
        }
    }
}


fun <SolutionType, InputType> runSolver(
    runName: String,
    input: InputType,
    solutionCheck: SolutionType?,
    solver: (InputType) -> SolutionType,
) {
    try {
        val solution = solver(input)
        if (solutionCheck == null) {
            println("$runName Finished: $solution")
        } else if (solution == solutionCheck) {
            println("$runName passed with: $solution")
        } else {
            println("$runName failed, got: $solution, expected: $solutionCheck")
        }
    } catch (ex: Exception) {
        println("Can't run test 1")
        throw ex
    }
}


// Milliseconds after release time that we will wait, before trying to grab input.
private const val WAIT_MIN = 900L

// Will pick and random value from (0, WAIT_MUL) and add that to the wait time above, to limit blocking in case
// other people are running this or similar schemes from similar locations.
private const val WAIT_MUL = 500.0

// Seconds under which we switch into wait and pounce mode.
private const val DELAY_TIME = 120L

suspend fun checkOrGetInput(year: Int = 2022, day: Int, dataDir: File = File("src")): String {
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
    suspend fun grabInput(year: Int, day: Int): String {
        val data: String
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