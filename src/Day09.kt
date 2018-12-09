import org.junit.Before
import org.junit.Test
import java.math.BigInteger
import kotlin.test.assertEquals

data class Marble(var previous: Marble?, var next: Marble?, var points: BigInteger = BigInteger.ZERO)

class Day9 {

    var numberOfMarbles = 0
    var numberOfPlayers = 0
    var currentMarble = Marble(null, null)
    lateinit var players: Array<BigInteger>
    var currentPlayer = 0

    fun setUp(input: String) {
        val words = input.split(" ")
        numberOfMarbles = words[6].toInt()
        numberOfPlayers = words[0].toInt()
        currentMarble.previous = currentMarble
        currentMarble.next = currentMarble
        players = Array(numberOfPlayers) { _ -> BigInteger.ZERO }
    }

    fun getWinningScore(s: String): BigInteger {
        setUp(s)

        (1..numberOfMarbles).forEach { marbleNumber ->
            if (marbleNumber % 23 == 0) {
                doSpecial(marbleNumber)
            } else {
                insertThisMarbleAfterNext(marbleNumber)
            }
            currentPlayer++
            if (currentPlayer >= numberOfPlayers) currentPlayer = 0
        }


        return players.max() ?: BigInteger.ZERO
    }

    private fun doSpecial(marbleNumber: Int) {
        players[currentPlayer] += BigInteger.valueOf(marbleNumber.toLong())
        val marbleSevenToLeft = currentMarble?.previous?.previous?.previous?.previous?.previous?.previous?.previous
        players[currentPlayer] += marbleSevenToLeft?.points ?: BigInteger.ZERO
        marbleSevenToLeft?.previous?.next = marbleSevenToLeft?.next
        marbleSevenToLeft?.next?.previous = marbleSevenToLeft?.previous
        currentMarble = marbleSevenToLeft?.next ?: currentMarble
    }

    private fun insertThisMarbleAfterNext(marbleNumber: Int) {
        val nextMarble = currentMarble.next
        val marbleAfterNext = nextMarble?.next
        val newMarble = Marble(nextMarble, marbleAfterNext, BigInteger.valueOf(marbleNumber.toLong()))
        nextMarble?.next = newMarble
        marbleAfterNext?.previous = newMarble
        currentMarble = newMarble
    }
}


class Day9Test {

    private lateinit var day9: Day9

    @Before
    fun setUp() {
        day9 = Day9()
    }

    @Test
    fun checkSetUp() {
        day9.setUp("10 players; last marble is worth 1618 points")
        assertEquals(10, day9.numberOfPlayers)
        assertEquals(1618, day9.numberOfMarbles)
    }

    @Test
    fun check1() {
        runTest(BigInteger.valueOf(32), 9, 25)
    }

    @Test
    fun check2() {
        runTest(BigInteger.valueOf(8317), 10, 1618)
    }

    @Test
    fun check3() {
        runTest(BigInteger.valueOf(146373), 13, 7999)
    }

    @Test
    fun check4() {
        runTest(BigInteger.valueOf(2764), 17, 1104)
    }

    @Test
    fun check5() {
        runTest(BigInteger.valueOf(54718), 21, 6111)
    }

    @Test
    fun check6() {
        runTest(BigInteger.valueOf(37305), 30, 5807)
    }

    @Test
    fun checkPart1() {
        runTest(BigInteger.valueOf(367634), 479, 71035)
    }

    @Test
    fun checkPart2() {
        runTest(BigInteger.valueOf(30200728910), 479, 71035 * 100)
    }

    private fun runTest(expected: BigInteger, players: Int, points: Int) {
        assertEquals(expected, day9.getWinningScore("$players players; last marble is worth $points points"))
    }

}