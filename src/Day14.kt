import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Day14 {


    val recipes = mutableListOf<Int>()

    var elf1 = 0
    var elf2 = 1

    init {
        recipes.add(3)
        recipes.add(7)
    }

    fun doRound() {
        val score = recipes[elf1] + recipes[elf2]
        if (score > 9) {
            recipes.add(1)
            recipes.add(score % 10)
        } else {
            recipes.add(score)
        }

        elf1 = (elf1 + 1 + recipes[elf1]) % recipes.size
        elf2 = (elf2 + 1 + recipes[elf2]) % recipes.size
    }

    fun getScore(position: Int): String {
        return recipes.subList(position, position + 10).joinToString(separator = "")
    }

    fun getTurnsBefore(s: String, maxTurns: Int): Int {
        repeat(maxTurns) {
            doRound()
            if (recipes.size > s.length) {
                if (s.equals(
                        recipes.subList(
                            recipes.size - s.length,
                            recipes.size
                        ).joinToString(separator = "")
                    )
                ) return recipes.size - s.length
            }
            if (recipes.size > 1 + s.length) {
                if (s.equals(
                        recipes.subList(
                            recipes.size - s.length - 1,
                            recipes.size - 1
                        ).joinToString(separator = "")
                    )
                ) return recipes.size - s.length - 1
            }
        }
        return 0
    }

}

class Day14Test {

    lateinit var day14: Day14

    @Before
    fun setUp() {
        day14 = Day14()
    }

    @Test
    fun checkPart2ForBig() {
        assertEquals(20200561, day14.getTurnsBefore("990941", 15500000))
    }

    @Test
    fun checkPart2For2018() {
        assertEquals(2018, day14.getTurnsBefore("59414", 2019))
    }

    @Test
    fun checkPart2ForEighteen() {
        assertEquals(18, day14.getTurnsBefore("92510", 19))
    }

    @Test
    fun checkPart2ForFive() {
        assertEquals(5, day14.getTurnsBefore("01245", 10))
    }

    @Test
    fun checkPart2ForNine() {
        assertEquals(9, day14.getTurnsBefore("51589", 10))
    }

    @Test
    fun checkPart1() {
        while (day14.recipes.size < 990951) {
            day14.doRound()
        }
        assertEquals("3841138812", day14.getScore(990941))
    }

    @Test
    fun checkScoreAfterTwentyEighteenRounds() {
        while (day14.recipes.size < 2028) {
            day14.doRound()
        }
        assertEquals("5941429882", day14.getScore(2018))
    }

    @Test
    fun checkScoreAfterEighteenRounds() {
        while (day14.recipes.size < 28) {
            day14.doRound()
        }
        assertEquals("9251071085", day14.getScore(18))
    }

    @Test
    fun checkScoreAfterFiveRounds() {
        while (day14.recipes.size < 15) {
            day14.doRound()
        }
        assertEquals("0124515891", day14.getScore(5))
    }

    @Test
    fun checkScoreAfterNineRounds() {
        while (day14.recipes.size < 19) {
            day14.doRound()
        }
        assertEquals("5158916779", day14.getScore(9))
    }

    @Test
    fun testOneRound() {
        day14.doRound()
        assertEquals(listOf(3, 7, 1, 0), day14.recipes)
        assertEquals(0, day14.elf1)
        assertEquals(1, day14.elf2)
    }

    @Test
    fun testTwoRounds() {
        day14.doRound()
        day14.doRound()
        assertEquals(listOf(3, 7, 1, 0, 1, 0), day14.recipes)
        assertEquals(4, day14.elf1)
        assertEquals(3, day14.elf2)
    }

}