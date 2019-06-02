package org.jonnyzzz.lifegame

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.ContentType
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respondBytes
import io.ktor.response.respondFile
import io.ktor.response.respondOutputStream
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.MemoryCacheImageOutputStream

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

      fun buildWorld(call: ApplicationCall): Maze3 {
        val simStep = call.request.queryParameters["iterations"]?.toIntOrNull() ?: 0
        val width =   call.request.queryParameters["width"]?.toIntOrNull() ?: 40
        val height =  call.request.queryParameters["height"]?.toIntOrNull() ?: 40
        var world = randomMaze(width, height)
        repeat(simStep) {
          world = world.nextGeneration(EvolutionCell::conwayLaws)
        }
        return world
      }

      get("/ascii") {
        call.respondText {
          buildWorld(call).renderToString()
        }
      }

      get("/img") {
        val world = buildWorld(call)

        val image = world.toImage(800, 800)

        val imageBytes = ByteArrayOutputStream().use {
          ImageIO.write(image, "png", it)
          it.toByteArray()
        }

        call.respondBytes(contentType = ContentType.Image.PNG, bytes = imageBytes)
      }

      get("/gif") {
        call.respondOutputStream(contentType = ContentType.Image.GIF) {
          val steps = call.request.queryParameters["steps"]?.toInt() ?: 3

          MemoryCacheImageOutputStream(this).use { os ->
            createGIF(os, delay = 200, loop = false, images = sequence {
              var world = buildWorld(call)
              var prevImage = world.toImage(800, 800)

              repeat(steps) {
                world = world.nextGeneration(EvolutionCell::conwayLaws)
                prevImage = world.renderToImage(prevImage.addAging())
                yield(prevImage)
              }
            })
          }
        }
      }
    }
  }.start(wait = true)
}
