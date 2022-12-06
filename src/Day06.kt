fun main() {
    object : Challenge<Int>("Day06", null, null) {
        fun getFirstAllDifferent(count: Int, fileContents: String): Int{
            val text = fileContents.toList()

            val sizes = text.windowed(count){
                it.toSet().size
            }

            val index = sizes.indexOfFirst { it == count }
            return index + count
        }


        override fun Context.part1(): Int = getFirstAllDifferent(4, fileContents)

        override fun Context.part2(): Int = getFirstAllDifferent(14, fileContents)

    }.trySolve()
}