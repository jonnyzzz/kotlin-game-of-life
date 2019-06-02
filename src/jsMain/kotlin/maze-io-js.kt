package org.jonnyzzz.lifegame

import org.w3c.dom.CanvasRenderingContext2D

fun Maze3.render(ctx: CanvasRenderingContext2D) {
  val leftImage = ctx.canvas
  val stepX = leftImage.width.toDouble() / this.width
  val stepY = leftImage.height.toDouble() / this.height

  ctx.clearRect(0.0, 0.0, leftImage.width.toDouble(), leftImage.height.toDouble())
  ctx.beginPath()
  forEachAlive { x, y ->
    ctx.rect(x * stepX, y * stepY, stepX, stepY)
  }
  ctx.fill()
  ctx.closePath()
}

