import org.junit.Test
import kotlin.test.assertEquals

class Day11(private val serialNumber: Int) {

    private val grid = Array(301) {
        Array(301) {
            0
        }
    }

    fun getPowerLevel(x: Int, y: Int): Int {
        val rackId = x + 10
        val powerLevel = (((rackId * y) + serialNumber) * rackId)
        val hundredsDigit = if (powerLevel > 99) (powerLevel / 100) % 10 else 0
        return hundredsDigit - 5
    }

    fun populateGrid() {
        for (x in 1..300) {
            for (y in 1..300) {
                grid[x][y] = getPowerLevel(x, y)
            }
        }
    }

    fun getHighestValue(): Pair<Int, Int> {

        var currentHighest = Int.MIN_VALUE
        var highestCoord = Pair(0, 0)

        for (y in 1..298) {
            for (x in 1..298) {
                val squareSum =
                    grid[x][y] + grid[x + 1][y] + grid[x + 2][y] +
                            grid[x][y + 1] + grid[x + 1][y + 1] + grid[x + 2][y + 1] +
                            grid[x][y + 2] + grid[x + 1][y + 2] + grid[x + 2][y + 2]
                if (squareSum > currentHighest) {
                    currentHighest = squareSum
                    highestCoord = Pair(x, y)
                }
            }
        }
        println("Higest Value = $currentHighest")
        return highestCoord
    }

    fun getHighestValueOfAnySizeSubGrid(): Pair<Int, Int> {
        var currentHighest = Int.MIN_VALUE
        var highestCoord = Pair(0, 0)
        var highestGridSize = 0


        for (z in 1..300) {
            for (y in 1..(301 - z)) {
                for (x in 1..(301 - z)) {

                    var squareSum = 0
                    for (i in x until x + z) {
                        for (j in y until y + z) {
                            squareSum += grid[i][j]
                        }
                    }

                    if (squareSum > currentHighest) {
                        currentHighest = squareSum
                        highestCoord = Pair(x, y)
                        highestGridSize = z
                    }
                }
            }
        }

        println("Highest Value = $currentHighest with grid size $highestGridSize")
        return highestCoord


    }
}

class Day11test {

    lateinit var day11: Day11

    @Test
    fun getHighestSubGridBig() {
        day11 = Day11(1308)
        day11.populateGrid()
        assertEquals(Pair(227, 199), day11.getHighestValueOfAnySizeSubGrid())
    }

    @Test
    fun getHighestSubGrid2() {
        day11 = Day11(42)
        day11.populateGrid()
        assertEquals(Pair(232, 251), day11.getHighestValueOfAnySizeSubGrid())
    }

    @Test
    fun getHighestSubGrid1() {
        day11 = Day11(18)
        day11.populateGrid()
        assertEquals(Pair(90, 269), day11.getHighestValueOfAnySizeSubGrid())
    }

    @Test
    fun getHighestBig() {
        day11 = Day11(1308)
        day11.populateGrid()
        assertEquals(Pair(21, 41), day11.getHighestValue())
    }

    @Test
    fun getHighest2() {
        day11 = Day11(42)
        day11.populateGrid()
        assertEquals(Pair(21, 61), day11.getHighestValue())
    }

    @Test
    fun getHighest1() {
        day11 = Day11(18)
        day11.populateGrid()
        assertEquals(Pair(33, 45), day11.getHighestValue())
    }

    @Test
    fun populateGrid1() {
        day11 = Day11(18)
        day11.populateGrid()
    }

    @Test
    fun checkGetPowerLevel1() {
        day11 = Day11(8)
        assertEquals(4, day11.getPowerLevel(3, 5))
    }

    @Test
    fun checkGetPowerLevel2() {
        day11 = Day11(57)
        assertEquals(-5, day11.getPowerLevel(122, 79))
    }

    @Test
    fun checkGetPowerLevel3() {
        day11 = Day11(39)
        assertEquals(0, day11.getPowerLevel(217, 196))
    }

    @Test
    fun checkGetPowerLevel4() {
        day11 = Day11(71)
        assertEquals(4, day11.getPowerLevel(101, 153))
    }

}