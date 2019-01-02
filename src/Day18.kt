import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class Day18 {

    var ground = ""
    var rowLength = 0

    fun loadData(fileName: String) {
        File(fileName).readLines().forEach { r ->
            if (rowLength == 0) rowLength = r.length
            ground += r
        }
    }

    fun print() {
        for (i in 1..ground.length) {
            print(ground[i - 1])
            if (i % rowLength == 0) println()
        }
        println()
    }

    fun countType(c: Char): Int {
        return ground.filter { it == c }.count()
    }

    fun calcPart1(): Int = countType('#') * countType('|')

    fun process(iterations: Int) {
        var repeatingInterval = 0
        val knownStates = mutableMapOf<String, Int>()
        repeat(iterations) {
//            if (it > 20000) {
//                print()
//                println("${calcPart1()}  #=${countType('#')}     |=${countType('|')}")
//            }
            var newGround = ""
            for (i in 0 until ground.length) {
                newGround += when (ground[i]) {
                    '.' -> if (getNeighbourCount(i, '|') >= 3) '|' else '.'
                    '|' -> if (getNeighbourCount(i, '#') >= 3) '#' else '|'
                    '#' -> if (getNeighbourCount(i, '#') >= 1 && getNeighbourCount(i, '|') >= 1) '#' else '.'
                    else -> throw IllegalArgumentException()
                }
            }
            if (ground == newGround) throw IllegalArgumentException("finished $it")
            ground = newGround

            if (knownStates.contains(ground)) {
                repeatingInterval = it - (knownStates[ground] ?:0)
                println("Repeating Interval $repeatingInterval")
                if ((1000000000 - it - 1) % repeatingInterval == 0) return
            }
            knownStates[ground] = it
        }
    }

    private fun getNeighbourCount(pos: Int, c: Char): Int {
        val x = pos % rowLength
        val y = pos / rowLength

        var neighbourCount = 0

        if (x > 0 && y > 0) neighbourCount += if (ground[((y - 1) * rowLength) + x - 1] == c) 1 else 0          // -1,-1
        if (y > 0) neighbourCount += if (ground[((y - 1) * rowLength) + x] == c) 1 else 0          // 0,-1
        if (x < rowLength - 1 && y > 0) neighbourCount += if (ground[((y - 1) * rowLength) + x + 1] == c) 1 else 0   // 1,-1

        if (x > 0) neighbourCount += if (ground[(y * rowLength) + x - 1] == c) 1 else 0                      // -1,0
        if (x < rowLength - 1) neighbourCount += if (ground[(y * rowLength) + x + 1] == c) 1 else 0              // 1,0

        if (x > 0 && y < rowLength - 1) neighbourCount += if (ground[((y + 1) * rowLength) + x - 1] == c) 1 else 0      // -1,1
        if (y < rowLength - 1) neighbourCount += if (ground[((y + 1) * rowLength) + x] == c) 1 else 0      // 0,1
        if (x < rowLength - 1 && y < rowLength - 1) neighbourCount += if (ground[((y + 1) * rowLength) + x + 1] == c) 1 else 0      // 1,1

        return neighbourCount
    }

}


class Day18Test {

    lateinit var day18: Day18

    @Before
    fun setUp() {
        day18 = Day18()
    }

    @Test
    fun checkProcesspart2() {
        day18.loadData("Data/Day18/day18-big.txt")
        day18.process(1000000000)
        day18.print()
        assertEquals(207998, day18.calcPart1())
        // 201373 too low
    }

    @Test
    fun checkProcesspart1() {
        day18.loadData("Data/Day18/day18-big.txt")
        day18.process(10)
        day18.print()
        assertEquals(466125, day18.calcPart1())
    }

    @Test
    fun checkProcess10() {
        day18.loadData("Data/Day18/day18-small.txt")
        day18.process(10)
        day18.print()
        assertEquals(1147, day18.calcPart1())
    }

    @Test
    fun checkProcess() {
        day18.loadData("Data/Day18/day18-small.txt")
        day18.print()
        day18.process(1)
        day18.print()
    }

    @Test
    fun checkLoad() {
        day18.loadData("Data/Day18/day18-small.txt")
        day18.print()
    }
}