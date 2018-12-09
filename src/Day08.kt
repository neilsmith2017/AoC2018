import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

data class Node(
    val numberOfChildNodes: Int,
    var numberOfMetaEntries: Int,
    val meta: MutableList<Int>,
    val childNodes: MutableList<Node>
)

class Day08 {

    val codes = mutableListOf<Int>()
    val nodes = mutableListOf<Node>()

    var ptr = 0

    fun loadData(filename: String) {
        val x = Scanner(File(filename))
        while (x.hasNextInt()) {
            codes.add(x.nextInt())
        }
    }

    fun getNext(): Int {
        return codes[ptr++]
    }

    fun processNodes() : Node {
        val numberOfChildren = getNext()
        val numberOfMetas = getNext()
        val metas = mutableListOf<Int>()
        val childNodes = mutableListOf<Node>()
        val node =  Node(numberOfChildren, numberOfMetas, metas, childNodes)
        nodes.add(node)

        (0 until numberOfChildren).forEach { _ ->
            childNodes.add(processNodes())
        }

        for (i in 0 until numberOfMetas) {
            metas.add(getNext())
        }
        return node
    }

    fun calculateMetaTotal(): Int {
        return nodes.map { it.meta.sum() }.sum()
    }

    fun getNodeValue(n: Node): Int {
        var tot = 0
        if (n.numberOfChildNodes == 0) return n.meta.sum()
        return n.meta.map {
            if (it <= n.numberOfChildNodes) getNodeValue(n.childNodes[it-1])
            else 0
        }.sum()
    }


}


class Day08Test {

    lateinit var day8: Day08

    @Before
    fun setUp() {
        day8 = Day08()
    }

    @Test
    fun check2Big() {
        day8.loadData("Data/Day08/day8-big.txt")
        day8.processNodes()
        assertEquals(30063, day8.getNodeValue(day8.nodes[0]))
    }

    @Test
    fun check2Small() {
        day8.loadData("Data/Day08/day8-small.txt")
        day8.processNodes()
        assertEquals(66, day8.getNodeValue(day8.nodes[0]))
    }

    @Test
    fun checkBig() {
        day8.loadData("Data/Day08/day8-big.txt")
        day8.processNodes()
        assertEquals(48443, day8.calculateMetaTotal())
    }

    @Test
    fun checkSmall() {
        day8.loadData("Data/Day08/day8-small.txt")
        day8.processNodes()
        assertEquals(138, day8.calculateMetaTotal())
    }

    @Test
    fun check() {
        day8.loadData("Data/Day08/day8-small.txt")
        assertEquals(16, day8.codes.size)
    }
}