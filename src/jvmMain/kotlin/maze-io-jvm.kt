package org.jonnyzzz.lifegame

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import kotlin.math.min

fun Maze3.toImage(width: Int, height: Int): BufferedImage {
  val img = BufferedImage(width, height, TYPE_INT_RGB)

  val ctx = img.graphics
  ctx.color = Color.white
  ctx.fillRect(0, 0, width, height)

  renderToImage(img)
  return img
}

fun Maze3.renderToImage(image: BufferedImage): BufferedImage {
  renderImage(this, image)
  return image
}

private fun renderImage(maze: Maze3, image: BufferedImage) {
  val stepX = image.width / maze.width
  val stepY = image.height / maze.height

  val ctx = image.graphics
  ctx.color = Color.BLACK
  maze.forEachAlive { x, y ->
    ctx.fillRoundRect(x * stepX, y * stepY, stepX - 1, stepY - 1, stepX, stepY)
  }
}

fun BufferedImage.addAging() = apply {
  for (x in 0 until width) {
    for (y in 0 until height) {
      val p = Color(getRGB(x, y))
      if (p != Color.WHITE) {
        setRGB(
          x, y, Color(
            min(255, p.red * 5 / 8 + 133),
            min(255, p.green * 5 / 8 + 133),
            min(255, p.blue * 5 / 8 + 133)
          ).rgb
        )
      }
    }
  }
}
