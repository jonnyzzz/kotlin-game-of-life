package org.jonnyzzz.lifegame

import kotlin.properties.Delegates

private fun initIOS() = randomMaze(40, 40)

class WorldWrapper(
  val onWorldChange: (String) -> Unit
) {

  private var world by Delegates.observable(initIOS()) { _, _, new ->
    onWorldChange(new.renderToString())
  }

  fun initWorld() {
    world = initIOS()
  }

  fun iterateWorld() {
    world = world.nextGeneration(EvolutionCell::conwayLaws)
  }
}




