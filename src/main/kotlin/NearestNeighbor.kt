package org.example
import jdk.incubator.vector.Vector
import kotlin.math.sign
import kotlin.random.Random
class NearestNeighbor(
    strings: List<String>,
    denseVectorLength: Int = 100,
    numberOfBands: Int = 20)
{
    /**
     * Find and print candidate pairs
     */
    init {
        // Create a set of shingles for each string
        val shingles = strings.map {shingle(it)}

        // Create the vocab and add all the shingles from all the strings to it
        val vocabSet: LinkedHashSet<String> = LinkedHashSet()
        shingles.forEach { set -> set.forEach { vocabSet.add(it) } }

        // Shuffle the vocab
        val vocab = vocabSet.shuffled()

        // Create a 1-hot sparse vector for each string
        val sparseVectors = shingles.map { sparseVector(it, vocab) }

        // Create a dense vector for each string, with a different hash for each element in the signature
        val hashes = List(denseVectorLength) { (List(vocab.size) { it }).shuffled() }
        val denseVectors = denseVectors(sparseVectors, hashes)

        // Find the candidate pairs by banding the dense vectors and looking for matches
        val candidates = candidates(denseVectors, numberOfBands)

        // Print the candidate pairs
        for (pair in candidates) {
            println("(${pair.first+1}, ${pair.second+1}) | Jaccard similarity: ${jaccard(sparseVectors[pair.first], sparseVectors[pair.second])}")
            println("  ${strings[pair.first]}")
            println("  ${strings[pair.second]}")
        }
    }

    /**
     * Create shingles of length [k] from a given [string] with no repeats
     * @param string the string to shingle
     * @param k the length of the shingles
     * @return the set of shingles
     */
    fun shingle(string: String, k: Int = 2): Set<String> {
        // Create a set to store the shingles
        val shingles = mutableSetOf<String>()

        // Loop through the string, moving the shingle window along and adding the shingle to the set
        for (i in 0..string.length-k) {
            shingles.add(string.substring(i, i+k))
        }

        return shingles
    }

    /**
     * Create a 1-hot encoded vector that stores what shingles from the vocab are present in the given list of shingles
     * @param shingles the shingles to look for in the vocab
     * @param vocab the vocabulary of all the given strings
     * @return the list of boolean values, which tell if the matching element in the vocabulary is in the given shingles
     */
    fun sparseVector(shingles: Set<String>, vocab: List<String>): List<Boolean> {
        return vocab.map { it in shingles }
    }

    /**
     * Create a list of dense vectors from a list of sparse vectors using MinHashing. This function works slightly
     * differently than in the instructions and explanation. Instead of looping through the hash in the order of the
     * elements and using the element number, it loops through the hash in order, and uses the values in the hash as
     * the indices to check. This is equivalent in that it randomizes the order.
     * @param sparseVectors the list of sparse vectors to MinHash
     * @param hashes the list of hashes to use. The size of this list determines the length of the dense vectors. Each
     * hash is the same length as the vocab and has the integers from zero to the length of the vocab minus one. These
     * numbers are shuffled differently for each hash, but are importantly the same across sparse vector encodings.
     * @return the list of dense vectors
     */
    fun denseVectors(sparseVectors: List<List<Boolean>>, hashes: List<List<Int>>): List<List<Int>> {
        // Create list to store the signatures
        val signatures = mutableListOf<List<Int>>()

        // For each spase vector
        for (sparseVector in sparseVectors) {
            // Create list to store the signature
            val signature = mutableListOf<Int>()

            // For each hash
            for (hash in hashes) {
                // Find the first value in the hash that is true in the sparse vector, then add its index to the
                // signature. Once this is done, continue to the next hash.
                for (i in hash.indices) {
                    if (sparseVector[hash[i]]) {
                        signature.add(i)
                        break
                    }
                }
            }

            // Add the signature to the list of signatures
            signatures.add(signature)
        }

        return signatures
    }


    /**
     * Takes a list of [denseVectors] and bands them into [nBands] bands. Then finds candidate pairs
     * @param denseVectors a list of dense vectors to use. Each dense vector must be the same length, which must be
     * evenly divisible by the number of bands
     * @param nBands the number of bands to divide the dense vectors into
     * @return a list a band maps. The keys of each band map are the hashes from the bands. The values are lists of the
     * indices of the dense vectors that got put in those bins
     */
    fun candidates(denseVectors: List<List<Int>>, nBands: Int): Set<Pair<Int, Int>> {
        // Make sure the bands can all have an equal, integer number of elements
        val vectorLength = denseVectors[0].size
        if (vectorLength % nBands != 0) {
            throw IllegalArgumentException("The size of the dense vector must evenly divisible by the number of bands")
        }

        // Calculate the number of elements to have in each band
        val sizeRatio = vectorLength / nBands

        // Create a data structure to store the bands
        val bands = List(nBands) { mutableMapOf<Int, MutableList<Int>>() }

        // Loop through each dense vector by index
        for (denseI in denseVectors.indices) {

            // Get the dense vector at the index
            val dense = denseVectors[denseI]

            // Make sure all the dense vectors are the same size
            if (dense.size != vectorLength) {
                throw IllegalArgumentException("The dense vectors must all be the same size")
            }

            // For each band
            for (i in 0..<nBands) {
                // Get the band
                val band = dense.slice(i * sizeRatio..<(i + 1) * sizeRatio)

                // If the bucket in the band is empty...
                if (bands[i][band.hashCode()] == null) {
                    // Create a new list with the index of the dense vector as the first value
                    bands[i][band.hashCode()] = mutableListOf(denseI)
                } else {
                    // Otherwise add the index of the dense vector to the existing list
                    bands[i][band.hashCode()]!!.add(denseI)
                }
            }
        }

        // Now the bands are populated
        // Create a set to hold the candidate pairs
        val candidates = mutableSetOf<Pair<Int, Int>>()

        // Loop through the bands
        for (band in bands) {
            // And each list of matches in each band
            for (matches in band.values) {
                when (matches.size) {
                    // When the size is 0 or 1, there are no pairs
                    0, 1 -> continue
                    // When the size is 2, there is one pair
                    2 -> candidates.add(Pair(matches[0], matches[1]))
                    // Create all possible combinations with 3 or more elements
                    else -> {
                        for (a in 0..<matches.size-1) {
                            for (b in a+1..<matches.size) {
                                candidates.add(Pair(matches[a], matches[b]))
                            }
                        }
                    }
                }
            }
        }

        return candidates
    }

    /**
     * Find the jaccard distance between two lists of booleans. Jaccard distance is defined as the ratio between the
     * intersection of A and B and the union of A and B.
     * @param a first list of booleans to use
     * @param b second list of booleans to use
     * @return the jaccard distance between A and B, from 0.0 to 1.0
     */
    fun jaccard(a: List<Boolean>, b: List<Boolean>): Double {
        // Find the intersection of A and B
        val aANDb = (a.zip(b).map { if (it.first && it.second) 1 else 0 }).sum()

        // Find the union of A and B
        val aORb = (a.zip(b).map { if (it.first || it.second) 1 else 0 }).sum()

        // Return the ratio between the intersection of A and B and the union of A and B.
        return if (aORb == 0) {
            0.0
        } else {
            aANDb.toDouble() / aORb.toDouble()
        }
    }
}