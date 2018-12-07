import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class Day07 {

    val v = mutableSetOf<Char>()
    val e = mutableSetOf<Pair<Char, Char>>()

    fun loadData(filename: String) {
        File(filename).readLines().forEach {
            val end = it.elementAt(36)
            val start = it.elementAt(5)
            v.add(end)
            v.add(start)
            e.add(Pair(start, end))
        }
    }

    fun getCandidates(): List<Char> {
        val targets = e.map { it.second }.toSet()
        val sources = e.map { it.first }.toSet().filter { v.contains(it) }
        return if (e.isEmpty()) v.toList() else (sources - targets).sorted().toList()
    }

    fun process(): String {
        var result = ""
        while (v.isNotEmpty()) {
            val it = getCandidates().get(0)
            result += it
            e.removeAll { pair -> pair.first == it }
            v.remove(it)
        }
        return result
    }

}

class Day07Test {

    lateinit var day7: Day07

    @Before
    fun setUp() {
        day7 = Day07()
    }

    @Test
    fun processGraphBig() {
        day7.loadData("Data/Day07/day7-big.txt")
        assertEquals("FMOXCDGJRAUIHKNYZTESWLPBQV", day7.process())
    }

    @Test
    fun processGraph() {
        day7.loadData("Data/Day07/day7-small.txt")
        assertEquals("CABDFE", day7.process())
    }

    @Test
    fun checkGetCandidates() {
        day7.loadData("Data/Day07/day7-small.txt")
        assertEquals(listOf('C'), day7.getCandidates())
    }

    @Test
    fun check() {
        day7.loadData("Data/Day07/day7-small.txt")
        assertEquals(6, day7.v.size)
        assertEquals(7, day7.e.size)
    }
}