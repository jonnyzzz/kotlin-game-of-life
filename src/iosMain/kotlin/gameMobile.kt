package org.jonnyzzz.lifegame

import kotlin.properties.Delegates


class WorldWrapper(
  val onWorldChange: (String) -> Unit
) {

  private var world by Delegates.observable(randomMaze(200, 200)) { _, _, new ->
    onWorldChange(new.renderToString())
  }

  fun initWorld() {
    world = randomMaze(200, 200)
  }

  fun iterateWorld() {
    world = world.nextGeneration(EvolutionCell::conwayLaws)
  }
}




