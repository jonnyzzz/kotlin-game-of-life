package org.jonnyzzz.game

fun main() {


  val test = """
      ...........
      ...x.......
      ...x.......
      ...x.......
      ...........
  """


  var m = loadMaze(test)

  repeat(10) {
    println(m.asString())

    m = m.iterate(Maze3::evolution)

  }


}

fun Maze3.countNeighbors(x: Int, y: Int): Int {
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


fun Maze3.evolution(x: Int, y: Int): CellState {
  val cellState = this[x, y]
  val countNeighbors = countNeighbors(x, y)

  return when (cellState) {
    // https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules

    CellState.ALIVE -> when (countNeighbors) {
      2, 3 -> CellState.ALIVE   // living on
      else -> CellState.DEAD    // underpopulation or overpopulation
    }

    CellState.DEAD -> when (countNeighbors) {
      3 -> CellState.ALIVE     // reproduction
      else -> CellState.DEAD
    }
  }
}
