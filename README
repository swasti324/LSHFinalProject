Steps:
- parse file (filepath) -> list<string>
- k-Shingling (list<string>, k) -> list<set<string>>
- Create vocabulary (list<set<string>>) - list<string>
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