import org.example.NearestNeighbor
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

import kotlin.random.Random
class NearestNeighborTest {

    /**
     * Test the shingling function
     */
    @Test
    fun shingle() {
        // Create an empty nearest neighbor object
        val nn = NearestNeighbor(listOf(""))

        // Test shingling
        assertEquals(setOf("He", "el", "ll", "lo"), nn.shingle("Hello"))
        assertEquals(setOf("Wo", "or", "rl", "ld"), nn.shingle("World"))
    }

    /**
     * Test the 1-hot encoding for the sparse vectors
     */
    @Test
    fun sparseVector() {
        // Create an empty nearest neighbor object
        val nn = NearestNeighbor(listOf(""))

        // Create a test vocab to use
        val vocab = listOf("He", "el", "Wo", "or", "rl", "ld", "ll", "lo")

        // Make sure that the 1-hot encoding matches
        assertEquals("11000011".map { it == '1' }, nn.sparseVector(setOf("He", "el", "ll", "lo"), vocab))
        assertEquals("00111100".map { it == '1' }, nn.sparseVector(setOf("Wo", "or", "rl", "ld"), vocab))
    }

    /**
     * Test converting sparse vectors to dense vectors
     */
    @Test
    fun denseVectors() {
        // Create an empty nearest neighbor object
        val nn = NearestNeighbor(listOf(""))

        // Create two sparse vectors to test
        val sparseVectors = listOf(
            "11000001".map { it == '1' },
            "00011100".map { it == '1' },
        )

        // Create hashes to test with
        val hashes = listOf(
            listOf(2, 4, 6, 1, 0, 7, 3, 5),
            listOf(6, 0, 7, 1, 2, 5, 4, 3),
        )

        // Calculate the expected dense vectors by hand
        val denseVectors = listOf(
            listOf(3, 1),
            listOf(1, 5),
        )

        // Make sure the expected dense vectors match the calculated ones
        assertEquals(denseVectors, nn.denseVectors(sparseVectors, hashes))
    }

    /**
     * Test getting the candidates from a list of dense vectors
     */
    @Test
    fun candidates() {
        // Create an empty nearest neighbor object
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

    /**
     * Test jaccard distance
     */
    @Test
    fun jaccard() {
        // Create an empty nearest neighbor object
        val nn = NearestNeighbor(listOf(""))

        // Test an arbitrary case
        assertEquals(1.0/3.0, nn.jaccard(
            listOf(true, true, false),
            listOf(false, true, true)))

        // All matches should have a distance of 1.0
        assertEquals(1.0, nn.jaccard(
            listOf(true, true, false),
            listOf(true, true, false)))

        // No matches should have a distance of 0.0
        assertEquals(0.0, nn.jaccard(
            listOf(false, true),
            listOf(true, false)))

        // Half matches (and all true in one or the other) should have a distance of 0.5
        assertEquals(0.5, nn.jaccard(
            listOf(true, false),
            listOf(true, true)))

        // Make sure that all false doesn't throw an error
        assertEquals(0.0, nn.jaccard(
            listOf(false, false),
            listOf(false, false)))
    }
}