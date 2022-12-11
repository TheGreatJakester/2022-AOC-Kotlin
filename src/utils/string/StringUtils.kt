package utils.string

import java.math.BigInteger
import java.security.MessageDigest


const val blockChar = "▌"
const val fullBlockChar = "█"

fun String.asLines() = split("\n")
fun String.asParts() = split("\n\n")

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


