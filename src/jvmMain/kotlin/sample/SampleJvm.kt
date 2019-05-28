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
                        title("Multiplatform Game of Life")
                    }
                    body {
                        h3 {
                            +"Multiplatform Game of Life"
                        }
                        p {
                            +"Left: JS/Canvas | Right: JVM/BufferedImage (PNG)"
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
                graphics.color = Color.RED

                world.forEachAlive { x, y ->
                    graphics.fill3DRect(x * 10, y * 10, 10, 10, true)
                }

                val x = ByteArrayOutputStream().use {
                    ImageIO.write(b, "png", it)
                    it.toByteArray()
                }

                Thread.sleep(200)
                call.respondBytes(contentType = ContentType.Image.PNG, bytes = x)
            }

            static("/static") {
                files(webDir)
            }
        }
    }.start(wait = true)
}