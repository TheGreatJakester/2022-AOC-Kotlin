typealias SolutionType = Int

private val dayNumber: String = "07"
private val testSolution1: SolutionType = 95437
private val testSolution2: SolutionType? = 24933642


interface ElfItem {
    val size: Int
}

class ElfFile(val name: String, override val size: Int) : ElfItem
class ElfDir(val name: String, val parent: ElfDir? = null, val contents: MutableList<ElfItem> = mutableListOf()) :
    ElfItem {
    override val size: Int get() = contents.sumOf { it.size }


    fun fillWithSmall(maxSize: Int, dirSet: MutableSet<ElfDir> = mutableSetOf()): Set<ElfDir> {
        val all = contents.filterIsInstance<ElfDir>()
        val smalls = all.filter { it.size < maxSize }
        dirSet.addAll(smalls)
        all.forEach { it.fillWithSmall(maxSize, dirSet) }
        return dirSet
    }

    fun fillWith(dirSet: MutableSet<ElfDir> = mutableSetOf()): Set<ElfDir> {
        val all = contents.filterIsInstance<ElfDir>()
        dirSet.addAll(all)
        all.forEach { it.fillWith(dirSet) }
        return dirSet
    }

    fun addDir(name: String) {
        contents.add(ElfDir(name, this))
    }

    fun addFile(name: String, size: Int) {
        contents.add(ElfFile(name, size))
    }
}


fun part1(input: String): SolutionType {
    val commandsInput = input.asLines()
    val commands = commandsInput.subList(1, commandsInput.size)
    val startDir = ElfDir("/")
    var currentDir = startDir

    commands.forEach {
        if (it == "$ ls") {
            // No op?
        } else if (it.contains("dir")) {
            val (_, dirName) = it.split(" ")
            currentDir.addDir(dirName)
        } else if (it.contains("cd")) {
            val (_, _, destination) = it.split(" ")
            currentDir = if (destination == "..") {
                currentDir.parent!!
            } else {
                currentDir.contents
                    .filterIsInstance<ElfDir>()
                    .first { it.name == destination }
            }

        } else { // must be a file
            val (fileSize, fileName) = it.split(" ")
            currentDir.addFile(fileName, fileSize.toInt())
        }
    }

    val smalls = startDir.fillWithSmall(100000)
    return smalls.sumOf { it.size }
}

fun part2(input: String): SolutionType {
    val commandsInput = input.asLines()
    val commands = commandsInput.subList(1, commandsInput.size)
    val startDir = ElfDir("/")
    var currentDir = startDir

    commands.forEach {
        if (it == "$ ls") {
            // No op?
        } else if (it.contains("dir")) {
            val (_, dirName) = it.split(" ")
            currentDir.addDir(dirName)
        } else if (it.contains("cd")) {
            val (_, _, destination) = it.split(" ")
            currentDir = if (destination == "..") {
                currentDir.parent!!
            } else {
                currentDir.contents
                    .filterIsInstance<ElfDir>()
                    .first { it.name == destination }
            }

        } else { // must be a file
            val (fileSize, fileName) = it.split(" ")
            currentDir.addFile(fileName, fileSize.toInt())
        }
    }



    val totalSize = startDir.size
    val neededSpace = 30_000_000 - (70_000_000 - totalSize)

    val all = startDir.fillWith()



    return all.sortedBy { it.size }.first { it.size > neededSpace }.size
}

fun main() {
    runSolver("Test 1", readInputAsText("Day${dayNumber}_test"), testSolution1, ::part1)
    runSolver("Test 2", readInputAsText("Day${dayNumber}_test"), testSolution2, ::part2)

    runSolver("Part 1", readInputAsText("Day${dayNumber}"), null, ::part1)
    runSolver("Part 2", readInputAsText("Day${dayNumber}"), null, ::part2)
}

