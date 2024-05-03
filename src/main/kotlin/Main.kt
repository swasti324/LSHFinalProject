package org.example
import kotlin.random.Random
import java.io.File

fun parse(filePath: String):List<String> {
    /*
    looks for text file and reads the data, storing it in a data structure

    Args: none

    Returns: list of strings
     */
    val file = File(filePath)
    if (!file.exists()) {
        println("File $filePath does not exist.")
    }
    val content = file.readText()
    val lines = content.lines()

    return lines
}

fun main() {
    // Print the results from finding the similarity of the quotes
    println("--- QUOTE PAIRS ---")
    val quotes = parse("./StringData.txt")
    NearestNeighbor(quotes)

    // Print the results from finding the similarity of the test data
    println()
    println("--- TEST DATA PAIRS ---")
    // Test data from https://github.com/brmson/dataset-sts/blob/master/data/sts/sick2014/SICK_train.txt
    val testData = parse("./testData.txt")
    NearestNeighbor(testData)
}