import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals


data class Instruction19(val opcode: String, val p1: Int, val p2: Int, val r: Int)


class Day19 {

    val instructions = mutableListOf<Instruction19>()
    val instructionMap = mutableMapOf<String, (IntArray, Instruction19) -> IntArray>()

    var ipRegister = 0

    private fun addr(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] + r[i.p2] }

    private fun addi(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] + i.p2 }

    private fun mulr(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] * r[i.p2] }

    private fun muli(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] * i.p2 }

    private fun banr(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = r[i.p1] and r[i.p2] }

    private fun bani(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] and i.p2 }

    private fun borr(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = r[i.p1] or r[i.p2] }

    private fun bori(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] or i.p2 }

    private fun setr(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] }

    private fun seti(r: IntArray, i: Instruction19): IntArray = r.copyOf().apply { this[i.r] = i.p1 }

    private fun gtir(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = if (i.p1 > r[i.p2]) 1 else 0 }

    private fun gtri(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = if (r[i.p1] > i.p2) 1 else 0 }

    private fun gtrr(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = if (r[i.p1] > r[i.p2]) 1 else 0 }

    private fun eqir(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = if (i.p1 == r[i.p2]) 1 else 0 }

    private fun eqri(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = if (r[i.p1] == i.p2) 1 else 0 }

    private fun eqrr(r: IntArray, i: Instruction19): IntArray =
        r.copyOf().apply { this[i.r] = if (r[i.p1] == r[i.p2]) 1 else 0 }

    fun loadFile(fileName: String) {
//        instructions.add(Instruction19("dummy",0,0,0))
        File(fileName).readLines().forEach {
            when {
                it.startsWith("#") -> {
//                    instructions.add(Instruction19Ip(it.substring(it.length - 1).toInt()))
                    ipRegister = it.substring(it.length - 1).toInt()
                }
                else -> {
                    instructions.add(parseRegs(it))
                }
            }
        }
    }

    private fun parseRegs(it: String): Instruction19 {
        val split = it.split(" ")
        return Instruction19(
            split[0],
            split[1].toInt(),
            split[2].toInt(),
            split[3].toInt()
        )
    }

    fun loadFunctions() {
        instructionMap["addr"] = ::addr
        instructionMap["addi"] = ::addi
        instructionMap["mulr"] = ::mulr
        instructionMap["muli"] = ::muli
        instructionMap["banr"] = ::banr
        instructionMap["bani"] = ::bani
        instructionMap["bori"] = ::bori
        instructionMap["borr"] = ::borr
        instructionMap["gtir"] = ::gtir
        instructionMap["gtri"] = ::gtri
        instructionMap["gtrr"] = ::gtrr
        instructionMap["setr"] = ::setr
        instructionMap["seti"] = ::seti
        instructionMap["eqir"] = ::eqir
        instructionMap["eqri"] = ::eqri
        instructionMap["eqrr"] = ::eqrr
    }

    fun runProgram(startValue: Int): IntArray {
        var registers = intArrayOf(startValue, 0, 0, 0, 0, 0)
        var ipValue = 0
        var count = 0

        while (true) {
            count++
            if (count % 1000000 == 0) println("count = $count")
            val i = instructions[ipValue]

            registers[ipRegister] = ipValue
//            print("ip=$ipValue [${registers.joinToString()}] ${i} ")
            registers = instructionMap[i.opcode]?.let {
                it(registers, i)
            } ?: intArrayOf(0, 0, 0, 0, 0, 0)

            ipValue = registers[ipRegister]
//            println("[${registers.joinToString()}]")
            ipValue++
//                    println("ipValue = $ipValue")
            if (ipValue < 0 || ipValue >= instructions.size) {
                println(count)
                return registers

            }
        }
        return registers
    }
}


class Day19test {

    lateinit var day19: Day19

    @Before
    fun setUp() {
        day19 = Day19()
        day19.loadFunctions()
    }

    @Test
    fun runBig2() {
        day19.loadFile("Data/Day19/day19-big.txt")
        val result = day19.runProgram(1)
        println("${result.joinToString()}")
        assertEquals(10551378, result[0])
    }

    @Test
    fun runBig() {
        day19.loadFile("Data/Day19/day19-big.txt")
        val result = day19.runProgram(0)
        println("${result.joinToString()}")
        assertEquals(1968, result[0])
    }

    @Test
    fun runSmall() {
        day19.loadFile("Data/Day19/day19-small.txt")
        println("${day19.runProgram(0).joinToString()}")
    }

    @Test
    fun checkLoad() {
        day19.loadFile("Data/Day19/day19-small.txt")
        assertEquals(8, day19.instructions.size)
    }

}