package org.jonnyzzz.lifegame


fun String.toMaze() = loadMaze(this)

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

