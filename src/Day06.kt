import java.lang.Exception
import java.util.Deque

fun main() {
    object : Challenge<Int>("Day06", null, null) {
        override fun Context.part1(): Int {
            val text = fileContents

            val last4 = ArrayDeque<Char>()

            repeat(4) {
                last4.addFirst(' ')
            }

            fun allDiff(): Boolean {
                if (last4.size < 4) return false
                val set = last4.toSet()
                return set.size == 4
            }

            var index = 0

            while (!allDiff() || index < 4) {
                val next = text[index]
                last4.addFirst(next)
                last4.removeLast()
                index += 1
            }

            return index
        }

        override fun Context.part2(): Int {
            val text = fileContents

            val last4 = ArrayDeque<Char>()

            repeat(14) {
                last4.addFirst(' ')
            }

            fun allDiff(): Boolean {
                if (last4.size < 14) return false
                val set = last4.toSet()
                return set.size == 14
            }

            var index = 0

            while (!allDiff() || index < 14) {
                val next = text[index]
                last4.addFirst(next)
                last4.removeLast()
                index += 1
            }

            return index
        }


    }.trySolve()
}