import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class Day1 {

    val frequencyChanges = mutableListOf<Int>()
    private val alreadySeenFrequencies = mutableSetOf<Int>()

    fun loadData(filename: String) {
        File(filename).readLines().forEach {
            frequencyChanges.add(it.toInt())
        }
    }

    fun processFrequencyChangesForPart1(): Int {
        return frequencyChanges.sum()
    }

    fun processFrequencyChangesForPart2(): Int {
        var offset = 0
        while (true) {
            frequencyChanges.forEach {
                if (alreadySeenFrequencies.contains(offset)) return offset
                alreadySeenFrequencies.add(offset)
                offset += it
            }
        }
    }

}

class Day1Part1Test {

    lateinit var day1: Day1

    @Before
    fun setUp() {
        day1 = Day1()
    }

    @Test
    fun testLoad() {
        day1.loadData("Day01Input.txt")
        assertEquals(1036, day1.frequencyChanges.size)
    }

    @Test
    fun runItForRealWithSmallData() {
        day1.loadData("Day01InputSmallData.txt")
        assertEquals(3, day1.processFrequencyChangesForPart1())
    }

    @Test
    fun runItForRealWithBigData() {
        day1.loadData("Day01Input.txt")
        assertEquals(402, day1.processFrequencyChangesForPart1())
    }
}

class Day1Part2Test {

    lateinit var day1: Day1

    @Before
    fun setUp() {
        day1 = Day1()
    }

    @Test
    fun runItForRealWithSmallData() {
        day1.loadData("Day01InputSmallData.txt")
        assertEquals(2, day1.processFrequencyChangesForPart2())
    }

    @Test
    fun runItForRealWithSmallData1() {
        day1.loadData("Day01InputSmall1.txt")
        assertEquals(0, day1.processFrequencyChangesForPart2())
    }

    @Test
    fun runItForRealWithSmallData2() {
        day1.loadData("Day01InputSmall2.txt")
        assertEquals(10, day1.processFrequencyChangesForPart2())
    }

    @Test
    fun runItForRealWithSmallData3() {
        day1.loadData("Day01InputSmall3.txt")
        assertEquals(5, day1.processFrequencyChangesForPart2())
    }

    @Test
    fun runItForRealWithSmallData4() {
        day1.loadData("Day01InputSmall4.txt")
        assertEquals(14, day1.processFrequencyChangesForPart2())
    }

    @Test
    fun runItForRealWithBigData() {
        day1.loadData("Day01Input.txt")
        assertEquals(481, day1.processFrequencyChangesForPart2())
    }
}