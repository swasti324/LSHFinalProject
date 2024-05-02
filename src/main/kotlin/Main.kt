package org.example
import kotlin.random.Random
import java.io.File

fun parse():List<String> {
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
    parse()

    val nn = NearestNeighbor(listOf(""))

    val vocab = listOf("He", "el", "Wo", "or", "rl", "ld", "ll", "lo")
    val sparseVectors = List(nn.sparseVector(setOf("He", "el", "ll", "lo"), vocab))
            val hash = (0..vocab.size).toList().shuffled(Random.Default)

        val signatures = nn.minHash(sparseVectors, vocab, hash)
    print(signatures)
}