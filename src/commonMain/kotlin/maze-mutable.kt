package org.jonnyzzz.lifegame

import kotlin.math.min


class Maze3(val width: Int, val height: Int) : MazeWorld {
  private val state = Array(height) { Array(width) { CellState.DEAD } }

  operator fun get(x: Int, y: Int) = state[y][x]
  operator fun set(x: Int, y: Int, value: CellState) {
    state[y][x] = value
  }

  override fun nextGeneration(evolutionRule: EvolutionCell.() -> CellState): Maze3 {
    val copy = Maze3(width, height)
    for (y in 0 until height) {
      for (x in 0 until width) {

        val cell = object : EvolutionCell {
          override val neighbours by lazy {
            countAliveNeighbors(x, y)
          }

          override val state by lazy { get(x, y) }
        }

        copy[x, y] = cell.evolutionRule()
      }
    }
    return copy
  }
}

private fun Maze3.countAliveNeighbors(x: Int, y: Int): Int {
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


fun Maze3.forEachAlive(f: (x: Int, y: Int) -> Unit) {
  for (y in 0 until height) {
    for (x in 0 until width) {
      if (this[x, y] == CellState.ALIVE) {
        f(x, y)
      }
    }
  }
}


fun Maze3.toSize(width: Int = this.width, height: Int = this.height): Maze3 {
  val copy = Maze3(width, height)
  for (y in 0 until min(height, this.height)) {
    for (x in 0 until min(width, this.width)) {
      copy[x, y] = this[x, y]
    }
  }
  return copy
}
