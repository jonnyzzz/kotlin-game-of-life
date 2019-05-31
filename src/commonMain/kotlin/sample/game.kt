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
    println(m.renderToString())
    m = m.nextGeneration(EvolutionCell::conwayLaws)
  }
}

/**
 * Conway Game of Life rules
 * see https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules
 */
fun EvolutionCell.conwayLaws() = when (state) {
  CellState.ALIVE -> when (neighbours) {
    2, 3 -> CellState.ALIVE   // living on
    else -> CellState.DEAD    // underpopulation or overpopulation
  }

  CellState.DEAD -> when (neighbours) {
    3 -> CellState.ALIVE     // reproduction
    else -> CellState.DEAD
  }
}
