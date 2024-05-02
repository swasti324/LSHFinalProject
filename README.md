Steps:
- parse file (filepath) -> list<string>
- k-Shingling (list<string>, k) -> list<set<string>>
- Create voca
- 
- bulary (list<set<string>>) - list<string>
- Create sparse vectors (vocab: list<string>, lines: list<string>)
- Minhashing (sparse_vectors, vocab) -> dense_vectors
- Banding (dense_vectors)

```Kotlin
fun main() {
    val nn = NearestNeighbor(listOf(
        "The young boys are playing outdoors and the man is smiling nearby",
        "The kids are playing outdoors near a man with a smile",
        "A person on a black motorbike is doing tricks with a jacket",
        "A man in a black jacket is doing tricks on a motorbike",
    ))
}
```
# Locality Sensitive Hashing
#### by Dominic Salmieri and Swasti Jain

## Overview

## Quotes

## Shingling and Vocabulary
![Locality Sensitive Hashing.png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing.png)

![Locality Sensitive Hashing (1).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%281%29.png)


## One-Hot Encoding: Sparse Vectors
![Locality Sensitive Hashing (2).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%282%29.png)


## Minhashing and Signatures
![Locality Sensitive Hashing (3).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%283%29.png)

![Locality Sensitive Hashing (4).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%284%29.png)

![Locality Sensitive Hashing (5).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%285%29.png)


## Dense Vector and Banding

![Locality Sensitive Hashing (6).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%286%29.png)

![Locality Sensitive Hashing (7).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%287%29.png)

![Locality Sensitive Hashing (8).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%288%29.png)

![Locality Sensitive Hashing (9).png](LSH%20Presentation%20Images%2FLocality%20Sensitive%20Hashing%20%289%29.png)


## Testing + Tuning LSH




