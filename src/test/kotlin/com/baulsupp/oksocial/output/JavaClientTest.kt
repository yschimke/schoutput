package com.baulsupp.oksocial.output

import com.baulsupp.oksocial.output.handler.ConsoleHandler.Companion.previewFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException

class JavaClientTest {
    @Test
    fun testPreviewFileFail() {
        Assertions.assertThrows(FileNotFoundException::class.java
        ) { previewFile(File("sdfjkhfd.txt")) }
    }

    @Test
    fun testPreviewFileWorks() {
        previewFile(File("src/test/resources/PNG_transparency_demonstration_1.png"))
    }
}
