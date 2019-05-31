package org.jonnyzzz.game


fun loadMaze(s: String): Maze3 {
  val rows = s.split("\n").map { it.trim() }.filter { it.isNotBlank() }

  val height = rows.size
  val width = rows.map { it.length }.max() ?: 0

  return Maze3(width, height).apply {
    rows.forEachIndexed { y, row ->
      row.forEachIndexed { x, ch ->
        if (ch != '.') {
          this[x, y] = CellState.ALIVE
        }
      }
    }
  }
}

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
