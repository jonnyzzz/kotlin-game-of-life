package org.jonnyzzz.game


class Maze3(val width: Int, val height: Int) {
  private val state = Array(height) { Array(width) { CellState.DEAD } }

  operator fun get(x: Int, y: Int) = state[y][x]
  operator fun set(x: Int, y: Int, value: CellState) {
    state[y][x] = value
  }
}


fun Maze3.iterate(rules: Maze3.(Int, Int) -> CellState) : Maze3 {
  val copy = Maze3(width, height)
  for (y in 0 until height) {
    for (x in 0 until width) {
      copy[x, y] = rules(x, y)
    }
  }
  return copy
}


