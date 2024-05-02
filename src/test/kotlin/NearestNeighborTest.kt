import org.example.NearestNeighbor
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class NearestNeighborTest {

    @Test
    fun shingle() {
        val nn = NearestNeighbor(listOf(""))

        assertEquals(setOf("He", "el", "ll", "lo"), nn.shingle("Hello"))
        assertEquals(setOf("Wo", "or", "rl", "ld"), nn.shingle("World"))
    }

    @Test
    fun sparseVector() {
        val nn = NearestNeighbor(listOf(""))

        val vocab = listOf("He", "el", "Wo", "or", "rl", "ld", "ll", "lo")

        assertEquals("11000011".map { it == '1' }, nn.sparseVector(setOf("He", "el", "ll", "lo"), vocab))
        assertEquals("00111100".map { it == '1' }, nn.sparseVector(setOf("Wo", "or", "rl", "ld"), vocab))
    }

    @Test
    fun minHash() {
        val nn = NearestNeighbor(listOf(""))

        val vocab = listOf("He", "el", "Wo", "or", "rl", "ld", "ll", "lo")

        nn.minHash(vocab)
        assertEquals("hello", "hello")
    }

    @Test
    fun band() {
        val nn = NearestNeighbor(listOf(""))

        val denseVectors = listOf(
            listOf(4, 2, 6, 5, 7, 2),
            listOf(4, 2, 2, 3, 6, 5),
            listOf(5, 4, 5, 3, 6, 5)
        )

        println(nn.band(denseVectors, 3))
    }

    @Test
    fun jaccard() {
        val nn = NearestNeighbor(listOf(""))

        assertEquals(1.0/3.0, nn.jaccard(
            listOf(true, true, false),
            listOf(false, true, true)))

        assertEquals(1.0, nn.jaccard(
            listOf(true, true),
            listOf(true, true)))

        assertEquals(0.0, nn.jaccard(
            listOf(false, true),
            listOf(true, false)))

        assertEquals(0.5, nn.jaccard(
            listOf(true, false),
            listOf(true, true)))
    }
}