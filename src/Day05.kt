import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.math.abs
import kotlin.test.assertEquals

class Day05 {

    fun loadData(s: String): String {
        val b = File(s).readBytes()
        return String(b)
    }

    fun reduce(input: String): String {
        var chars = input
        var newChars = mutableListOf<Char>()
        var charsLength = 0

        while (chars.length != charsLength) {
            var i = 0
            while (i < chars.length - 1)
                if (abs(chars[i] - chars[i + 1]) == 32) {
                    i += 2
                } else {
                    newChars.add(chars[i])
                    i++
                }
            if (i < chars.length) newChars.add(chars[i])
            charsLength = chars.length
            chars = String(newChars.toCharArray())
            newChars = arrayListOf()

        }

        return chars
    }

    fun processFile(filename: String): Int {
        val input = loadData(filename)
        return reduce(input).length
    }

    fun distinctValues(input: String) = String(input.toCharArray().distinctBy { it.toLowerCase() }.toCharArray())

    fun reduceSkippingCharacter(input: String, charToSkip: Char): String {
      return reduce(input.filter { it.toLowerCase() != charToSkip.toLowerCase() })
    }

    fun processFile2(filename: String): Int {
        val input = loadData(filename)
        val deduped = distinctValues(input)
        return deduped.toCharArray().map { it -> reduceSkippingCharacter(input, it) }.map { it.length }.min() ?: 0
    }
}

class Day05Test {

    lateinit var day5: Day05

    @Before
    fun setUp() {
        day5 = Day05()
    }

    @Test
    fun checkDedup() {
        val input = "dabAcCaCBAcCcaDA"
        val x = day5.distinctValues(input)
        val y = x.toCharArray().map { it -> day5.reduceSkippingCharacter(input, it) }.map { it.length }.min() ?: 0
        assertEquals(4, y)
    }

    @Test
    fun check1() {
        assertEquals("", day5.reduce("Aa"))
    }

    @Test
    fun check2() {
        assertEquals("", day5.reduce("abBA"))
    }

    @Test
    fun check3() {
        assertEquals("abAB", day5.reduce("abAB"))
    }

    @Test
    fun check4() {
        assertEquals("aabAAB", day5.reduce("aabAAB"))
    }

    @Test
    fun check5() {
        assertEquals("dabCBAcaDA", day5.reduce("dabAcCaCBAcCcaDA"))
    }

    @Test
    fun checkBig() {
        assertEquals(9116, day5.processFile("Data/Day05/day5-big.txt"))
    }

    @Test
    fun calc() {
        assertEquals(32, 'a' - 'A')
    }

    @Test
    fun check21() {
        assertEquals("dabc", day5.distinctValues("dabAcCaCBAcCcaDA"))
    }

    @Test
    fun check22() {
        assertEquals("dbCBcD", day5.reduceSkippingCharacter("dabAcCaCBAcCcaDA", 'a'))
    }

    @Test
    fun check23() {
        assertEquals("daCAcaDA", day5.reduceSkippingCharacter("dabAcCaCBAcCcaDA", 'b'))
    }

    @Test
    fun check24() {
        assertEquals("daDA", day5.reduceSkippingCharacter("dabAcCaCBAcCcaDA", 'c'))
    }

    @Test
    fun check25() {
        assertEquals("abCBAc", day5.reduceSkippingCharacter("dabAcCaCBAcCcaDA", 'd'))
    }

    @Test
    fun checkBig2() {
        assertEquals(6890, day5.processFile2("Data/Day05/day5-big.txt"))
    }
}