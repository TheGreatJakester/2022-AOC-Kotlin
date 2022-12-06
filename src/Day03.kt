fun String.firstHalf(): String {
    return this.substring(0, this.length / 2)
}

fun String.secondHalf(): String {
    return this.substring(this.length / 2, this.length)
}

fun scoreLetter(letter: Char): Int {
    if (letter in 'A'..'Z') {
        return letter.code - 'A'.code + 27
    } else if (letter in 'a'..'z') {
        return letter.code - 'a'.code + 1
    } else {
        throw Exception("Missed")
    }
}


fun main() {
    fun part1(input: List<String>): Int {
        val letters = input.mapNotNull {
            val first = it.firstHalf()
            val last = it.secondHalf()

            first.firstOrNull { letter -> last.contains(letter) }
        }

        val scroes = letters.map(::scoreLetter)

        return scroes.sum()
    }

    fun part2(input: List<String>): Int {
        val mutableInput = mutableListOf<String>().apply {
            addAll(input)
        }

        var sum = 0

        while (mutableInput.isNotEmpty()) {
            val (elf1, elf2, elf3) = mutableInput.take(3)
            repeat(3) {
                mutableInput.removeAt(0)
            }

            val shared = elf1.filter { elf2.contains(it) }.first { elf3.contains(it) }

            sum += scoreLetter(shared)
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsLines("Day03_test")
    check(part1(testInput) == 157)

    val input = readInputAsLines("Day03")
    println(part1(input))
    println(part2(input))
}

