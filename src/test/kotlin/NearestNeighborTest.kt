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
    fun candidates() {
        val nn = NearestNeighbor(listOf(""))

        // Create dense vectors with the following matches
        // 4, 2, 6, 5, -, -, 1, 4,
        // 4, 2, -, -, 1, 5, -, -,
        // -, -, -, -, 1, 5, 3, 2,
        // -, -, -, -, 1, 5, -, -,
        // -, -, 6, 5, -, -, 3, 2,
        // -, -, -, -, 1, 5, 1, 4,
        val denseVectors = mutableListOf(
            mutableListOf(4, 2, 6, 5, 7, 9, 1, 4,),
            mutableListOf(4, 2, 1, 2, 1, 5, 4, 3,),
            mutableListOf(3, 4, 6, 1, 1, 5, 3, 2,),
            mutableListOf(1, 5, 4, 5, 1, 5, 5, 1,),
            mutableListOf(6, 2, 6, 5, 2, 3, 3, 2,),
            mutableListOf(7, 2, 3, 4, 1, 5, 1, 4,),
        )

        // Those dense vectors should create these candidate pairs
        val pairs = setOf(
            Pair(0, 1),
            Pair(0, 4),
            Pair(0, 5),
            Pair(1, 2),
            Pair(1, 3),
            Pair(1, 5),
            Pair(2, 3),
            Pair(2, 4),
            Pair(2, 5),
            Pair(3, 5),
        )

        // Make sure the candidates are correct
        assertEquals(pairs, nn.candidates(denseVectors, 4))

        // Make sure that an indivisible number of bands throws an exception
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { nn.candidates(denseVectors, 3) }

        // Make sure that having dense vectors of different sizes throws an exception
        denseVectors[1].removeLast()
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { nn.candidates(denseVectors, 4) }
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