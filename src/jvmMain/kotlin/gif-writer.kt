package org.jonnyzzz.lifegame

import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageTypeSpecifier
import javax.imageio.metadata.IIOMetadataNode
import javax.imageio.stream.ImageOutputStream

private fun getNode(rootNode: IIOMetadataNode, nodeName: String): IIOMetadataNode {
  val nNodes = rootNode.length
  for (i in 0 until nNodes) {
    if (rootNode.item(i).nodeName.equals(nodeName, ignoreCase = true)) {
      return rootNode.item(i) as IIOMetadataNode
    }
  }
  val node = IIOMetadataNode(nodeName)
  rootNode.appendChild(node)
  return node
}

//inspired by https://memorynotfound.com/generate-gif-image-java-delay-infinite-loop-example/
fun createGIF(
  out: ImageOutputStream,
  images: Sequence<RenderedImage>,
  imageType: Int = BufferedImage.TYPE_INT_ARGB,
  delay: Int = 200,
  loop: Boolean = false
) {
  val writer = ImageIO.getImageWritersBySuffix("gif").next()
  val params = writer.defaultWriteParam

  val imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType)
  val metadata = writer.getDefaultImageMetadata(imageTypeSpecifier, params)

  val metaFormatName = metadata.nativeMetadataFormatName
  val root = metadata.getAsTree(metaFormatName) as IIOMetadataNode

  val graphicsControlExtensionNode = getNode(root, "GraphicControlExtension")
  graphicsControlExtensionNode.setAttribute("disposalMethod", "none")
  graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE")
  graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE")
  graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay / 10))
  graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0")

  val commentsNode = getNode(root, "CommentExtensions")
  commentsNode.setAttribute("CommentExtension", "Created by: https://jonnyzzz.com")

  val appExtensionsNode = getNode(root, "ApplicationExtensions")
  val child = IIOMetadataNode("ApplicationExtension")
  child.setAttribute("applicationID", "NETSCAPE")
  child.setAttribute("authenticationCode", "2.0")

  val loopContinuously = if (loop) 0 else 1

  child.userObject = byteArrayOf(
    0x1,
    (loopContinuously and 0xFF).toByte(),
    (loopContinuously shr 8 and 0xFF).toByte()
  )

  appExtensionsNode.appendChild(child)
  metadata.setFromTree(metaFormatName, root)

  writer.output = out
  writer.prepareWriteSequence(null)


  images.forEach {
    writer.writeToSequence(IIOImage(it, null, metadata), params)
  }

  writer.endWriteSequence()
}
