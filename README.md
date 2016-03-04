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

* [ ] Directive REPL
  * [x] quit
  * [x] process-file
  * [ ] query
  * [x] ignore empty input lines
  * [x] echo entire input, not just directive

* [ ] Querying
  * [ ] empty data set
  * [ ] all matches
  * [ ] limit matches to 10

* [ ] Processing files
  * [ ] trie insertion
  * [ ] associate nodes with movie titles
  * [x] concurrent updates
  * [ ] better title tokenization; ignore hyphens, split on other punctuation

* [ ] profile memory footprint

* [ ] Test cases
  * [ ] > 10 matches
  * [ ] "Moon" and "Moonraker"
  * [ ] lexigraphic order
  * [ ] "The Thing (1982)" and "The Thing (2011)"
  * [ ] case insensitivity when querying
