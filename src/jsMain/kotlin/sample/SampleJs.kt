package sample

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import kotlin.browser.*

actual object Platform {
    actual val name: String = "JS"
}

actual fun render(w: World) {
    val canvas = document.getElementById("conway-canvas") as HTMLCanvasElement
    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    ctx.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
    for(x in 0 until w.width) {
        for(y in 0 until w.height) {
            if(w.get(x,y) == CellState.ALIVE) {
                ctx.beginPath()
                ctx.rect(x * 10.0, y * 10.0, 10.0, 10.0)
                ctx.fill()
                ctx.closePath()
            }
        }
    }
}

var world = populatedWorld
var simStep = 0
var image: HTMLImageElement? = null

@JsName("setupUI")
fun setupUI() {
    val step = document.getElementById("step-simulation") as HTMLButtonElement
    step.onclick = {
        simulationStep()
    }

    image = document.getElementById("conway-image") as HTMLImageElement

    render(world)
    setImage(0)
}

@JsName("simulationStep")
fun simulationStep() {
    simStep++
    world = world.conway()
    render(world)
    setImage(simStep)
}

fun setImage(simStep: Int) {
    image?.src = "/postcard/$simStep"
}