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
}