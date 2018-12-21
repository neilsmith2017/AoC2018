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
    SPACE
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

enum class Turn {
    STRAIGHT,
    LEFT,
    RIGHT
}

data class Cart(
    var x: Int,
    var y: Int,
    var direction: Direction,
    var nextDirection: Turn = Turn.LEFT,
    var exists: Boolean = true
) :
    Comparable<Cart> {
    override fun compareTo(other: Cart): Int {
        return if (y == other.y) x - other.x
        else y - other.y
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Cart) return false
        if (this === other) return false
        return (x == other.x && y == other.y)

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
                            carts.add(Cart(x, rowNumber, Direction.LEFT))
                            Shapes.HORIZONTAL
                        }
                        '>' -> {
                            carts.add(Cart(x, rowNumber, Direction.RIGHT))
                            Shapes.HORIZONTAL
                        }
                        '^' -> {
                            carts.add(Cart(x, rowNumber, Direction.UP))
                            Shapes.VERTICAL
                        }
                        'v' -> {
                            carts.add(Cart(x, rowNumber, Direction.DOWN))
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

    fun doATick(part1: Boolean): Boolean {
        carts.sort()
        carts.forEach { cart ->
            cart.x = getNextX(cart.x, cart.direction)
            cart.y = getNextY(cart.y, cart.direction)

            if (carts.contains(cart)) {
                if (part1) {
                    println("CLASH found $cart")
                    return true
                } else {
                    cart.exists = false
                    carts.forEach() {
                        if (it.x == cart.x && it.y == cart.y) it.exists = false
                    }
                }
            }


            when (grid[cart.y][cart.x]) {

                Shapes.FORWARD_SLASH -> cart.direction = when (cart.direction) {
                    Direction.UP -> Direction.RIGHT
                    Direction.DOWN -> Direction.LEFT
                    Direction.LEFT -> Direction.DOWN
                    Direction.RIGHT -> Direction.UP
                }
                Shapes.BACKWARD_SLASH -> cart.direction = when (cart.direction) {
                    Direction.UP -> Direction.LEFT
                    Direction.DOWN -> Direction.RIGHT
                    Direction.LEFT -> Direction.UP
                    Direction.RIGHT -> Direction.DOWN
                }
                Shapes.JUNCTION -> cart.direction = when (cart.nextDirection) {


                    Turn.STRAIGHT -> {
                        cart.nextDirection = Turn.RIGHT
                        cart.direction
                    }
                    Turn.LEFT -> {
                        cart.nextDirection = Turn.STRAIGHT
                        when (cart.direction) {
                            Direction.UP -> Direction.LEFT
                            Direction.DOWN -> Direction.RIGHT
                            Direction.LEFT -> Direction.DOWN
                            Direction.RIGHT -> Direction.UP
                        }
                    }
                    Turn.RIGHT -> {
                        cart.nextDirection = Turn.LEFT
                        when (cart.direction) {
                            Direction.UP -> Direction.RIGHT
                            Direction.DOWN -> Direction.LEFT
                            Direction.LEFT -> Direction.UP
                            Direction.RIGHT -> Direction.DOWN
                        }
                    }
                }
                else -> cart.direction
            }
        }
        carts.removeIf { c -> !c.exists }
        if (carts.size == 1) return true
        return false
    }

    private fun getNextX(x: Int, direction: Direction): Int {
        return when (direction) {
            Direction.DOWN, Direction.UP -> x
            Direction.LEFT -> x - 1
            Direction.RIGHT -> x + 1
        }
    }

    private fun getNextY(y: Int, direction: Direction): Int {
        return when (direction) {
            Direction.DOWN -> y + 1
            Direction.UP -> y - 1
            Direction.LEFT, Direction.RIGHT -> y
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
    fun testTick2Big() {
        day13.loadData("Data/Day13/day13-big.txt")
        var iterations = 0
        do {
            iterations++
        } while (!day13.doATick(false))
        println("Iternations $iterations")
        println(day13.carts)
    }

    @Test
    fun testTick2Small() {
        day13.loadData("Data/Day13/day13-part2.txt")
        var iterations = 0
        do {
            iterations++
        } while (!day13.doATick(false))
        println("Iternations $iterations")
        println(day13.carts)
    }

    @Test
    fun testTickBig() {
        day13.loadData("Data/Day13/day13-big.txt")
        var iterations = 0
        do {
            iterations++
        } while (!day13.doATick(true))
        println("Iternations $iterations")
    }

    @Test
    fun testTickSmall() {
        day13.loadData("Data/Day13/day13-small.txt")
        var iterations = 0
        do {
            iterations++
        } while (!day13.doATick(true))
        println("Iternations $iterations")
    }

    @Test
    fun testTickTiny() {
        day13.loadData("Data/Day13/day13-tiny.txt")
        println(day13.carts)
        day13.doATick(true)
        println(day13.carts)
        day13.doATick(true)
        println(day13.carts)
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