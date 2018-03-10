package com.baulsupp.oksocial.output.formats

import com.kitfox.svg.SVGCache
import com.kitfox.svg.app.beans.SVGIcon
import okio.Buffer
import okio.BufferedSource
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object SvgHandler {
  fun convertSvgToPng(source: BufferedSource): BufferedSource {
    val universe = SVGCache.getSVGUniverse().also { it.isVerbose = false }

    val uri = source.inputStream().use { universe.loadSVG(it, "preview") }

    val icon = SVGIcon()
    icon.antiAlias = true
    icon.svgURI = uri

    val sink = Buffer()

    val image = BufferedImage(icon.iconWidth, icon.iconHeight, BufferedImage.TYPE_INT_ARGB)
    val g = image.createGraphics()
    g.setClip(0, 0, icon.iconWidth, icon.iconHeight)
    icon.paintIcon(null, g, 0, 0)
    g.dispose()

    ImageIO.write(image, "png", sink.outputStream())

    universe.clear()

    return sink
  }
}