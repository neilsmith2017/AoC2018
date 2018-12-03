import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

data class Claim(val id: Int, val xOffset: Int, val yOffset: Int, val xSize: Int, val ySize: Int)


class Day03 {

    val claims = mutableListOf<Claim>()
    lateinit var fabric : IntArray


    fun loadData(filename: String) {
        val dr = "#([0-9]) @ ([0-9]),([0-9]): ([0-9])x([0-9])".toRegex()
        File(filename).readLines().forEach {
            claims.add(dr.matchEntire(it)
                ?.destructured
                ?.let { (id, xOffset, yOffset, xSize, ySize) ->
                    Claim(id.toInt(), xOffset.toInt(), yOffset.toInt(), xSize.toInt(), ySize.toInt())
                }
                ?: throw IllegalArgumentException("Invalid Input"))
        }
    }

    fun getSizeOfArray(): Pair<Int, Int> {
        var maxX = 0
        var maxY = 0
        claims.forEach {
            if (it.xOffset + it.xSize > maxX) maxX = it.xOffset + it.xSize
            if (it.yOffset + it.ySize > maxY) maxY = it.yOffset + it.ySize
        }
        return Pair(maxX, maxY)
    }

    fun initFabric(size : Pair<Int,Int>) {
        fabric = IntArray(size.first * size.second)
    }

    fun setMappingFunction(size : Pair<Int,Int>) {
        mapCoord = {(size.first, size.second)}

//                fun(size.first, size.second) -> 6 }
    }

}

class Day03Part1Test {

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
        assertEquals(Pair(7, 7), day3.getSizeOfArray())
    }
}