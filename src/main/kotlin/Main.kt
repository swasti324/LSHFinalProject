package org.example
import kotlin.random.Random
import java.io.File

fun parse():List<String> {
    /*
    looks for text file and reads the data, storing it in a data structure

    Args: none

    Returns: list of strings
     */
    val filePath = "./StringData.txt"
    val file = File(filePath)
    if (!file.exists()) {
        println("File $filePath does not exist.")
    }
    val content = file.readText()
    val lines = content.lines()

    return lines
}

fun main() {
    val nn = NearestNeighbor(listOf(
        "The young boys are playing outdoors and the man is smiling nearby",
        "The kids are playing outdoors near a man with a smile",
        "A person on a black motorbike is doing tricks with a jacket",
        "A man in a black jacket is doing tricks on a motorbike",
    ))
}