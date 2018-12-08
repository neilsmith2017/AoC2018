import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class Day07 {

    val v = mutableSetOf<Char>()
    val e = mutableSetOf<Pair<Char, Char>>()
    val w = mutableListOf<Int>()
    val weights = mutableMapOf<Char, Int>()

    fun loadData(filename: String) {
        File(filename).readLines().forEach {
            val end = it.elementAt(36)
            val start = it.elementAt(5)
            v.add(end)
            v.add(start)
            e.add(Pair(start, end))
        }
    }

    fun getCandidateEdges(): List<Char> {
        val targets = e.map { it.second }.toSet()
        val sources = e.map { it.first }.toSet().filter { v.contains(it) }
        return if (e.isEmpty()) v.toList() else (sources - targets).sorted().toList()
    }

    fun calculateMinWeights(order: String, unitTime: Int) {
        order.forEach {
            val minOfPrevious = e.filter { edge -> edge.second == it }
                .map { edge -> (weights[edge.first] ?: 0) + edge.first.toInt() - 'A'.toInt() + unitTime + 1 }
                .max() ?: 0
            weights[it] = minOfPrevious
        }

    }

    fun process(): String {
        var result = ""
        while (v.isNotEmpty()) {
            val it = getCandidateEdges()[0]
            result += it
            e.removeAll { pair -> pair.first == it }
            v.remove(it)
        }
        return result
    }


    fun buildIt(numberOfWorkers: Int, unitTime: Int, order: String): Int {
        (1..numberOfWorkers).forEach { _ -> w.add(0) }
        calculateMinWeights(order, unitTime)

        while (v.isNotEmpty()) {
            val candidates = getCandidateEdges()
            val indecesOfAvailableWorkers = getIndecesOfAvailableWorkers(candidates.size)

            var minValueOfNew = Int.MAX_VALUE

            (0 until indecesOfAvailableWorkers.size).forEach {
                val workerIndex = indecesOfAvailableWorkers[it]
                if (w[workerIndex] < weights.get(candidates[it]) ?: 0) w[workerIndex] = weights[candidates[it]] ?: 0
                w[workerIndex] = w[workerIndex] + candidates[it].toInt() - 'A'.toInt() + unitTime + 1
                if (w[workerIndex] < minValueOfNew) minValueOfNew = w[workerIndex]
                e.removeAll { pair -> pair.first == candidates[it] }
                v.remove(candidates[it])
            }

            w.indices.forEach {
                if (w[it] < minValueOfNew && !indecesOfAvailableWorkers.contains(it)) w[it] = minValueOfNew
            }
        }

        return w.max() ?: 0
    }

    private fun getIndecesOfAvailableWorkers(size: Int): List<Int> {
        val minWorker = w.min()
        val possibleWorkers = w.indices.filter { w[it] == minWorker }
        return possibleWorkers.subList(0, minOf(size, possibleWorkers.size))
    }
}

class Day07Test {

    lateinit var day7: Day07

    @Before
    fun setUp() {
        day7 = Day07()
    }

    @Test
    fun buildItBig() {
        day7.loadData("Data/Day07/day7-big.txt")
        assertEquals(1053, day7.buildIt(5, 60, "FMOXCDGJRAUIHKNYZTESWLPBQV"))
    }

    @Test
    fun buildItSmall2() {
        day7.loadData("Data/Day07/day7-small.txt")
        assertEquals(14, day7.buildIt(3, 0, "CABDFE"))
    }

    @Test
    fun buildItSmall() {
        day7.loadData("Data/Day07/day7-small.txt")
        assertEquals(15, day7.buildIt(2, 0, "CABDFE"))
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
        assertEquals(listOf('C'), day7.getCandidateEdges())
    }

    @Test
    fun check() {
        day7.loadData("Data/Day07/day7-small.txt")
        assertEquals(6, day7.v.size)
        assertEquals(7, day7.e.size)
    }
}