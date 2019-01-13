import org.junit.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

interface D20 {
    fun getCount(): Int
    fun add(d20: D20)
    fun printIt()
}

class D20Char(val ch: Char) : D20 {
    override fun printIt() {
        print(ch)
    }

    override fun add(d20: D20) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return 1
    }
}

class D20Bracket : D20 {
    private val bracketList = mutableListOf<D20List>()
    private var total = 0

    override fun printIt() {
        print("(")
        for (i in 0 until bracketList.size) {
            bracketList[i].printIt()
            if (i != bracketList.size - 1) print("|")
        }
        print(")")
    }

    override fun add(d20: D20) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        total = if (bracketList.last().isEmpty()) 0
        else (bracketList.map { it.getCount() }.max() ?: -99999)
        return total
    }

    fun add(): D20List {
        val newList = D20List()
        bracketList.add(newList)
        return newList
    }
}

class D20List : D20 {

    private val members = mutableListOf<D20>()
    private var total = 0

    override fun printIt() {
        members.forEach { it.printIt() }
    }

    override
    fun add(d20: D20) {
        members.add(d20)
    }

    override fun getCount() : Int {
       total= members.map {
            it.getCount()
        }.sum()
        return total
    }

    fun isEmpty() : Boolean {
        return members.size == 0
    }

    fun addBracket(newBracket: D20Bracket) {
        members.add(newBracket)
    }
}

class Day20 {

    val tree = D20Bracket()
    val listStack = Stack<D20List>()
    val bracketStack = Stack<D20Bracket>()

    fun loadFromFile(fileName: String): Int {
        val input = File(fileName).readText()
        return loadFromString(input)
    }

    fun loadFromString(input: String): Int {
        lateinit var currentNode: D20List
        var currentBracket = tree
        input.forEach {
            when (it) {
                '^' -> {
                    currentNode = currentBracket.add()
                }

                'N', 'S', 'E', 'W' -> currentNode.add(D20Char(it))

                '(' -> {
                    listStack.push(currentNode)
                    bracketStack.push(currentBracket)
                    currentBracket = D20Bracket()
                    currentNode.addBracket(currentBracket)
                    currentNode = currentBracket.add()
                }

                ')' -> {
                    currentNode = listStack.pop()
                    currentBracket = bracketStack.pop()
                }

                '|' -> {
                    currentNode = currentBracket.add()
                }
            }
        }
        tree.printIt()
        println()
        val total = tree.getCount()
        return total
    }
}

class Day20Test {


    @Test
    fun checkLoadFromFileTodd() {
        val d = Day20()
        assertEquals(3699, d.loadFromFile("Data/Day20/day20-todd.txt"))
    }
    @Test
    fun checkLoadFromFileOutput() {
        val d = Day20()
        assertEquals(4778, d.loadFromFile("Data/Day20/day20-output.txt"))
        //4775 too low
    }

    @Test
    fun checkLoadFromFile() {
        val d = Day20()
        assertEquals(4775, d.loadFromFile("Data/Day20/day20-big.txt"))
        //4775 too low
    }

    @Test
    fun checkLoadFromString4() {
        val d = Day20()
        assertEquals(23, d.loadFromString("""^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$"""))
    }

    @Test
    fun checkLoadFromString3() {
        val d = Day20()
        assertEquals(31, d.loadFromString("""^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$"""))
    }

    @Test
    fun checkLoadFromString2() {
        val d = Day20()
        assertEquals(18, d.loadFromString("""^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"""))
    }

    @Test
    fun checkNestedBrackets1() {
        val d = Day20()
        assertEquals(6, d.loadFromString("""^E(NEWS(WNSE|)|EE|SWEN)N$"""))
    }

    @Test
    fun checkLoadFromString1() {
        val d = Day20()
        assertEquals(10, d.loadFromString("""^ENWWW(NEEE|SSE(EE|N))$"""))
    }

    @Test
    fun checkLoadFromString() {
        val d = Day20()
        assertEquals(10, d.loadFromString("""^ENWWW((NEEE|S(S|E(EEE|))E(E|N)(N|E))$"""))
    }

    @Test
    fun checkLoadFromString0() {
        val d = Day20()
        assertEquals(3, d.loadFromString("""^WNE$"""))
    }
}