package org.jonnyzzz.game


class Maze3(val width: Int, val height: Int) : MazeWorld {
  private val state = Array(height) { Array(width) { CellState.DEAD } }

  operator fun get(x: Int, y: Int) = state[y][x]
  operator fun set(x: Int, y: Int, value: CellState) {
    state[y][x] = value
  }

  private fun countNeighbors(x: Int, y: Int): Int {
    var count = 0

    for (yy in (y - 1)..(y + 1)) {
      for (xx in (x - 1)..(x + 1)) {
        if (xx == x && yy == y) continue
        if (xx !in 0 until width) continue
        if (yy !in 0 until height) continue

        if (this[xx, yy] == CellState.ALIVE) {
          count++
        }
      }
    }

    return count
  }

  override fun nextGeneration(evolutionRule: EvolutionCell.() -> CellState): Maze3 {
    val copy = Maze3(width, height)
    for (y in 0 until height) {
      for (x in 0 until width) {

        val cell = object : EvolutionCell {
          override val neighbours by lazy {
            countNeighbors(x, y)
          }

          override val state by lazy { get(x, y) }
        }

        copy[x, y] = cell.evolutionRule()
      }
    }
    return copy
  }
}

