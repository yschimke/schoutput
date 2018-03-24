package com.baulsupp.oksocial.output.util

import java.io.IOException
import java.util.Properties

object PlatformUtil {
  fun versionString(mainClass: Class<*>, propertiesFile: String): String {
    return try {
      val prop = Properties()
      val `in` = mainClass.getResourceAsStream(propertiesFile)
      prop.load(`in`)
      `in`.close()
      prop.getProperty("version")
    } catch (e: IOException) {
      throw AssertionError("Could not load $propertiesFile")
    }
  }

  val isOSX by lazy { System.getProperty("os.name").contains("OS X") }

  val isLinux by lazy { System.getProperty("os.name").contains("Linux") }
}
