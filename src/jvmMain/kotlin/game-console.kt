package org.jonnyzzz.lifegame.console

import org.jonnyzzz.lifegame.*
import java.io.File
import java.io.FileOutputStream
import javax.imageio.stream.MemoryCacheImageOutputStream

fun main() {
  val file = File("build/render.gif").canonicalFile

  println("Rendering to $file")

  val images = sequence {
    var world = randomMaze(40, 40)
    var prevImage = world.toImage(800, 800)

    repeat(300) {
      println("Step $it...")
      world = world.nextGeneration(EvolutionCell::conwayLaws)
      prevImage = world.renderToImage(prevImage.addAging())
      yield(prevImage)
    }
  }

  FileOutputStream(file).use {
    MemoryCacheImageOutputStream(it).use { os ->
      createGIF(os, images)
    }
  }

  println("Use $file")
  Runtime.getRuntime().exec("open $file")
}

