# Type Ahead Search Pr…

## Quick Start

```shell
$ gradle clean check shadowJar

$ ./bin/tas

# or

$ java -jar build/libs/type-ahead-search-0.2.0-all.jar

# or

$ java -cp build/libs/type-ahead-search-0.2.0-all.jar tas.CLI
```

## TODO

* [x] replace `ConcurrentRadixTree` with a bespoke data structure
  * [x] equivalent prefix tree
  * [x] concurrent writes

* [ ] use guice assisted inject instead of factories

* [x] Directive REPL
  * [x] quit
  * [x] process-file
  * [x] query
  * [x] ignore empty input lines
  * [x] echo entire input, not just directive

* [x] Querying
  * [x] empty data set
  * [x] all matches
  * [x] limit matches to 10

* [ ] Processing files
  * [x] trie insertion
  * [x] support multiple movies with the same key
  * [x] concurrent updates
  * [ ] better write performance; finer grained locks than one lock for the entire tree
  * [ ] better title tokenization
    * [ ] ignore hyphens
    * [x] split on other punctuation

## Profiling

`FatMovie` conservatively uses `String` for all fields. `ThinMovie` makes some assumptions about the input data — country codes are two characters and movie years are between [1887-2132] — to conserve some heap space. With around 100k movies loaded, `ThinMovie` uses approximately 10mB less. Something like a `FileOffsetMovie` could be used to further reduce the memory footprint trading it for CPU and disk IO.

```
$ bin/tas
# GC, heap size

query foo
# GC, heap size

process-file imdb-aa
...
# GC, heap size

query blu
query zoo
query m
# GC, heap size
```

### Results

* 114,840 movies from IMDB FTP
  * FatMovie
    * startup: 23 mB
    * query: 23 mB
    * load: 64 mB
    * query: 65 mB
  * ThinMovie
    * startup: 23 mB
    * query: 23 mB
    * load: 54 mB
    * query: 54 mB
