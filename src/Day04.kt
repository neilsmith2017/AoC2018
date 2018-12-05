import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

data class SleepRecord(val date: String, val id: Int, val minutes: IntArray)

class Day04 {

    val sleeps = mutableListOf<SleepRecord>()

    private val guardRegex = """^.*#([0-9]*) .*$""".toRegex()
    private val sleepRegex = """^.*:([0-9]*)].*$""".toRegex()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun loadData(filename: String) {
        var sleepStart = 0
        lateinit var currentRecord: SleepRecord
        var firstTime = true

        File(filename).readLines().toList().sorted().forEach {
            when (it.substring(19, 24)) {
                "Guard" -> {
                    if (firstTime) {
                        firstTime = false
                    } else {
                        sleeps.add(currentRecord)
                    }
                    currentRecord = SleepRecord(getDateString(it), extractGuard(it), IntArray(60))
                }
                "falls" -> sleepStart = extractSleep(it)
                "wakes" -> {
                    val sleepEnd = extractSleep(it)
                    (sleepStart until sleepEnd).forEach { minute -> currentRecord.minutes[minute] = 1 }
                }
                else -> throw IllegalArgumentException("Invalid input $it")
            }
        }
        if (!firstTime) sleeps.add(currentRecord)
    }

    private fun getDateString(it: String): String {
        var d = LocalDate.parse(it.substring(1, 11), formatter)
        if (it.substring(12..13) != "00") d = d.plusDays(1)
        return d.format(formatter)
    }

    private fun extractSleep(line: String): Int {
        return sleepRegex.matchEntire(line)
            ?.destructured
            ?.let { (sleepTime) ->
                sleepTime.toInt()
            }
            ?: throw IllegalArgumentException("Invalid input $line")
    }

    private fun extractGuard(line: String): Int {
        return guardRegex.matchEntire(line)
            ?.destructured
            ?.let { (id) -> id.toInt() }
            ?: throw IllegalArgumentException("Invalid input $line")
    }

    fun countDays(): Pair<Int, Int> {
        return sleeps.map { (_, id, minutes) -> Pair(id, minutes.sum()) }
            .groupBy { it.first }
            .mapValues { (_, list) -> list.map { p -> p.second }.sum() }
            .maxBy { (k, v) -> v }
            ?.toPair() ?: throw IllegalArgumentException("Map is empty")
    }


    fun findCommonMinute(guardId: Int): Pair<Int, Int> {

        val s = sleeps.filter { it.id == guardId }
        val sleepsForGuard = IntArray(60)

        s.forEach { sleepRecord ->
            (0..59).forEach { minute ->
                sleepsForGuard[minute] += sleepRecord.minutes[minute]
            }
        }

        return Pair(sleepsForGuard.indexOf(sleepsForGuard.max() ?: -1), sleepsForGuard.max() ?: -1)
    }

    fun solvePart1(filename: String): Int {
        loadData(filename)
        val guard = countDays()
        val minute = findCommonMinute(guard.first)
        return guard.first * minute.first
    }


    fun solvePart2(filename: String): Int {
        loadData(filename)

        val guardMaxSleepMinute = mutableMapOf<Int, Pair<Int, Int>>()

        sleeps.map { (_, id, _) -> id }.distinct().forEach { guardId ->
            guardMaxSleepMinute[guardId] = findCommonMinute(guardId)
        }

        val theOne = guardMaxSleepMinute.maxBy { it.value.second }

        return if (theOne != null) theOne.key * theOne.value.first else -1
    }


}

class Day04Test {

    private lateinit var day4: Day04

    @Before
    fun setUp() {
        day4 = Day04()
    }

    @Test
    fun check() {
        day4.loadData("Data/Day04-input-files/small-input.txt")
        assertEquals(5, day4.sleeps.size)
    }

    @Test
    fun checkCountDays() {
        day4.loadData("Data/Day04-input-files/small-input.txt")
        val result = day4.countDays()
        assertEquals(10, result.first)
        assertEquals(50, result.second)
        assertEquals(24, day4.findCommonMinute(result.first).first)
    }

    @Test
    fun runFirstPart() {
        assertEquals(240, day4.solvePart1("Data/Day04-input-files/small-input.txt"))
    }

    @Test
    fun runFirstPartBigData() {
        assertEquals(94040, day4.solvePart1("Data/Day04-input-files/big-input.txt"))
    }

    @Test
    fun runSecondPart() {
        assertEquals(4455, day4.solvePart2("Data/Day04-input-files/small-input.txt"))
    }

    @Test
    fun runSecondPartBigData() {
        assertEquals(39940, day4.solvePart2("Data/Day04-input-files/big-input.txt"))
    }
}