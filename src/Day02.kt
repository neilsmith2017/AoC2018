import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class Day02 {

    val boxIds = mutableListOf<String>()

    fun loadData(filename: String) {
        File(filename).readLines().forEach {
            boxIds.add(it)
        }
    }

    fun processStrings(): Int {
        var twos = 0
        var threes = 0
        boxIds.forEach {
            val twosOrThrees = it.hasTwosOrThrees()
            twos += if (twosOrThrees.first) 1 else 0
            threes += if (twosOrThrees.second) 1 else 0
        }
        return twos * threes
    }

    fun compareAllStrings(): String {
        boxIds.forEach { source ->
            boxIds.forEach { other ->
                if (source.numberOfDifferencesAtSamePosition(other) == 1) return source.sames(other)
            }
        }
        return ""
    }
}

private fun String.sames(other: String): String {
    var sames = ""
    for (i in 0 until this.length) {
        if (this[i] == other[i]) sames += this[i]
    }
    return sames
}

private fun String.hasTwosOrThrees(): Pair<Boolean, Boolean> {
    val l = this.toCharArray().groupBy { it }.mapValues { (_, v) -> v.size }
    return Pair(l.containsValue(2), l.containsValue(3))
}

private fun String.numberOfDifferencesAtSamePosition(other: String): Int {
    if (this.length != other.length) return -1
    var differences = 0
    for (i in 0 until this.length) {
        if (this[i] != other[i]) differences++
    }
    return differences
}

class TestDay2Part1 {

    lateinit var day2: Day02

    @Before
    fun setUp() {
        day2 = Day02()
    }

    @Test
    fun checkFileSizeIsCorrect() {
        day2.loadData("Day02-input-files/small-input.txt")
        assertEquals(7, day2.boxIds.size)
    }

    @Test
    fun checkParseAString() {
        assertEquals(Pair(false, false), "abababab".hasTwosOrThrees())
        assertEquals(Pair(true, false), "aabb".hasTwosOrThrees())
        assertEquals(Pair(true, true), "aabbb".hasTwosOrThrees())
        assertEquals(Pair(false, true), "aaabbb".hasTwosOrThrees())
    }

    @Test
    fun checkSmallData() {
        day2.loadData("Day02-input-files/small-input.txt")
        assertEquals(12, day2.processStrings())
    }

    @Test
    fun checkBigData() {
        day2.loadData("Day02-input-files/input.txt")
        assertEquals(5000, day2.processStrings())
    }
}

class TestDay2Part2 {

    lateinit var day2: Day02

    @Before
    fun setUp() {
        day2 = Day02()
    }

    @Test
    fun checkFileSizeIsCorrect() {
        day2.loadData("Data/Day02-input-files/part2-small-input.txt")
        assertEquals(7, day2.boxIds.size)
    }

    @Test
    fun checkDifferenceCounts() {
        assertEquals(0, "abc".numberOfDifferencesAtSamePosition("abc"))
        assertEquals(1, "abc".numberOfDifferencesAtSamePosition("abd"))
        assertEquals(3, "abc".numberOfDifferencesAtSamePosition("def"))
        assertEquals(2, "abc".numberOfDifferencesAtSamePosition("aed"))
    }

    @Test
    fun checkSmallStrings() {
        day2.loadData("Data/Day02-input-files/part2-small-input.txt")
        assertEquals("fgij", day2.compareAllStrings())
    }

    @Test
    fun checkBigStrings() {
        day2.loadData("Data/Day02-input-files/input.txt")
        assertEquals("ymdrchgpvwfloluktajxijsqb", day2.compareAllStrings())
    }
}