import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

data class Pot(var plant: Boolean = false, var prev: Pot?, var next: Pot?)

class Day12 {

    val rules = mutableMapOf<Int, Boolean>()
    val potVeryLeft = Pot(false, null, null)
    val potLeft = Pot(false, potVeryLeft, null)
    val potRight = Pot(false, potLeft, null)
    val potVeryRight = Pot(false, potRight, null)
    var pot0: Pot? = null

    fun loadData(filename: String) {

        potVeryLeft.next = potLeft
        potLeft.next = potRight
        potRight.next = potVeryRight

        File(filename).readLines().forEach {

            if (it.startsWith("initial state:")) {
                buildInitialState(it.substring(15))
            } else if (it.isNotEmpty()) {
                createRule(it)
            }
        }
        pot0 = findLeftPot()
    }

    private fun createRule(rule: String) {
        val reversedRule = rule.substring(0, 5).reversed()
        var multiplier = 1
        var total = 0
        reversedRule.forEach {
            if (it == '#') total += multiplier
            multiplier *= 2
        }
        rules[total] = rule.last() == '#'
    }

    private fun buildInitialState(initialState: String) {
        var lastPot = potLeft

        initialState.forEach {
            val currentPot = Pot(it == '#', lastPot, lastPot.next)
            lastPot.next = currentPot
            currentPot.next?.prev = currentPot

            lastPot = currentPot
        }

    }

    fun findLeftPot(): Pot? {
        return potLeft.next
    }

    fun findRightPot(): Pot? {
        return potRight.prev
    }

    fun addPotsToLeftIfRequired() {
        val leftPot = findLeftPot()
        leftPot?.apply {
            if (plant || next?.plant == true) {
                insertPotAfter(prev)
                addPotsToLeftIfRequired()
            }
        }
    }

    private fun insertPotAfter(pot: Pot?) {
        pot?.apply {
            val newPot = Pot(false, this, next)
            next?.prev = newPot
            next = newPot
        }
    }

    fun addPotsToRightIfRequired() {
        val rightPot = findRightPot()
        rightPot?.apply {
            if (plant || prev?.plant == true) {
                insertPotAfter(this)
                addPotsToRightIfRequired()
            }
        }
    }

    fun printPots() {
        var currentPot: Pot? = potVeryLeft
        do {
            print(getSymbol(currentPot?.plant == true))
            currentPot = currentPot?.next
        } while (currentPot != null)
        println()
    }

    private fun getSymbol(plant: Boolean): String {
        return if (plant) "#" else "."
    }

    fun processPots() {
        addPotsToLeftIfRequired()
        addPotsToRightIfRequired()
        var currentPot = findLeftPot()

        val newPlantStates = mutableListOf<Boolean>()
        while (currentPot?.next != null) {

            newPlantStates.add(doesPlantSurvive(currentPot))

            currentPot = currentPot.next!!
        }
        currentPot = findLeftPot()
        newPlantStates.forEach {
            currentPot?.plant = it
            currentPot = currentPot?.next!!
        }
    }

    private fun doesPlantSurvive(currentPot: Pot): Boolean {
        return if ( currentPot.next != null && currentPot.next?.next != null) {
            var currentScore = 0
            currentScore += if (currentPot.prev?.prev?.plant == true) 16 else 0
            currentScore += if (currentPot.prev?.plant == true) 8 else 0
            currentScore += if (currentPot.plant) 4 else 0
            currentScore += if (currentPot.next?.plant == true) 2 else 0
            currentScore += if (currentPot.next?.next?.plant == true) 1 else 0
            rules.getOrDefault(currentScore, false)
        } else {
            false
        }
    }

    fun calcScore(): Int {
        var currentPot = pot0
        var index = 0
        var score = 0
        do {
            if (currentPot?.plant == true) {
                score += index
            }
            index++
            currentPot = currentPot?.next
        } while (currentPot != null)

        currentPot = pot0
        index = 0
        do {
            if (currentPot?.plant == true) {
                score += index
            }
            index--
            currentPot = currentPot?.prev
        } while (currentPot != null)

        return score
    }
}

class Day12test {

    lateinit var day12: Day12

    @Before
    fun setUp() {
        day12 = Day12()
    }

    @Test
    fun checkBigScore2() {
        day12.loadData("Data/Day12/day12-big.txt")
        repeat(5000000) {
            repeat(10000) {
                day12.processPots()
            }
        }
        assertEquals(3276, day12.calcScore())
    }

    @Test
    fun checkBigScore() {
        day12.loadData("Data/Day12/day12-big.txt")
        repeat(20) {
            day12.processPots()
        }
        assertEquals(3276, day12.calcScore())
    }

    @Test
    fun checkScore() {
        day12.loadData("Data/Day12/day12-small.txt")
        repeat(20) {
            day12.processPots()
        }
        assertEquals(325, day12.calcScore())
    }

    @Test
    fun checkProcess() {
        day12.loadData("Data/Day12/day12-small.txt")
        day12.printPots()
        repeat(20) {
            day12.processPots()
            print("$it ")
            day12.printPots()
        }
    }

    @Test
    fun checkAddingLeft() {
        day12.loadData("Data/Day12/day12-small.txt")
        day12.printPots()
        day12.addPotsToLeftIfRequired()
        day12.printPots()
    }

    @Test
    fun checkAddingRight() {
        day12.loadData("Data/Day12/day12-small.txt")
        day12.printPots()
        day12.addPotsToRightIfRequired()
        day12.printPots()
    }

    @Test
    fun checkPrinting() {
        day12.loadData("Data/Day12/day12-small.txt")
        day12.printPots()
    }

    @Test
    fun checkRules() {
        day12.loadData("Data/Day12/day12-small.txt")
        assertEquals(32, day12.rules.size)
    }
}