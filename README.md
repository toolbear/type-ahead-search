# Type Ahead Search Pr…

## Quick Start

```shell
$ gradle clean check shadowJar
$ java -jar build/libs/type-ahead-search-0.0.1-all.jar
```

or

```shell
$ gradle clean check shadowJar
$ ./bin/tas
```

## TODO

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
  * [ ] better title tokenization; ignore hyphens, split on other punctuation

* [ ] profile memory footprint

* [ ] Test cases
  * [ ] > 10 matches
  * [ ] "Moon" and "Moonraker"
  * [ ] lexigraphic order
  * [ ] "The Thing (1982)" and "The Thing (2011)"
  * [ ] case insensitivity when querying

## Profiling

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
