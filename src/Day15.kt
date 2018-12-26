import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

enum class Square {
    WALL, SPACE, ELF, GOBLIN
}

enum class UnitType {
    ELF, GOBLIN
}

data class Unit(
    val type: UnitType,
    var x: Int,
    var y: Int,
    val name: Int,
    val attack: Int = 3,
    var hp: Int = 200,
    var alive: Boolean = true
) :
    Comparable<Unit> {
    override fun compareTo(other: Unit): Int {
        if (y == other.y) return x - other.x
        return y - other.y
    }
}

data class Point15(val x: Int, val y: Int) {

    fun getNeighbours(): List<Point15> {
        return listOf(
            Point15(x, y - 1),
            Point15(x - 1, y),
            Point15(x + 1, y),
            Point15(x, y + 1)
        )
    }
}

class Day15 {

    val cavern = mutableListOf<MutableList<Square>>()
    val units = mutableListOf<Unit>()

    fun loadData(fileName: String, elfAttack: Int = 3) {
        var name = 0
        cavern.clear()
        units.clear()
        File(fileName).readLines().forEach { line ->
            val row = mutableListOf<Square>()
            line.forEach {
                row.add(
                    when (it) {
                        '.' -> Square.SPACE
                        'G' -> {
                            units.add(Unit(UnitType.GOBLIN, row.size, cavern.size, name++))
                            Square.GOBLIN
                        }
                        'E' -> {
                            units.add(Unit(UnitType.ELF, row.size, cavern.size, name++, elfAttack))
                            Square.ELF
                        }
                        else -> Square.WALL
                    }
                )
            }
            cavern.add(row)
        }
    }

    fun getEnemies(unit: Unit): List<Unit> {
        return units.filter { target -> target.type != unit.type && target.alive }
    }

    fun getTargetPoints(targets: List<Unit>): List<Point15> {
        val points = mutableListOf<Point15>()
        targets.forEach {
            points.addAll(Point15(it.x, it.y).getNeighbours().filter { p -> cavern[p.y][p.x] == Square.SPACE })
        }
        return points
    }

    fun getRoutes(unit: Point15, targets: List<Point15>): List<Point15> {
        val visited: MutableSet<Point15> = mutableSetOf()
        val routes: Deque<List<Point15>> = ArrayDeque()

        visited.add(unit)

        unit.getNeighbours()
            .filter { cavern[it.y][it.x] == Square.SPACE }
            .forEach { routes.add(listOf(it)) }

        while (routes.isNotEmpty()) {
            val route = routes.removeFirst()
            val pathEnd = route.last()

            if (pathEnd in targets) {
                return route
            }

            if (pathEnd !in visited) {
                visited.add(pathEnd)
                pathEnd.getNeighbours()
                    .filter { cavern[it.y][it.x] == Square.SPACE }
                    .filterNot { it in visited }
                    .forEach { routes.add(route + it) }
            }
        }
        return emptyList()
    }

    fun moveUnit(unit: Unit, nextPosition: Point15) {
        cavern[unit.y][unit.x] = Square.SPACE
        cavern[nextPosition.y][nextPosition.x] = if (unit.type == UnitType.ELF) Square.ELF else Square.GOBLIN
        unit.x = nextPosition.x
        unit.y = nextPosition.y
    }

    fun attackNeighbour(unit: Unit): Boolean {
        val neighbourCoords = Point15(unit.x, unit.y).getNeighbours()

        val potentialNeighboursToAttack =
            units.filter { u -> neighbourCoords.contains(Point15(u.x, u.y)) && u.type != unit.type && u.alive }

        val neighbourToAttack =
            if (potentialNeighboursToAttack.size > 1) {
                val minSize = potentialNeighboursToAttack.map { it -> it.hp }.min()
                potentialNeighboursToAttack.filter { it -> it.hp == minSize }.firstOrNull()
            } else {
                potentialNeighboursToAttack.firstOrNull()
            }

        if (neighbourToAttack != null) {
            val unitToAttack = units.first { u -> u.x == neighbourToAttack.x && u.y == neighbourToAttack.y }
            unitToAttack.hp -= unit.attack
            if (unitToAttack.hp <= 0) {
                unitToAttack.alive = false
                cavern[unitToAttack.y][unitToAttack.x] = Square.SPACE
            }
            return true
        }
        return false
    }

    fun doATurn(): Boolean {
        units.filter { it.alive }
            .forEach { u ->
                if (u.alive) {
                    val enemies = getEnemies(u)
                    if (enemies.isEmpty()) return false
                    if (!attackNeighbour(u)) {
                        val routes = getRoutes(Point15(u.x, u.y), getTargetPoints(getEnemies(u)))
                        if (routes.isNotEmpty()) {
                            moveUnit(u, routes.first())
                            attackNeighbour(u)
                        }
                    }
                }
            }
        units.sort()
        return true
    }

    fun doBattle(): Int {
        var turns = 0
//        printCavern()
//        printSummary(turns)
        while (doATurn()) {
            turns++
//            printCavern()
//            printSummary(turns)
            units.removeAll { it -> !it.alive }
        }
        val health = printSummary(turns)
        return turns * health
    }

    private fun printSummary(turns: Int): Int {
//        return 0
        val health = units.filter { u -> u.alive }.sumBy { u -> u.hp }
//        units.sorted().forEach { it -> println("$it") }
        println("Turns = $turns  Health = $health")
        println("total = ${turns * health}")
        return health
    }

    private fun printCavern() {
        return
        cavern.forEach {
            it.forEach { sq ->
                print(
                    when (sq) {
                        Square.WALL -> "#"
                        Square.SPACE -> "."
                        Square.ELF -> "E"
                        Square.GOBLIN -> "G"
                    }
                )
            }
            println()
        }
    }
}

/*
Up or Left  - choose Up
Up or Right - choose Up
Down or Right - choose Right
Down or Left - Choose Left

Up then Left/Right then Down
*/

class Day15Test {

    lateinit var day15: Day15

    @Before
    fun setUp() {
        day15 = Day15()
    }

    @Test
    fun p2Big() {
        val file = "Data/Day15/day15-big.txt"
        var minAttack = 8
        var allDone = false
        day15.loadData(file, minAttack)
        val numberOfElves = day15.units.filter { u -> u.type == UnitType.ELF }.count()
        while (!allDone) {
            minAttack++
            day15.loadData(file, minAttack)
            day15.doBattle()
            if (day15.units.filter { u -> u.type == UnitType.ELF && u.alive }.count() == numberOfElves) allDone = true
        }
        println("minAttack $minAttack")
        // 50200 - too high
        // 51204 - too high
    }

    @Test
    fun p2Check5() {
        val file = "Data/Day15/day15-ex5.txt"
        var minAttack = 3
        var allDone = false
        day15.loadData(file, minAttack)
        val numberOfElves = day15.units.filter { u -> u.type == UnitType.ELF }.count()
        while (!allDone) {
            minAttack++
            println(minAttack)
            day15.loadData(file, minAttack)
            day15.doBattle()
            if (day15.units.filter { u -> u.type == UnitType.ELF && u.alive }.count() == numberOfElves) allDone = true
        }
        println("minAttack $minAttack")
    }

    @Test
    fun p2Check3() {
        val file = "Data/Day15/day15-ex3.txt"
        var minAttack = 3
        var allDone = false
        day15.loadData(file, minAttack)
        val numberOfElves = day15.units.filter { u -> u.type == UnitType.ELF }.count()
        while (!allDone) {
            minAttack++
            println(minAttack)
            day15.loadData(file, minAttack)
            day15.doBattle()
            if (day15.units.filter { u -> u.type == UnitType.ELF }.count() == numberOfElves) allDone = true
        }
        println("minAttack $minAttack")
    }

    @Test
    fun p2Check2() {
        val file = "Data/Day15/day15-ex2.txt"
        var minAttack = 3
        var allDone = false
        day15.loadData(file, minAttack)
        val numberOfElves = day15.units.filter { u -> u.type == UnitType.ELF }.count()
        while (!allDone) {
            minAttack++
            println(minAttack)
            day15.loadData(file, minAttack)
            day15.doBattle()
            if (day15.units.filter { u -> u.type == UnitType.ELF }.count() == numberOfElves) allDone = true
        }
        println("minAttack $minAttack")
    }

    @Test
    fun p2Check1() {
        val file = "Data/Day15/day15-target2.txt"
        var minAttack = 3
        var allDone = false
        while (!allDone) {
            minAttack++
            println(minAttack)
            day15.loadData(file, minAttack)
            day15.doBattle()
            if (day15.units.filter { u -> u.type == UnitType.ELF }.count() == 2) allDone = true
        }
        println("minAttack $minAttack")
    }

    @Test
    fun checkDoingTurnsBig() {
        day15.loadData("Data/Day15/day15-big.txt")
        day15.doBattle()
        // 216552 too big
        // 201856
    }

    @Test
    fun checkNarrow() {
        day15.loadData("Data/Day15/day15-narrow.txt")
        assertEquals(18740, day15.doBattle())
    }

    @Test
    fun checkDoingTurnsMove0() {
        day15.loadData("Data/Day15/day15-move0.txt")
        assertEquals(18740, day15.doBattle())
    }

    @Test
    fun checkDoingTurnsMove1() {
        day15.loadData("Data/Day15/day15-move1.txt")
        assertEquals(18740, day15.doBattle())
    }

    @Test
    fun checkDoingTurnsMove2() {
        day15.loadData("Data/Day15/day15-move2.txt")
        assertEquals(18740, day15.doBattle())
    }

    @Test
    fun checkDoingTurnsEx5() {
        day15.loadData("Data/Day15/day15-ex5.txt")
        assertEquals(18740, day15.doBattle())
    }

    @Test
    fun checkDoingTurnsEx3() {
        day15.loadData("Data/Day15/day15-ex3.txt")
        assertEquals(27755, day15.doBattle())
    }

    @Test
    fun checkDoingTurnsEx2() {
        day15.loadData("Data/Day15/day15-ex2.txt")
        assertEquals(39514, day15.doBattle())
    }

    @Test
    fun checkDoingTurnsEx1() {
        day15.loadData("Data/Day15/day15-ex1.txt")
        assertEquals(36334, day15.doBattle())
    }

    @Test
    fun checkDoingTurns() {
        day15.loadData("Data/Day15/day15-target2.txt")
        assertEquals(27730, day15.doBattle())
    }

    @Test
    fun checkDoingATurn() {
        day15.loadData("Data/Day15/day15-target2.txt")
        day15.doATurn()
        println(day15.units)
    }

    @Test
    fun checkGetRoutes() {
        day15.loadData("Data/Day15/day15-targets.txt")

        val elf = day15.units.first()
        val enemies = day15.getEnemies(elf)
        val targetPoints = day15.getTargetPoints(enemies)
        val route = day15.getRoutes(Point15(elf.x, elf.y), targetPoints)

        println(route)
    }

    @Test
    fun checkGetPoints() {
        day15.loadData("Data/Day15/day15-targets.txt")
        println(day15.getTargetPoints(day15.getEnemies(day15.units.first())))
    }

    @Test
    fun checkGetTargets() {
        day15.loadData("Data/Day15/day15-small.txt")
        println(day15.getEnemies(day15.units.last()))
    }

    @Test
    fun checkLoadingSmall() {
        day15.loadData("Data/Day15/day15-small.txt")
        assertEquals(6, day15.units.size)
//        println(day15.units)
//        println(day15.cavern)
    }

    @Test
    fun checkSort() {
        day15.loadData("Data/Day15/day15-small.txt")
        day15.units.sort()
        println(day15.units)
    }

}