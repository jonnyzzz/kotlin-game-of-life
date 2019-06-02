@file:Suppress("unused")

package org.jonnyzzz.lifegame

import kotlin.properties.Delegates

class WorldWrapper(
  val onWorldChange: (String) -> Unit
) {
  private fun initIOS() = randomMaze(40, 40)

  private var world by Delegates.observable(initIOS()) { _, _, it ->
    onWorldChange(it.renderToString())
  }

  fun initWorld() {
    world = initIOS()
  }

  fun iterateWorld() {
    world = world.nextGeneration(EvolutionCell::conwayLaws)
  }
}




