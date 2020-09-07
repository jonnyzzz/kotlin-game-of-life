package org.jonnyzzz.lifegame

import org.jonnyzzz.lifegame.CellState.ALIVE
import org.jonnyzzz.lifegame.CellState.DEAD


/**
 * Conway Game of Life rules
 * see https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules
 */
fun EvolutionCell.conwayLaws() = when (state) {
  ALIVE -> when (neighbours) {
    2, 3 -> ALIVE  // living on
    else -> DEAD   // underpopulation or overpopulation
  }

  DEAD -> when (neighbours) {
    3 -> ALIVE     // reproduction
    else -> DEAD
  }
}



