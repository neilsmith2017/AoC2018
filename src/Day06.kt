import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.test.assertEquals

data class Points(var infinite: Boolean = false, var locations: Int = 0)

class Day06 {

    val coords = mutableMapOf<Pair<Int, Int>, Points>()

    var top = 0
    var bottom = 0
    var left = 0
    var right = 0

    fun loadData(filename: String) {
        File(filename).readLines().forEach {
            val p = it.split(", ")
            coords.put(Pair(p[0].toInt(), p[1].toInt()), Points())
        }
    }

    fun setBounds() {
        top = coords.minBy { it.key.second }?.key?.second ?: 0
        bottom = coords.maxBy { it.key.second }?.key?.second ?: 0
        left = coords.minBy { it.key.first }?.key?.first ?: 0
        right = coords.maxBy { it.key.first }?.key?.first ?: 0
    }

    fun processBoard(): Int {
        for (y in top..bottom) {
            println()
            for (x in left..right) {
                if (coords.contains(Pair(x, y)) && (x == left || x == right || y == top || y == bottom)) {
                    coords[Pair(x, y)]?.infinite = true
                    print("x")
                } else if (x in left + 1 until right && y in top + 1 until bottom) {
                    getNearestPoints(x, y)
                } else {
                    print(".")
                }
            }
        }
        return coords.filter { !it.value.infinite }.maxBy { it.value.locations }?.value?.locations ?: -1
    }

    fun getNearestPoints(x: Int, y: Int) {
        var minDistance = coords.minBy { it.key.mDistance(x, y) }?.key?.mDistance(x, y)
        var closestPoints = coords.filter { it.key.mDistance(x, y) == minDistance }
//        if (closestPoints.size == 1 && minDistance ?: 0 <= closestEdgeDistance(closestPoints.keys.first()) + 1) {
        if (closestPoints.size == 1 && getOwnerOfClosestEdge(x, y) != closestPoints.keys.first()) {
            val c = coords.get(closestPoints.keys.first())
            if (c != null) {
                c.locations++
                print("*")
            }
        } else {
            print("+")
        }
    }

    fun getOwnerOfClosestEdge(x: Int, y: Int): Pair<Int, Int> {
        val edges = arrayListOf<Pair<Int, Int>>(
            Pair(x, top),
            Pair(x, bottom),
            Pair(left, y),
            Pair(right, y)
        ).minBy { it.mDistance(x, y) }
        if (edges != null) {
            val edgeOwner = coords.minBy { it.key.mDistance(edges.first, edges.second) }
            return edgeOwner?.key ?: throw IllegalArgumentException()
        }
        throw IllegalArgumentException()
    }

    fun processPart2(threshold: Int): Int {

        var numberOfPoints = 0

        for (y in top..bottom) {
            for (x in left..right) {

                val distanceForPoint = coords.map {
                    it.key.mDistance(x, y)
                }.sum()

                if (distanceForPoint < threshold) {
                    numberOfPoints++
                }
            }
        }
        return numberOfPoints
    }
}

fun Pair<Int, Int>.mDistance(x: Int, y: Int): Int {
    return abs(this.first - x) + abs(this.second - y)
}

class Day6Test {

    private lateinit var day6: Day06

    @Before
    fun setUp() {
        day6 = Day06()
    }

    @Test
    fun checkProcessBig2() {
        day6.loadData("Data/Day06/day6-big.txt")
        day6.setBounds()
        assertEquals(46306, day6.processPart2(10000))
    }

    @Test
    fun checkProcess2() {
        day6.loadData("Data/Day06/day6-small.txt")
        day6.setBounds()
        assertEquals(16, day6.processPart2(32))
    }

    @Test
    fun checkProcessBig() {
        day6.loadData("Data/Day06/day6-big.txt")
        day6.setBounds()
        assertEquals(4016, day6.processBoard())
    }

    @Test
    fun checkProcess() {
        day6.loadData("Data/Day06/day6-small.txt")
        day6.setBounds()
        assertEquals(17, day6.processBoard())
    }

    @Test
    fun checkBounds() {
        day6.loadData("Data/Day06/day6-small.txt")
        day6.setBounds()
        assertEquals(1, day6.top)
        assertEquals(1, day6.left)
        assertEquals(9, day6.bottom)
        assertEquals(8, day6.right)
    }

    @Test
    fun check() {
        day6.loadData("Data/Day06/day6-small.txt")
        assertEquals(6, day6.coords.size)
    }
}







