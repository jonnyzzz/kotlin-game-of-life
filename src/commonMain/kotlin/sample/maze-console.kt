package org.jonnyzzz.game


private val emptyCell = "🕸"
private val aliveCells = listOf("🐜", "🐛", "🦗", "🐞", "🦠")

fun Maze3.asString() = buildString {
  for (y in 0 until height) {
    for (x in 0 until width) {
      append(
        when (get(x, y)) {
          CellState.ALIVE -> aliveCells.random()
          else -> emptyCell
        }
      )
    }
    append("\n")
  }
}

