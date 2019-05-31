@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")
package org.jonnyzzz.game


inline class X(val x: Int)
inline class Y(val y: Int)

inline class CellId(val c: Long)  {
  val x: X get() = X(c.shr(32).toInt())
  val y: Y get() = Y(c.and(Int.MAX_VALUE.toLong()).toInt())
}

fun CellId(x: X, y: Y) = CellId(x.x.toLong().shl(32) + y.y)

class Space(width: Int, height: Int) {
  val xRange = 0 until width
  val yRange = 0 until height
}

interface Maze4 {
  val xRange: IntRange
  val yRange: IntRange

  fun isAlive(cell: CellId) : Boolean
}

class MazeImpl(width: Int, height: Int) : Maze4 {
  private val alive = mutableSetOf<CellId>()

  override val xRange = 0 until width
  override val yRange = 0 until height

  override fun isAlive(cell: CellId) = cell in alive
}

