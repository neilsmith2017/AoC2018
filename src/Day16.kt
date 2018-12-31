import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

data class Registers(val r: List<Int>)
data class Instruction(val opCode: Int, val p1: Int, val p2: Int, val r: Int)

data class Operation(val before: Registers, val op: Instruction, val after: Registers)

class Day16 {

    val operations = mutableListOf<Operation>()

    fun loadData(fileName: String) {
        lateinit var before: Registers
        lateinit var after: Registers
        lateinit var instruction: Instruction
        File(fileName).readLines().forEach {
            when {
                it.startsWith("Before") -> {
                    before = Registers(parseRegs(it))
                }
                it.startsWith("After") -> {
                    after = Registers(parseRegs(it))
                    operations.add(Operation(before, instruction, after))
                }
                it.isNotEmpty() -> {
                    instruction = parseInstruction(it)
                }
            }
        }
    }

    private fun parseInstruction(it: String): Instruction {
        var i = it.split(" ")
        return Instruction(i[0].toInt(), i[1].toInt(), i[2].toInt(), i[3].toInt())
    }

    private fun parseRegs(it: String): List<Int> {
        val r = mutableListOf<Int>()
        r.add(it.substring(9, 10).toInt())
        r.add(it.substring(12, 13).toInt())
        r.add(it.substring(15, 16).toInt())
        r.add(it.substring(18, 19).toInt())
        return r
    }

    fun addr(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] + r.r[i.p2]
        return Registers(regs)
    }

    fun addi(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] + i.p2
        return Registers(regs)
    }

    fun mulr(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] * r.r[i.p2]
        return Registers(regs)
    }

    fun muli(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] * i.p2
        return Registers(regs)
    }

    fun banr(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] and r.r[i.p2]
        return Registers(regs)
    }

    fun bani(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] and i.p2
        return Registers(regs)
    }

    fun borr(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] or r.r[i.p2]
        return Registers(regs)
    }

    fun bori(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1] or i.p2
        return Registers(regs)
    }

    fun setr(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = r.r[i.p1]
        return Registers(regs)
    }

    fun seti(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = i.p1
        return Registers(regs)
    }

    fun gtir(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = if (i.p1 > r.r[i.p2]) 1 else 0
        return Registers(regs)
    }

    fun gtri(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = if (r.r[i.p1] > i.p2) 1 else 0
        return Registers(regs)
    }

    fun gtrr(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = if (r.r[i.p1] > r.r[i.p2]) 1 else 0
        return Registers(regs)
    }

    fun eqir(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = if (i.p1 == r.r[i.p2]) 1 else 0
        return Registers(regs)
    }

    fun eqri(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = if (r.r[i.p1] == i.p2) 1 else 0
        return Registers(regs)
    }

    fun eqrr(r: Registers, i: Instruction): Registers {
        val regs = mutableListOf<Int>()
        regs.addAll(r.r)
        regs[i.r] = if (r.r[i.p1] == r.r[i.p2]) 1 else 0
        return Registers(regs)
    }

    fun getResultCountForAllOpcodes(input: Registers, instruction: Instruction, output : Registers): Boolean {
        val results = mutableListOf<Registers>()
        results.add(addr(input, instruction))
        results.add(addi(input, instruction))
        results.add(mulr(input, instruction))
        results.add(muli(input, instruction))
        results.add(setr(input, instruction))
        results.add(seti(input, instruction))
        results.add(bani(input, instruction))
        results.add(banr(input, instruction))
        results.add(bori(input, instruction))
        results.add(borr(input, instruction))
        results.add(gtir(input, instruction))
        results.add(gtri(input, instruction))
        results.add(gtrr(input, instruction))
        results.add(eqir(input, instruction))
        results.add(eqrr(input, instruction))
        results.add(eqri(input, instruction))

        val x = results.filter { it.r == output.r }.count() >= 3
        return x
    }

}


class Day16Test {

    lateinit var day16: Day16

    @Before
    fun setUp() {
        day16 = Day16()
    }

    @Test
    fun checkPart1() {
        day16.loadData("Data/Day16/day16-opcodes.txt")
        var count = 0
        repeat(day16.operations.size) {
            if (day16.getResultCountForAllOpcodes(day16.operations[it].before, day16.operations[it].op, day16.operations[it].after)) count++
        }
        assertEquals(521, count)
        // 582 too high
    }

    @Test
    fun check1() {
        assertEquals(true, day16.getResultCountForAllOpcodes(Registers(listOf(3, 2, 1, 1)), Instruction(9, 2, 1, 2), Registers(listOf(3, 2, 2, 1))))
    }

    @Test
    fun checkLoad() {
        day16.loadData("Data/Day16/day16-opcodes.txt")
        println(day16.operations)
        println(day16.operations.size)
    }
}