package org.jonnyzzz.lifegame


var world = randomMaze(200, 200)

fun iterateWorld() {
  world = world.nextGeneration(EvolutionCell::conwayLaws)
}



fun Maze3.renderAsImage() {

//
//  val area = width *height;
//  val componentsPerPixel = 4
//  val pixelData = ByteArray(area * componentsPerPixel) { 0.toByte() }
//
//  forEachAlive { x, y ->
//    val base = x + width * y
//    pixelData[base + 0] = 0
//    pixelData[base + 1] = 0
//    pixelData[base + 2] = 0
//    pixelData[base + 3] = 0
//  }
//
//
//  val BitsPerComponent = 8;
//  val BytesPerRow=((BitsPerComponent * width) / 8) * componentsPerPixel;
//  val colorSpace = CGColorSpaceCreateDeviceRGB();
//  val gtx = CGBitmapContextCreate(pixelData, width, height, BitsPerComponent, BytesPerRow, colorSpace, CGImageAlphaInfo.kCGImageAlphaPremultipliedLast
//  )
//  val myimage = CGBitmapContextCreateImage(gtx)
//
//  // Convert to UIImage
//  val image = UIImage.imageWithCGImage(myimage)
//
//  // Create a rect to display
//  val imageRect = CGRectMake(0, 0, image.size.width, image.size.height);
//
//  // Here is the Two Snnipets to draw image
//  val context = UIGraphicsGetCurrentContext()
//
//  // Transform image
//  CGContextTranslateCTM(context, 0, image.size.height);
//  CGContextScaleCTM(context, 1.0, -1.0);
//
//  // Finaly Draw your image
//  CGContextDrawImage(context, imageRect, image.CGImage);
//
//  // You can also use following to draw your image in 'drawRect' method
//  //    [[UIImage imageWithCGImage:myimage] drawInRect:CGRectMake(0, 0, 145, 15)];
//
//  CGContextRelease(gtx);
//  CGImageRelease(myimage);
}
