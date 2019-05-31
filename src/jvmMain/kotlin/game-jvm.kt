package org.jonnyzzz.lifegame

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respondFile
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File

fun main() {
  embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
    val currentDir = File(".").absoluteFile
    environment.log.info("Current directory: $currentDir")

    val webDir = File(
      System.getProperty("jsWeb")
        ?: "cannot find path to jsWeb in the system property. Running via the Gradle run task?"
    )

    environment.log.info("Web directory: $webDir")

    install(CallLogging)

    routing {
      get("/") {
        call.respondFile(File(webDir, "index.html"))
      }

      static("/static") {
        files(webDir)
      }

/*            get("/ascii/{simStep}") {
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

                val imageBytes = ByteArrayOutputStream().use {
                    ImageIO.write(b, "png", it)
                    it.toByteArray()
                }

                delay(Duration.ofMillis(200))
                call.respondBytes(contentType = ContentType.Image.PNG, bytes = imageBytes)
            }*/


    }
  }.start(wait = true)
}


// https://memorynotfound.com/generate-gif-image-java-delay-infinite-loop-example/
