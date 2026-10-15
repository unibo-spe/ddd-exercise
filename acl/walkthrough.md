# Exercise 3 -- Walkthrough

A suggested order for adding CSV import/export without letting third-party libraries corrupt the domain.
A possible solution is on branch `master`.

## Step 0 -- Get to know the domain

Read `Row` and `Table` (plus their implementations), and notice that they mention JDK types only.
This must still hold at the end of the exercise.

## Step 1 -- Design the port

Define a `CsvService` interface _in terms of the domain_: it writes a `Table` to a file, and reads a `Table` from a file.
Before writing it, decide:

- which settings make sense _whatever the library_ (e.g. separator and quote characters) and which ones do not (a setting supported by one library only cannot be part of the interface)
- which exceptions may be thrown: `java.io.IOException` is fine, library-specific exceptions are not
- how clients get an instance _without naming the implementation classes_, e.g. static factory methods returning `CsvService`

> __Checkpoint__: `CsvService` compiles even with no CSV library on the classpath.

## Step 2 -- Write the tests once, for any implementation

Write a JUnit 5 _parameterized_ test, whose implementations come from a `@MethodSource`:

```java
static Stream<CsvService> services() {
    return Stream.of(CsvService.apacheCommons(',', '"')); // the other one will be added later
}

@ParameterizedTest
@MethodSource("services")
void readsWhatItWrites(CsvService service) { /* ... */ }
```

Cover at least:

- writing a table and reading it back gives an equal table
- reading a _hand-written_ file (e.g. via `Files.writeString`, into a `@TempDir`)
- tricky values: separators inside values (`"likes commas, a lot"`), quotes inside values (`says "hello"`)

## Step 3 -- First implementation: Apache Commons CSV

Implement the interface with [Apache Commons CSV](https://www.baeldung.com/apache-commons-csv) (`CSVFormat`, `CSVPrinter`, `CSVParser`).
Convert library records into `Row`s right away: library types should only appear inside this class.

> __Checkpoint__: all tests pass for this implementation.

## Step 4 -- Second implementation: OpenCSV

Implement the interface again with [OpenCSV](https://www.baeldung.com/opencsv), and add it to `services()`.
Now the same tests run twice.
If some test fails for OpenCSV only, the two libraries interpret CSV differently: find out which settings make them agree (hint: look for an RFC 4180 compliant parser in OpenCSV).
This is exactly the kind of difference an anti-corruption layer is meant to hide.

## Step 5 -- Check for leaks

- in `acl/build.gradle.kts`, the libraries are `implementation` dependencies: what would change with `api`?
- no public method of the module mentions library types, in signatures or exceptions
- add one more test: a file written by one implementation is read by the other one

> __Checkpoint__: replacing one library with the other requires changing one line in the clients' code, i.e. the call to the factory method.
