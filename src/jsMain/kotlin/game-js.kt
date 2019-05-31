package org.jonnyzzz.lifegame

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Document
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


val MainScope = MainScope()

val leftImage : HTMLCanvasElement by document
val rightImage : HTMLImageElement by document

inline operator fun <reified T> Document.getValue(x: Any?, kProperty: KProperty<*>) = getElementById(kProperty.name) as T

var world by Delegates.observable(glider.toSize(100, 100)) { _, _, new ->
  MainScope.launch {
    val ctx = leftImage.getContext("2d") as CanvasRenderingContext2D
    val stepX = leftImage.width.toDouble() / new.width
    val stepY = leftImage.height.toDouble() / new.height

    ctx.clearRect(0.0, 0.0, leftImage.width.toDouble(), leftImage.height.toDouble())
    ctx.beginPath()
    new.forEachAlive { x, y -> ctx.rect(x * stepX, y * stepY, stepX, stepY) }
    ctx.fill()
    ctx.closePath()
  }
}

fun updateLeftImage() = MainScope.launch {
  world = world.nextGeneration(EvolutionCell::conwayLaws)
}

@Suppress("unused")
@JsName("initTheGame")
fun renderHTML() = MainScope.launch {
  document.getElementById("content")!!.append {
    h3 {
      +"Multiplatform Game of Life"
    }
    p {
      +"Left: JS/Canvas | Right: JVM/BufferedImage (PNG)"
    }

    val size = "400px"
    div(classes = "images") {
      canvas {
        id = ::leftImage.name
        width = size
        height = size
      }
      canvas {
        id = ::rightImage.name
        width = size
        height = size
      }
    }

    button {
      +"Step simulation"
      onClickFunction = { updateLeftImage() }
    }
  }

  updateLeftImage()
}


//
//fun render(w: World) {
//
//}
//
//var world = populatedWorld
//var simStep = 0
//var image: HTMLImageElement? = null
//
//@JsName("setupUI")
//fun setupUI() {
//    val step = document.getElementById("step-simulation") as HTMLButtonElement
//    step.onclick = {
//        simulationStep()
//    }
//
//    image = document.getElementById("conway-image") as HTMLImageElement
//
//    render(world)
//    setImage(0)
//}
//
//@JsName("simulationStep")
//fun simulationStep() {
//    simStep++
//    world = world.conway()
//    render(world)
//    setImage(simStep)
//}
//
//fun setImage(simStep: Int) {
//    image?.src = "/postcard/$simStep"
//}
