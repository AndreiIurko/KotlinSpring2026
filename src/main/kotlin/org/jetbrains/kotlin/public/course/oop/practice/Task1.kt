package org.jetbrains.kotlin.public.course.oop.practice

import java.nio.file.Path
import kotlin.io.path.readText

/*
Implement a file reader for two file types: markdown and plain text
Requirements:
 - When reading a file through the FileReader, I should clearly differentiate between file types (plain/markdown/failure).
 In case of success, path and content should be saved. In case of failure - the message why content couldn't be loaded
 - The first line of file content should contain the string "<Presentable name of class>: <file-path>"
 */


interface FileReader
class PlainTextReader: FileReader

fun main() {
    // example how to read text
    val text = PlainTextReader::class.java.getResource("/file.txt")?.path?.let { Path.of(it) }?.readText()
    println(text)
}