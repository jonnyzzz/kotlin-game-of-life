package sample

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_BGR
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.*
import javax.imageio.ImageIO

actual object Platform {
    actual val name: String = "JVM"
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        val currentDir = File(".").absoluteFile
        environment.log.info("Current directory: $currentDir")

        val webDir = listOf(
            "web",
            "../src/jsMain/web",
            "src/jsMain/web"
        ).map {
            File(currentDir, it)
        }.firstOrNull { it.isDirectory }?.absoluteFile ?: error("Can't find 'web' folder for this sample")

        environment.log.info("Web directory: $webDir")

        routing {
            get("/") {
                call.respondHtml {
                    head {
                        title("Hello from Ktor!")
                    }
                    body {
                        h3 {
                            +"Multiplatform Game of Life"
                        }
                        p {
                            +"${hello()} from Ktor."
                        }

                        script(src = "/static/require.min.js") {
                        }
                        script {
                            +"require.config({baseUrl: '/static'});\n"
                            +"require(['/static/conway.js'], function(js) { js.sample.setupUI(); });\n"
                        }
                        canvas {
                            id = "conway-canvas"
                            width = "100px"
                            height = "100px"
                        }
                        img {
                            id = "conway-image"
                            width = "100px"
                            height = "100px"
                        }
                        button {
                            id = "step-simulation"
                            +"Step simulation"
                        }
                    }
                }
            }

            get("/ascii/{simStep}") {
                val simStep = call.parameters["simStep"]?.toInt() ?: return@get
                var world = populatedWorld
                repeat(simStep) {
                    world = world.conway()
                }
                call.respondText {
                    world.toAscii()
                }
            }

            get("/postcard/{simStep}") {
                val simStep = call.parameters["simStep"]?.toInt() ?: return@get
                var world = populatedWorld
                repeat(simStep) {
                    world = world.conway()
                }
                val b = BufferedImage(100, 100, TYPE_INT_RGB)
                val graphics = b.createGraphics()
                for(x in 0 until world.width) {
                    for(y in 0 until world.height) {
                        if(world.get(x,y) == CellState.ALIVE) {
                            graphics.color = Color.RED
                            graphics.fill3DRect(x * 10, y * 10, 10, 10, true)
                        }
                    }
                }

                val bytes = ByteArrayOutputStream()
                ImageIO.write(b, "png", bytes)
                Thread.sleep(200)
                call.respondBytes(contentType = ContentType.Image.PNG) {
                    bytes.toByteArray()
                }
                bytes.close()
            }

            static("/static") {
                files(webDir)
            }
        }
    }.start(wait = true)
}

actual fun render(w: World) {

}