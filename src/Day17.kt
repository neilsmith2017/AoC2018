import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

enum class GroundType(val printCharacter: Char) {
    CLAY('#'),
    SAND('.'),
    SPRING('+'),
    WATER('~'),
    WET_SAND('|')
}

data class Coord(val x: Int, val y: Int)

class Day17 {

    var groundMap = arrayListOf<Array<GroundType>>()
    var xShift = 0
    var minInputY = Int.MAX_VALUE
    var maxInputY = 0

    val coords: Deque<Coord> = ArrayDeque()

    fun printGrid() {
        groundMap.forEach { row ->
            row.forEach { sq -> print(sq.printCharacter) }
            println()
        }
        println()
    }

    private fun xValue(x: Int): Int = x - xShift

    fun loadData(fileName: String) {
        val dr = "([x-y])=([0-9]*), ([x-y])=(.*)".toRegex()

        val ranges = mutableListOf<Pair<IntRange, IntRange>>()

        File(fileName).readLines().forEach {
            dr.matchEntire(it)
                ?.destructured
                ?.let { (first, point, second, range) ->
                    val xRange: IntRange =
                        if (first == "y") range.toIntRange() else IntRange(point.toInt(), point.toInt())
                    val yRange: IntRange =
                        if (first == "x") range.toIntRange() else IntRange(point.toInt(), point.toInt())

                    ranges.add(Pair(xRange, yRange))
                }
        }

        val maxY = ranges.maxBy { r -> r.second.endInclusive }?.second?.endInclusive ?: 0
        xShift = (ranges.minBy { r -> r.first.start }?.first?.start ?: 0) - 1
        val maxX = (ranges.maxBy { r -> r.first.endInclusive }?.first?.endInclusive ?: 0) + 2


        if (maxY > maxInputY) maxInputY = maxY
        val minY = ranges.minBy { r -> r.second.start }?.second?.start ?: Int.MAX_VALUE
        if (minY < minInputY) minInputY = minY

        repeat(maxY + 1) {
            groundMap.add(Array(xValue(maxX)) { GroundType.SAND })
        }

        ranges.forEach { outerPair ->
            outerPair.first.forEach { x ->
                outerPair.second.forEach { y ->
                    groundMap[y][xValue(x)] = GroundType.CLAY
                }
            }
        }

        groundMap[0][xValue(500)] = GroundType.SPRING
    }

    fun processData() {
        coords.add(Coord(xValue(500), 0))
//        var count = 0

        fun addPoint(x: Int, y: Int) {
            if (y < groundMap.size - 1) {
                coords.add(Coord(x, y))
            }
        }
        while (coords.isNotEmpty()) {
//            if (count++ == 500) {
//                printGrid()
//            }
            val point = coords.pop()
            val nextPoint = Coord(point.x, point.y + 1)
            when (groundMap[nextPoint.y][nextPoint.x]) {

                GroundType.CLAY, GroundType.WATER -> {
                    val maxLeft = goLeft(point)
                    val maxRight = goRight(point)

                    if (groundMap[maxLeft.y + 1][maxLeft.x] == GroundType.SAND || groundMap[maxLeft.y + 1][maxLeft.x] == GroundType.WET_SAND ||
                        groundMap[maxRight.y + 1][maxRight.x] == GroundType.SAND || groundMap[maxRight.y + 1][maxRight.x] == GroundType.WET_SAND
                    ) {
                        if (groundMap[maxLeft.y + 1][maxLeft.x] == GroundType.SAND) {
                            coords.add(maxLeft)
                            groundMap[maxLeft.y][maxLeft.x] = GroundType.WET_SAND
                        }
                        if (groundMap[maxRight.y + 1][maxRight.x] == GroundType.SAND) {
                            coords.add(maxRight)
                            groundMap[maxRight.y][maxRight.x] = GroundType.WET_SAND
                        }
                        for (i in maxLeft.x + 1 until maxRight.x) {
                            groundMap[maxLeft.y][i] = GroundType.WET_SAND
                        }
                    } else {
                        for (i in maxLeft.x + 1 until maxRight.x) {
                            groundMap[maxLeft.y][i] = GroundType.WATER
                        }
//                        if (groundMap[maxLeft.y][maxLeft.x] == GroundType.CLAY && groundMap[maxLeft.y][maxLeft.x] == GroundType.CLAY) {
                        addPoint(point.x, point.y - 1)
//                        }
                    }
                }
                GroundType.SAND -> {
                    groundMap[nextPoint.y][nextPoint.x] = GroundType.WET_SAND
                    addPoint(nextPoint.x, nextPoint.y)
                }
                GroundType.SPRING, GroundType.WET_SAND -> {
                }
            }
        }
    }

    private fun goLeft(point: Coord): Coord {
        var x = point.x

        do {
            x--
        } while ((groundMap[point.y][x] == GroundType.SAND || groundMap[point.y][x] == GroundType.WET_SAND) && (groundMap[point.y + 1][x] == GroundType.CLAY || groundMap[point.y + 1][x] == GroundType.WATER))

        return Coord(x, point.y)
    }

    private fun goRight(point: Coord): Coord {
        var x = point.x

        do {
            x++
        } while ((groundMap[point.y][x] == GroundType.SAND || groundMap[point.y][x] == GroundType.WET_SAND) && (groundMap[point.y + 1][x] == GroundType.CLAY || groundMap[point.y + 1][x] == GroundType.WATER))

        return Coord(x, point.y)
    }

    fun countWater(): Int {
        return (minInputY..maxInputY)
            .map { y -> groundMap[y] }
            .sumBy { x -> x.filter { v -> v == GroundType.WATER || v == GroundType.WET_SAND }.count() }
    }

    fun countJustWater(): Int {
        return (minInputY..maxInputY)
            .map { y -> groundMap[y] }
            .sumBy { x -> x.filter { v -> v == GroundType.WATER }.count() }
    }
}

private fun String.toIntRange(): IntRange {
    val startEnd = this.split("..")
    return IntRange(startEnd[0].toInt(), startEnd[1].toInt())
}

class Day17Test {

    lateinit var day17: Day17

    @Before
    fun setUp() {
        day17 = Day17()
    }

    @Test
    fun runBigPart2() {
        day17.loadData("Data/Day17/day17-input.txt")
        day17.processData()
        day17.printGrid()
        assertEquals(27086, day17.countJustWater())
        // 34780 too big
    }

    @Test
    fun check2ProcessSmall() {
        day17.loadData("Data/Day17/day17-small.txt")
        day17.processData()
        day17.printGrid()
        assertEquals(29, day17.countJustWater())
    }

    @Test
    fun runBigPart1() {
        day17.loadData("Data/Day17/day17-input.txt")
        day17.processData()
        day17.printGrid()
        assertEquals(34775, day17.countWater())
        // 34780 too big
    }

    @Test
    fun checkProcessSmall() {
        day17.loadData("Data/Day17/day17-small.txt")
        day17.processData()
        day17.printGrid()
        assertEquals(57, day17.countWater())
    }

    @Test
    fun checkLoadTest1() {
        day17.loadData("Data/Day17/day17-test-1.txt")
        day17.printGrid()
//        assertEquals(499, day17.xShift)
//        assertEquals(8, day17.groundMap.size)
        day17.processData()
        day17.printGrid()
        assertEquals(18, day17.countWater())
    }

    @Test
    fun checkLoadBig() {
        day17.loadData("Data/Day17/day17-input.txt")
        day17.printGrid()
        assertEquals(292, day17.xShift)
        assertEquals(1869, day17.groundMap.size)
    }

    @Test
    fun checkLoad() {
        day17.loadData("Data/Day17/day17-small.txt")
        day17.printGrid()
        assertEquals(494, day17.xShift)
        assertEquals(14, day17.groundMap.size)
    }
}