package org.jonnyzzz.lifegame

import kotlinx.coroutines.*
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.hasClass
import kotlin.dom.removeClass
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


val MainScope = MainScope()

val leftImage : HTMLCanvasElement by document
val preImage : HTMLPreElement by document

inline operator fun <reified T> Document.getValue(x: Any?, kProperty: KProperty<*>) = getElementById(kProperty.name) as T

var world by Delegates.observable(glider.toSize(40, 40)) { _, _, new ->
  MainScope.launch {
    preImage.innerText = new.toSize(40, 25).renderToString()
  }

  MainScope.launch {
    val ctx = leftImage.getContext("2d") as CanvasRenderingContext2D
    val stepX = leftImage.width.toDouble() / new.width
    val stepY = leftImage.height.toDouble() / new.height

    ctx.clearRect(0.0, 0.0, leftImage.width.toDouble(), leftImage.height.toDouble())
    ctx.beginPath()
    new.forEachAlive { x, y ->
      ctx.rect(x * stepX, y * stepY, stepX, stepY)
    }
    ctx.fill()
    ctx.closePath()
  }
}


fun nextStep() = MainScope.launch {
  world = world.nextGeneration(EvolutionCell::conwayLaws)
}

var autoPlay : Job? = null

fun toggleAutoplay() = MainScope.launch {
  if (autoPlay != null)  {
    autoPlay?.cancel()
    autoPlay = null
  } else {
    autoPlay = launch {
      while (true) {
        nextStep().join()
        delay(222)
      }
    }
  }
}

@Suppress("unused")
@JsName("initTheGame")
fun renderHTML() = MainScope.launch {
  document.getElementById("content")!!.append {
    val size = "400px"
    div(classes = "controls btn-block"){
      button {
        +"Init Random"
        classes = setOf("btn", "btn-primary")
        onClickFunction = { world = randomMaze(40, 40) }
      }

      button {
        +"Next Generation"
        classes = setOf("btn", "btn-primary")
        onClickFunction = { nextStep() }
      }

      button {
        +"Toggle Autoplay"
        classes = setOf("btn", "btn-primary")
        onClickFunction = { toggleAutoplay() }
      }

      button {
        +"Toggle Console Mode"
        classes = setOf("btn", "btn-primary")
        onClickFunction = { preImage.toggleClass("shown") }
      }
    }

    div(classes = "preImages") {
      pre {
        id = ::preImage.name
      }
    }

    div(classes = "images") {
      canvas {
        id = ::leftImage.name
        width = size
        height = size
      }
    }

  }

  yield()
  nextStep()
}

fun Element.toggleClass(name: String) {
  if (hasClass(name)) {
    removeClass(name)
  } else {
    addClass(name)
  }
}
