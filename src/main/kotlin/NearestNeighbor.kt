package org.example

class NearestNeighbor(strings: List<String>) {
//    val nBands: Int = 10

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

//    fun denseVector(sparseVector: List<Boolean>, hashes: List<List<Int>>): List<Int> {
//        TODO()
//    }

//    fun bin(dense: List<Boolean>) {
//        TODO()
//    }

//    fun findCandidates(string: String) {
//        TODO()
//    }

//    fun findClosest(string: String) {
//        TODO()
//    }
}