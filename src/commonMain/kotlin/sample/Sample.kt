package sample

enum class CellState {
    DEAD,
    ALIVE
}

val emptyWorld = World(Array(10) {
    Array(10) {
        CellState.DEAD
    }
})

val populatedWorld =
    emptyWorld
        .set(4, 3, CellState.ALIVE)
        .set(5, 4, CellState.ALIVE)
        .set(5, 5, CellState.ALIVE)
        .set(3, 5, CellState.ALIVE)
        .set(4, 5, CellState.ALIVE)

class World(private val matrix: Array<Array<CellState>>) {
    val height = 10 //y direction
    val width = 10 //x direction

    fun forEach(f: (x: Int, y: Int, cellState: CellState) -> Unit) {
        matrix.forEachIndexed { y, line ->
            line.forEachIndexed { x, cellState ->
                f(x, y, cellState)
            }
        }
    }

    fun forEachAlive(f: (x: Int, y: Int) -> Unit) {
        forEach { x, y, cellState ->
            if (cellState == CellState.ALIVE) {
                f(x, y)
            }
        }
    }

    fun countNeighbors(x: Int, y: Int): Int {
        var aliveNeighbors = 0
        for (column in (x - 1)..(x + 1)) {
            for (row in (y - 1)..(y + 1)) {
                if (column == x && row == y) continue
                if (column !in 0 until width) continue
                if (row !in 0 until height) continue
                if (get(column, row) == CellState.ALIVE) {
                    aliveNeighbors++
                }
            }
        }
        return aliveNeighbors
    }

    fun set(x: Int, y: Int, state: CellState): World {
        val newMatrix = matrix.map { it.copyOf() }.toTypedArray()
        newMatrix[y][x] = state
        return World(newMatrix)
    }

    fun get(x: Int, y: Int): CellState {
        return matrix[y][x]
    }

    fun map(ruleset: (x: Int, y: Int, state: CellState) -> CellState): World {
        var newWorld = emptyWorld
        forEach { x, y, cellState -> newWorld = newWorld.set(x, y, ruleset(x, y, cellState)) }
        return newWorld
    }

}

fun World.conway() = map { x, y, state ->
    when (state) {
        CellState.ALIVE -> when (countNeighbors(x, y)) {
            in 2..3 -> CellState.ALIVE // living on
            else -> CellState.DEAD // underpopulation or overpopulation
        }
        CellState.DEAD -> when (countNeighbors(x, y)) {
            3 -> CellState.ALIVE // reproduction
            else -> CellState.DEAD
        }
    }
}

fun World.toAscii(): String {
    val sb = StringBuilder()
    for (y in 0 until height) {
        for (x in 0 until width) {
            sb.append(
                when (get(x, y)) {
                    CellState.DEAD -> "❌"
                    CellState.ALIVE -> "✅"
                }
            )
        }
        sb.append("\n")
    }
    return sb.toString()
}

fun main() {
    val world = populatedWorld
    println(world.toAscii())
}
