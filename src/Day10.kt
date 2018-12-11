import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

data class Point(var x: Int, var y: Int, val xOff: Int, val yOff: Int)

class Day10 {

    val points = mutableListOf<Point>()
    var minX = 0
    var maxX = 0
    var minY = 0
    var maxY = 0

    fun loadData(filename: String) {
        val dr = "^position=< *(-?[0-9]+), *(-?[0-9]+)> velocity=< *(-?[0-9]+), *(-?[0-9]+)>.*".toRegex()
        File(filename).readLines().forEach {
            points.add(dr.matchEntire(it)
                ?.destructured
                ?.let { (x, y, xOff, yOff) ->
                    val ss = 0
                    Point(x.toInt(), y.toInt(), xOff.toInt(), yOff.toInt())
                }
                ?: throw IllegalArgumentException("Invalid Input"))
        }
    }

    fun calculateBounds() {
        minX = points.map { p -> p.x }.min() ?: 0
        minY = points.map { p -> p.y }.min() ?: 0
        maxX = points.map { p -> p.x }.max() ?: 0
        maxY = points.map { p -> p.y }.max() ?: 0
    }

    fun drawStars() {
        val sky = Array(maxY - minY + 1) {
            Array(maxX - minX + 1) { false }
        }

        points.forEach { p ->
            sky[p.y - minY][p.x - minX] = true
        }

        sky.forEach { y ->
            y.forEach { x -> if (x) print("*") else print(".") }
            println()
        }
    }

    fun drawStarsWithoutArray() {
        calculateBounds()
        if (maxX - minX > 100 || maxY - minY > 100) return

        val pointsMap = points.map { it -> Pair(it.x, it.y) }.toSet()

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (pointsMap.contains(Pair(x, y))) print("#") else print(".")
            }
            println()
        }

    }

    fun moveStars() {
        points.forEach { p ->
            p.x += p.xOff
            p.y += p.yOff
        }
    }
}

class Day10Test {

    private lateinit var day10: Day10

    @Before
    fun setUp() {
        day10 = Day10()
    }

    @Test
    fun checkDrawAndIterateBig() {
        day10.loadData("Data/Day10/day10-big.txt")
        day10.calculateBounds()
        day10.drawStarsWithoutArray()
        for (i in 1..15000) {
            if (i > 10000) println("After $i seconds")
            day10.moveStars()
            day10.drawStarsWithoutArray()
        }
    }


    @Test
    fun checkDrawAndIterate2() {
        day10.loadData("Data/Day10/day10-small.txt")
        day10.calculateBounds()
        day10.drawStarsWithoutArray()
        for (i in 1..4) {
            println("After $i seconds")
            day10.moveStars()
            day10.drawStarsWithoutArray()
        }
    }


    @Test
    fun checkDrawAndIterate() {
        day10.loadData("Data/Day10/day10-small.txt")
        day10.calculateBounds()
        day10.drawStars()
        for (i in 1..4) {
            println("After $i seconds")
            day10.moveStars()
            day10.drawStars()
        }
    }

    @Test
    fun checkDraw() {
        day10.loadData("Data/Day10/day10-small.txt")
        day10.calculateBounds()
        day10.drawStars()
    }

    @Test
    fun checkRange() {
        day10.loadData("Data/Day10/day10-small.txt")
        day10.calculateBounds()
        assertEquals(-6, day10.minX)
        assertEquals(-4, day10.minY)
        assertEquals(15, day10.maxX)
        assertEquals(11, day10.maxY)
    }

    @Test
    fun checkLoad() {
        day10.loadData("Data/Day10/day10-small.txt")
        assertEquals(31, day10.points.size)
    }
}