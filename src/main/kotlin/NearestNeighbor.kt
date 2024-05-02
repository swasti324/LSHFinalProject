package org.example
import kotlin.random.Random
class NearestNeighbor(strings: List<String>) {

    init {
        val shingles = strings.map {shingle(it)}
        val vocabSet: LinkedHashSet<String> = LinkedHashSet()
        shingles.forEach { set -> set.forEach { vocabSet.add(it) } }
        val vocab = vocabSet.shuffled()

        val sparseVectors = shingles.map { sparseVector(it, vocab) }
        // val denseVectors = sparseVectors.map { denseVector(it) }
    }

    fun shingle(string: String, k: Int = 2): Set<String> {
        val shingles = mutableSetOf<String>()
        for (i in 0..string.length-k) {
            shingles.add(string.substring(i, i+k))
        }
        return shingles
    }

    fun sparseVector(shingles: Set<String>, vocab: List<String>): List<Boolean> {
        return vocab.map { it in shingles }
    }

    fun minHash(vocab: List<String>){
        val hash = (1..vocab.size).toList().shuffled(Random.Default)

        for (i in 1..vocab.size) {
            val id = hash.indexOf(i)
            val signature = vocab[id]
            println("$i -> $id -> $signature")
            if (signature == "signature") {
                println("match!")
                break
            }
        }
    }

//    fun denseVector(sparseVector: List<Boolean>, hashes: List<List<Int>>): List<Int> {
//
//    }

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

        println(bands)

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

//    fun findClosest(string: String) {
//        TODO()
//    }

    fun jaccard(a: List<Boolean>, b: List<Boolean>): Double {
        val aANDb = (a.zip(b).map { if (it.first && it.second) 1 else 0 }).sum()
        val aORb = (a.zip(b).map { if (it.first || it.second) 1 else 0 }).sum()
        return aANDb.toDouble() / aORb.toDouble()
    }
}