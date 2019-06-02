package org.jonnyzzz.lifegame

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB

fun Maze3.toImage(width: Int, height: Int): BufferedImage {
  val img = BufferedImage(width, height, TYPE_INT_RGB)

  val stepX = width / this.width
  val stepY = height / this.height

  val ctx = img.createGraphics()
  ctx.color = Color.white
  ctx.fillRect(0, 0, width, height)
  ctx.color = Color.BLACK
  forEachAlive { x, y ->
    ctx.fillRect(x * stepX, y * stepY, stepX, stepY)
  }

  return img
}
