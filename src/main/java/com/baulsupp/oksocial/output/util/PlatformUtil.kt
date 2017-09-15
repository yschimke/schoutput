package com.baulsupp.oksocial.output.util

import java.io.IOException
import java.io.InputStream
import java.util.Properties

object PlatformUtil {

  fun versionString(mainClass: Class<*>, propertiesFile: String): String {
    try {
      val prop = Properties()
      val `in` = mainClass.getResourceAsStream(propertiesFile)
      prop.load(`in`)
      `in`.close()
      return prop.getProperty("version")
    } catch (e: IOException) {
      throw AssertionError("Could not load " + propertiesFile)
    }

  }

  val isOSX: Boolean
    get() {
      val osName = System.getProperty("os.name")
      return osName.contains("OS X")
    }

  val isLinux: Boolean
    get() {
      val osName = System.getProperty("os.name")
      return osName.contains("Linux")
    }
}
