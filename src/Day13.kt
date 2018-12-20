import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

enum class Shapes {
    FORWARD_SLASH,
    HORIZONTAL,
    BACKWARD_SLASH,
    VERTICAL,
    JUNCTION,
    UP,
    DOWN,
    LEFT,
    RIGHT,
    SPACE
}

data class Cart(var x: Int, var y: Int, var direction: Shapes) : Comparable<Cart> {
    override fun compareTo(other: Cart): Int {
        return if (y == other.y) x - other.x
        else y - other.y
    }
}

class Day13 {

    val grid = mutableListOf<List<Shapes>>()
    val carts = mutableListOf<Cart>()

    var rowNumber = 0

    fun loadData(filename: String) {
        File(filename).readLines().forEach {
            val row = mutableListOf<Shapes>()
            for (x in 0 until it.length) {
                row.add(
                    when (it[x]) {
                        '<' -> {
                            carts.add(Cart(x, rowNumber, Shapes.LEFT))
                            Shapes.HORIZONTAL
                        }
                        '>' -> {
                            carts.add(Cart(x, rowNumber, Shapes.RIGHT))
                            Shapes.HORIZONTAL
                        }
                        '^' -> {
                            carts.add(Cart(x, rowNumber, Shapes.UP))
                            Shapes.VERTICAL
                        }
                        'v' -> {
                            carts.add(Cart(x, rowNumber, Shapes.DOWN))
                            Shapes.VERTICAL
                        }
                        '/' -> Shapes.FORWARD_SLASH
                        '-' -> Shapes.HORIZONTAL
                        '|' -> Shapes.VERTICAL
                        '+' -> Shapes.JUNCTION
                        '\\' -> Shapes.BACKWARD_SLASH
                        else -> Shapes.SPACE
                    }
                )
            }
            grid.add(row)
            rowNumber++
        }
    }

    fun doATick() {
        carts.sort()
        carts.forEach {
            println(grid[it.y][it.x])
        }
    }

}

class Day13Test {

    lateinit var day13: Day13

    @Before
    fun setUp() {
        day13 = Day13()
    }

    @Test
    fun testTickTiny() {
        day13.loadData("Data/Day13/day13-tiny.txt")
        day13.doATick()
    }

    @Test
    fun testSortBig() {
        day13.loadData("Data/Day13/day13-big.txt")
        println(day13.carts)
        println(day13.carts.sorted())
    }

    @Test
    fun testSortSmall() {
        day13.loadData("Data/Day13/day13-small.txt")
        println(day13.carts)
        println(day13.carts.sorted())
    }

    @Test
    fun testSortTiny() {
        day13.loadData("Data/Day13/day13-tiny.txt")
        println(day13.carts)
        println(day13.carts.sorted())
    }

    @Test
    fun testLoadTiny() {
        day13.loadData("Data/Day13/day13-tiny.txt")
        assertEquals(7, day13.grid.size)
    }

    @Test
    fun testLoadSmall() {
        day13.loadData("Data/Day13/day13-small.txt")
        assertEquals(6, day13.grid.size)
    }

    @Test
    fun testLoadBig() {
        day13.loadData("Data/Day13/day13-big.txt")
        assertEquals(150, day13.grid.size)
    }
}