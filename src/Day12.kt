import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class Day12 {

    val rules = mutableMapOf<String, Char>()

    lateinit var pots: String
    var leftPotValue = 0


    fun loadData(filename: String) {

        File(filename).readLines().forEach {

            if (it.startsWith("initial state:")) {
                pots = it.substring(15)
            } else if (it.isNotEmpty()) {
                createRule(it)
            }
        }
    }

    private fun createRule(rule: String) {
        rules[rule.substring(0, 5)] = rule.last()
    }

    fun addPotsToLeftIfRequired() {
        when (pots.substring(0, 2)) {
            ".#" -> {
                pots = ".$pots"
                leftPotValue++
            }
            "#.", "##" -> {
                pots = "..$pots"
                leftPotValue += 2
            }
        }
    }

    fun addPotsToRightIfRequired() {
        when (pots.substring(pots.length - 2)) {
            "#." -> {
                pots = "$pots."
            }
            ".#", "##" -> {
                pots = "$pots.."
            }
        }
    }

    fun printPots() {
        println(pots)
    }

    fun processPots() {
        addPotsToLeftIfRequired()
        addPotsToRightIfRequired()

        val potsToTest = "..$pots.."

        var newPlantStates = ""

        for (i in 2..potsToTest.length - 3) {

            newPlantStates += doesPlantSurvive(potsToTest.substring(i - 2, i + 3))

        }
        pots = newPlantStates
    }

    private fun doesPlantSurvive(pot: String): Char {
        return rules.getOrDefault(pot, '.')

    }

    fun calcScore(): Int {
        var score = 0


        for (i in 0 until pots.length) {
            if (pots[i] == '#') {
                score += i - leftPotValue
            }
        }

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
//        repeat(5000000) {
            repeat(10000) {
                day12.processPots()
            }
            println("looping")
//        }
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
        assertEquals(27, day12.pots.length)
    }

    @Test
    fun checkAddingRight() {
        day12.loadData("Data/Day12/day12-small.txt")
        day12.printPots()
        day12.addPotsToRightIfRequired()
        day12.printPots()
        assertEquals(27, day12.pots.length)
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