package org.jonnyzzz.lifegame

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.PI

fun Maze3.render(canvas: HTMLCanvasElement) = render(this, canvas)

private fun render(maze: Maze3, canvas: HTMLCanvasElement) {

  val width = canvas.width.toDouble()
  val height = canvas.height.toDouble()

  val stepX = width / maze.width
  val stepY = height / maze.height

  val rX = (stepX - 1) / 2
  val rY = (stepY - 1) / 2

  with(canvas.getContext("2d") as CanvasRenderingContext2D) {
    clearRect(0.0, 0.0, width, height)
    maze.forEachAlive { x, y ->
      beginPath()
      ellipse(x * stepX + rX, y * stepY + rY, rX, rY, 0.0, 0.0, 2 * PI)
      fill()
    }
  }

}

