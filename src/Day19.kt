import org.junit.Before
import org.junit.Test

class Day19 {



    fun loadFile(fileName: String) {

    }
}


class Day19test {

    lateinit var day19 : Day19

    @Before
    fun setUp() {
        day19 = Day19()
    }

    @Test
    fun checkLoad() {
        day19.loadFile("Data/Day19/day19-small.txt")
    }

}