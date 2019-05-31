package org.jonnyzzz.lifegame

import kotlin.random.Random

fun randomMaze(width: Int, height: Int, p: Double = 0.3) = Maze3(width, height).apply {
  for (y in 0 until height) {
    for (x in 0 until width) {
      if (Random.nextDouble() <= p) {
        this[x, y] = CellState.ALIVE
      }
    }
  }
}

