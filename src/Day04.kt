fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            it.split(",")
        }.filter { (elf1, elf2) ->
            val (a, b) = elf1.split("-")
            val (c, d) = elf2.split("-")

            if (a.toInt() <= c.toInt() && b.toInt() >= d.toInt()) {
                return@filter true
            }

            if (c.toInt() <= a.toInt() && d.toInt() >= b.toInt()) {
                return@filter true
            }

            false
        }.count()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split(",")
        }.filter { (elf1, elf2) ->
            val (elf1min, elf1max) = elf1.split("-").map { it.toInt() }
            val (elf2min, elf2max) = elf2.split("-").map { it.toInt() }

            val elf1Range = elf1min..elf1max
            val elf2Range = elf2min..elf2max

            return@filter elf1min in elf2Range ||
                    elf1max in elf2Range ||
                    elf2min in elf1Range ||
                    elf2max in elf1Range

        }.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val pt2 = part2(testInput)
    println(pt2)
    check(pt2 == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

