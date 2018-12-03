import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

data class Claim(val id: Int, val xOffset: Int, val yOffset: Int, val xSize: Int, val ySize: Int)


class Day03 {

    val claims = mutableListOf<Claim>()

    lateinit var fabric: IntArray


    var xSize = 0
    var ySize = 0

    fun loadData(filename: String) {
        val dr = "#([0-9]*) @ ([0-9]*),([0-9]*): ([0-9]*)x([0-9]*)".toRegex()
        File(filename).readLines().forEach {
            claims.add(dr.matchEntire(it)
                ?.destructured
                ?.let { (id, xOffset, yOffset, xSize, ySize) ->
                    Claim(id.toInt(), xOffset.toInt(), yOffset.toInt(), xSize.toInt(), ySize.toInt())
                }
                ?: throw IllegalArgumentException("Invalid Input"))
        }
    }

    fun setArraySize() {
        claims.forEach {
            if (it.xOffset + it.xSize > xSize) xSize = it.xOffset + it.xSize
            if (it.yOffset + it.ySize > ySize) ySize = it.yOffset + it.ySize
        }
        fabric = IntArray(xSize * ySize)
    }

    fun getArrayPosition(x: Int, y: Int): Int {
        return x * ySize + y
    }

    fun processFile(): Int { // #1 @ 1,3: 4x4
        claims.forEach {
            for (x in it.xOffset until it.xOffset + it.xSize)
                for (y in it.yOffset until it.yOffset + it.ySize)
                    fabric[getArrayPosition(x, y)] += 1
        }

        return fabric.count { it > 1 }
    }

    fun getNonOverlappingClaimNumber(): Int {
        var goodClaimId = 0
        claims.forEach {
            var goodClaim = true
            for (x in it.xOffset until it.xOffset + it.xSize)
                for (y in it.yOffset until it.yOffset + it.ySize)
                    if (fabric[getArrayPosition(x, y)] != 1) goodClaim = false
            if (goodClaim) {
                goodClaimId = it.id
                return@forEach
            }
        }

        return goodClaimId
    }

    fun getNonOverlappingClaimNumber2(): Int {
        for (c in claims) {
            var goodClaim = true
            for (x in c.xOffset until c.xOffset + c.xSize)
                for (y in c.yOffset until c.yOffset + c.ySize)
                    if (fabric[getArrayPosition(x, y)] != 1) goodClaim = false
            if (goodClaim) {
                return c.id
            }
        }
        return 0
    }

}

class Day03Test {

    lateinit var day3: Day03

    @Before
    fun setUp() {
        day3 = Day03()
    }

    @Test
    fun checkLoadingWorks() {
        day3.loadData("Day03-input-files/small-data.txt")
        assertEquals(3, day3.claims.size)
        day3.claims.forEach { println(it) }
    }

    @Test
    fun checkMaxSize() {
        day3.loadData("Day03-input-files/small-data.txt")
        day3.setArraySize()
        assertEquals(7, day3.xSize)
        assertEquals(7, day3.ySize)
        assertEquals(49, day3.fabric.size)
    }

    @Test
    fun checkGetArrayPosition() {
        day3.xSize = 6
        day3.ySize = 10
        assertEquals(0, day3.getArrayPosition(0, 0))
        assertEquals(9, day3.getArrayPosition(0, 9))
        assertEquals(10, day3.getArrayPosition(1, 0))
        assertEquals(60, day3.getArrayPosition(6, 0))
        assertEquals(69, day3.getArrayPosition(6, 9))
    }

    @Test
    fun checkProcessFile() {
        day3.loadData("Day03-input-files/small-data.txt")
        day3.setArraySize()
        assertEquals(4, day3.processFile())
    }

    @Test
    fun checkProcessBigFile() {
        day3.loadData("Day03-input-files/day3-data.txt")
        day3.setArraySize()
        assertEquals(108961, day3.processFile())
    }

    @Test
    fun checkNonOverlappingSmallFile() {
        day3.loadData("Day03-input-files/small-data.txt")
        day3.setArraySize()
        day3.processFile()
        assertEquals(3, day3.getNonOverlappingClaimNumber())
    }

    @Test
    fun checkNonOverlappingBigFile() {
        day3.loadData("Day03-input-files/day3-data.txt")
        day3.setArraySize()
        day3.processFile()
        assertEquals(681, day3.getNonOverlappingClaimNumber())
    }

    @Test
    fun checkNonOverlappingBigFile2() {
        day3.loadData("Day03-input-files/day3-data.txt")
        day3.setArraySize()
        day3.processFile()
        assertEquals(681, day3.getNonOverlappingClaimNumber2())
    }
}