import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

data class SleepRecord(val date: String, val id: Int, val minutes: BooleanArray)

class Day04 {

    val sleeps = mutableListOf<SleepRecord>()

    val guardRegex = """^.*#([0-9]*) .*$""".toRegex()
    val sleepRegex = """^.*:([0-9]*)].*$""".toRegex()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
                    currentRecord = SleepRecord(getDateString(it), extractGuard(it), BooleanArray(60))
                }
                "falls" -> sleepStart = extractSleep(it)
                "wakes" -> {
                    val sleepEnd = extractSleep(it)
                    (sleepStart until sleepEnd).forEach { minute -> currentRecord.minutes[minute] = true }
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

    fun countDays() {
        sleeps.forEach { println("${it.date}  ${it.id} ${it.minutes.filter { s -> s }.size}") }
        val x = sleeps.map { (_, id, minutes) ->  Pair(id, minutes.filter { s -> s}.size ) }
        val y = x.groupBy { it.first}
        println(y.mapValues { (_, list) ->  list.map { p -> p.second }.sum() })
    }
}

class Day04Test {

    lateinit var day4: Day04

    @Before
    fun setUp() {
        day4 = Day04()
    }

    @Test
    fun check() {
        day4.loadData("Data/Day04-input-files/small-input.txt")
        day4.sleeps.forEach { println(it) }
        assertEquals(5, day4.sleeps.size)
    }

    @Test
    fun checkCountDays() {
        day4.loadData("Data/Day04-input-files/small-input.txt")
        day4.countDays()
    }
}