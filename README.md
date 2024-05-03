# Locality Sensitive Hashing
#### by Dominic Salmieri and Swasti Jain

## Overview
Most Hashing functions focus on categorizing information that is the same. 
In order to categorize information that is similar we need to see what sections of the data are the same.
The more the smaller chunks of data match, the higher the similarity.
In this project we implemented Locality Sensitive Hashing algorithm to see the similarity between hundreds of famous quotes.
The ```StringData.txt``` file holds a culmination of many quotes from this website https://blog.hubspot.com/sales/famous-quotes.
This project aims to explain and implement the Locality Sensitive Hashing Algorithm. 

## Quotes
We chose famous quotes as our data of choice.
In order to parse through the text file we first created a function that would identify each quote and store the string in a list to be manipulated later

```Kotlin
fun parse():List<String> {
    /*
    looks for text file and reads the data, storing it in a data structure
    
    Args: none
    
    Returns: list of strings
     */
    val filePath = "./StringData.txt"
    val file = File(filePath)
    if (!file.exists()) {
        println("File $filePath does not exist.")
    }
    val content = file.readText()
    val lines = content.lines()

    return lines
}
```
This parse function as defined in the ```Main.kt``` file looks for the defined text file and reads the content.
It returns a list of the quotes alone.

## Shingling and Vocabulary
In order to break down the data to check for similarity, we split the strings into substrings called 'Shingles'.
Shingles are 2-3 character substrings, here we chose 2.
![Locality Sensitive Hashing.png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing.png)
This image illustrates how a string might be broken down into shingles.

![Locality Sensitive Hashing (1).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%281%29.png)
This process is done for each on of the quotes (red, green, blue represent different quotes). 
Then a cumulative vocabulary is built. It is important to note that the vocabulary must be a set of distinct shingles. 
The crossed out vocabulary shows duplicate shingles. We want there to be no duplicates as in the future we will be categorizing how often these shingles show up 

```Kotlin
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
```

## One-Hot Encoding: Sparse Vectors
Once our vocabulary is built, we can see which shingles appear in which quote.
It would be helpful to condense the information of when each shingle appears in the vocabulary.
We implement 'One-Hot Encoding' to condense this information.
![Locality Sensitive Hashing (2).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%282%29.png)
We first shuffle our vocabulary, simply to randomize the locations. 
Then we loop through each item in the vocabulary to check if the quote contains any of the same shingles.
This is stored as a boolean value in a new vector called a 'Sparse Vector'.
This process is repeated for each quote until many sparse vectors are created.
```Kotlin
 /**
     * Create a 1-hot encoded vector that stores what shingles from the vocab are present in the given list of shingles
     * @param shingles the shingles to look for in the vocab
     * @param vocab the vocabulary of all the given strings
     * @return the list of boolean values, which tell if the matching element in the vocabulary is in the given shingles
     */
    fun sparseVector(shingles: Set<String>, vocab: List<String>): List<Boolean> {
        return vocab.map { it in shingles }
    }
```
## MinHashing and Dense Vectors
These sparse vectors aren't very helpful on their own. 
It would be very helpful to figure out which shingles appear more often for which quotes
This idea centers around constantly randomizing the vocabulary and checking the encoded sparse vector to see which shingle shows up which quotes in the correct spot.
The culmination of this data turns into a signature. The following details the process.


The first thing to do is to create a numerical ID for each shingle in the vocabulary for computational efficiency.
Then that is shuffled and compared to each quote's sparse vector. The first number that exists in the sparse vector is stored as part of the signature.
![Locality Sensitive Hashing (3).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%283%29.png)

This process is repeated. Note that the shuffle is kept consistent for each signature digit.
![Locality Sensitive Hashing (4).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%284%29.png)
Eventually signatures are created and these are known as 'Dense Vectors'. 
We have successfully consolidated information of random collisions. We will take this one step further.
![Locality Sensitive Hashing (5).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%285%29.png)
```Kotlin
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
   ```

## Banding and Hashing
Banding breaks up the already consolidated signatures into smaller sections to compare the similarity of each quote signature.

This shows how the signature is broken into 2 bit parts
![Locality Sensitive Hashing (6).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%286%29.png)

Each band is then rehashed until 'Candidate Pairs' are found.  
![Locality Sensitive Hashing (7).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%287%29.png)

![Locality Sensitive Hashing (8).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%288%29.png)
Essentially a candidate pair is when two signatures get hashed into the same bucket. This indicates a matched sub-part. 
The more candidate pairs are found for two signatures the more we can be sure they are similar.
![Locality Sensitive Hashing (9).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%289%29.png)

```Kotlin
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
```
## Testing + Tuning LSH

```Kotlin

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
```




