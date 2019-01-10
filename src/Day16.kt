import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

data class Instruction(val opCode: Int, val p1: Int, val p2: Int, val r: Int)

data class Operation(val before: IntArray, val op: Instruction, val after: IntArray)



class Day16 {
    private fun addr(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] + r[i.p2] }

    private fun addi(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] + i.p2 }

    private fun mulr(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] * r[i.p2] }

    fun muli(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] * i.p2 }

    fun banr(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] and r[i.p2] }

    fun bani(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] and i.p2 }

    fun borr(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] or r[i.p2] }

    fun bori(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] or i.p2 }

    fun setr(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = r[i.p1] }

    fun seti(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = i.p1 }

    fun gtir(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = if (i.p1 > r[i.p2]) 1 else 0 }

    fun gtri(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = if (r[i.p1] > i.p2) 1 else 0 }

    fun gtrr(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = if (r[i.p1] > r[i.p2]) 1 else 0 }

    fun eqir(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = if (i.p1 == r[i.p2]) 1 else 0 }

    fun eqri(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = if (r[i.p1] == i.p2) 1 else 0 }

    fun eqrr(r: IntArray, i: Instruction): IntArray = r.copyOf().apply { this[i.r] = if (r[i.p1] == r[i.p2]) 1 else 0 }

    val instructionMap = mutableMapOf<String, (IntArray, Instruction) -> IntArray>()
    val operations = mutableListOf<Operation>()

    val opCodeMap = mutableMapOf<Int, String>()
    lateinit var program: List<Instruction>

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

    fun loadData(fileName: String) {
        lateinit var before: IntArray
        lateinit var after: IntArray
        lateinit var instruction: Instruction
        File(fileName).readLines().forEach {
            when {
                it.startsWith("Before") -> {
                    before = parseRegs(it)
                }
                it.startsWith("After") -> {
                    after = parseRegs(it)
                    operations.add(Operation(before, instruction, after))
                }
                it.isNotEmpty() -> {
                    instruction = parseInstruction(it)
                }
            }
        }
    }

    fun loadProgram(fileName: String) {
        val instructions = mutableListOf<Instruction>()

        File(fileName).readLines().forEach {
            instructions.add(parseInstruction(it))
        }

        program = instructions
    }

    private fun parseInstruction(it: String): Instruction {
        val i = it.split(" ")
        return Instruction(i[0].toInt(), i[1].toInt(), i[2].toInt(), i[3].toInt())
    }

    private fun parseRegs(it: String) = intArrayOf(
        it.substring(9, 10).toInt(),
        it.substring(12, 13).toInt(),
        it.substring(15, 16).toInt(),
        it.substring(18, 19).toInt()
    )



    fun getResultCountForAllOpCodes(
        input: IntArray,
        instruction: Instruction,
        output: IntArray
    ): Boolean {
        val results = mutableListOf<IntArray>()

        instructionMap["addr"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["addi"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["mulr"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["muli"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["setr"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["seti"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["banr"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["bani"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["borr"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["bori"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["gtir"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["gtri"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["gtrr"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["eqir"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["eqri"]?.let { it(input, instruction).let { res -> results.add(res) } }
        instructionMap["eqrr"]?.let { it(input, instruction).let { res -> results.add(res) } }

        return results.filter { it.contentEquals(output) }.count() >= 3
    }

    fun buildOpCodeMap() {
        operations.forEach { op ->
            val result = instructionMap
                .filter { instr -> instr.value(op.before, op.op).contentEquals(op.after) }
                .filterNot { instr -> opCodeMap.containsValue(instr.key) }
            if (result.size == 1) {
                opCodeMap[op.op.opCode] = result.keys.first()
            }

        }
    }

    fun runProgram(): IntArray {
        var registers = intArrayOf(0, 0, 0, 0)

        program.forEach { instruction ->
            registers =
                    instructionMap[opCodeMap[instruction.opCode]]?.let {
                        it(
                            registers,
                            instruction
                        )//.let { res -> res }
                    } ?: intArrayOf(0, 0, 0, 0)
        }

        return registers
    }

}


class Day16Test {

    private lateinit var day16: Day16

    @Before
    fun setUp() {
        day16 = Day16()
        day16.loadFunctions()
    }

    @Test
    fun runPart2() {
        day16.loadData("Data/Day16/day16-opcodes.txt")
        day16.loadProgram("Data/Day16/day16-instructions.txt")
        day16.buildOpCodeMap()
        val result = day16.runProgram()
        println("${result[0]} ${result[1]} ${result[2]} ${result[3]}")
        assertEquals(0, result[0])
    }

    @Test
    fun checkLoadProgram() {
        day16.loadProgram("Data/Day16/day16-instructions.txt")
        assertEquals(968, day16.program.size)
        println(day16.program[5])
    }

    @Test
    fun checkOpCodeMap() {
        day16.loadData("Data/Day16/day16-opcodes.txt")
        day16.buildOpCodeMap()
        println(day16.opCodeMap)
        assertEquals(16, day16.opCodeMap.size)
    }

    @Test
    fun checkPart1() {
        day16.loadData("Data/Day16/day16-opcodes.txt")
        var count = 0
        repeat(day16.operations.size) {
            if (day16.getResultCountForAllOpCodes(
                    day16.operations[it].before,
                    day16.operations[it].op,
                    day16.operations[it].after
                )
            ) count++
        }
        assertEquals(521, count)
        // 582 too high
    }

    @Test
    fun check1() {
        assertEquals(
            true,
            day16.getResultCountForAllOpCodes(
                intArrayOf(3, 2, 1, 1),
                Instruction(9, 2, 1, 2),
                intArrayOf(3, 2, 2, 1)
            )
        )
    }

    @Test
    fun checkLoad() {
        day16.loadData("Data/Day16/day16-opcodes.txt")
        println(day16.operations)
        println(day16.operations.size)
    }
}