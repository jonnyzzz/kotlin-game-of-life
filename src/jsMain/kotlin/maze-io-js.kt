package org.jonnyzzz.lifegame

import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.PI

fun Maze3.render(ctx: CanvasRenderingContext2D) {
  val leftImage = ctx.canvas
  val stepX = leftImage.width.toDouble() / this.width
  val stepY = leftImage.height.toDouble() / this.height

  val rX = (stepX - 1) / 2
  val rY = (stepY - 1) / 2

  ctx.clearRect(0.0, 0.0, leftImage.width.toDouble(), leftImage.height.toDouble())
  ctx.beginPath()
  forEachAlive { x, y ->
    ctx.beginPath()
    ctx.ellipse(x * stepX + rX, y * stepY + rY, rX, rY, 0.0, 0.0, 2 * PI)
    ctx.fill()
  }
  ctx.fill()
  ctx.closePath()
}

