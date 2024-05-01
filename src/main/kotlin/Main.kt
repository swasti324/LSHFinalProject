package org.example

import java.io.File

fun parse() {
    val filePath = "./StringData.txt"
    val file = File(filePath)
    if (!file.exists()) {
        println("File $filePath does not exist.")
        return
    }
    val content = file.readText()
    val lines = content.lines()

}
fun main() {
    parse()
}