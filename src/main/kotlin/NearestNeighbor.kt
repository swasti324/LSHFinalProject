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

    fun minHash(sparseVectors: List<List<Boolean>>, vocab: List<String>, hash:List<Int>):List<List<Int>>{
//         vocab is given. list of index of vocab created
//        shuffle the vocab many times?
//        compare several shuffles with a sparse vector to generate a signature
//        once a signature is created return it

        //            for each element in shuffled hash
//                shuffled hash, id takes the value of current(4 at index j = 0 for example)
//                      check if sparsevector[i] ==1

//                            add the hash id to the signature vector

        val signatures = MutableList(sparseVectors.size) { mutableListOf<Int>() }

        for (i in vocab.indices) {
//            val hash = (0..vocab.size).toList().shuffled(Random.Default)
            for (j in hash.indices){
                val id = hash.indexOf(j)
                if(sparseVectors[i][hash.indexOf(j)]){
                    signatures[i].add(id)
                }
            }
        }
        return signatures
    }


    /**
     * Takes a list of [denseVectors] and bands them into [nBands] bands
     * @param denseVectors a list of dense vectors to use. Each dense vector must be the same length, which must be
     * evenly divisible by the number of bands
     * @param nBands the number of bands to divide the dense vectors into
     * @return a list a band maps. The keys of each band map are the hashes from the bands. The values are lists of the
     * indices of the dense vectors that got put in those bins
     */
    fun band(denseVectors: List<List<Int>>, nBands: Int): List<Map<Int, List<Int>>> {
        val vectorLength = denseVectors[0].size
        if (vectorLength % nBands != 0) {
            throw IllegalArgumentException("The size of the dense vector must evenly divisible by the number of bands")
        }

        val sizeRatio = vectorLength / nBands

        val bands = List(nBands) { mutableMapOf<Int, MutableList<Int>>() }

        for (denseI in denseVectors.indices) {

            val dense = denseVectors[denseI]

            if (dense.size != vectorLength) {
                throw IllegalArgumentException("The dense vectors must all be the same size")
            }

            for (i in 0..<nBands) {
                val band = dense.slice(i * sizeRatio..<(i + 1) * sizeRatio)
                println("$band | ${band.hashCode()}")

                if (bands[i][band.hashCode()] == null) {
                    bands[i][band.hashCode()] = mutableListOf(denseI)
                } else {
                    bands[i][band.hashCode()]?.add(denseI)
                }

            }
        }
        return bands
    }

    fun findCandidates(string: String) {
        TODO()
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