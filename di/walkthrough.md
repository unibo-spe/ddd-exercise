# Exercise 4 -- Walkthrough

A suggested order for refactoring the `InvoiceService` towards dependency injection.
A possible solution is on branch `master`.

## Step 0 -- Feel the pain

1. Run `./gradlew :di:test --rerun-tasks` twice, then look into your temporary directory (`java.io.tmpdir`): what is `invoices.csv`?
2. Uncomment the assertion on the invoice number: why does it fail from the second run on?
3. Disconnect from the Internet, and run the tests again.
4. Try to assert the exact total of an invoice in USD.

Write down every problem: each one points to a _hidden dependency_.

## Step 1 -- Find the hidden dependencies

Go through `InvoiceService` line by line, looking for things it _creates_ or _reaches_ by itself, instead of receiving them:

| Where | Dependency | Why it hurts tests |
|-------|------------|--------------------|
| `LocalDate.now()` | the system clock | results change every day |
| ... | ... | ... |

Look for: `new` of non-value objects, `getInstance()`, static methods with side effects, I/O (network, files, console).

## Step 2 -- Keep a safety net

Do not delete the existing tests yet: they are weak, but they tell you whether the refactoring broke something.
Run them after each of the next steps.

## Step 3 -- Extract one dependency at a time

For each dependency: (1) define an interface describing _what_ the service needs, (2) move the current code into an implementation of it, (3) let the service use the interface.
Start from the easy ones:

1. __the clock__: no new interface is needed, `java.time.Clock` already is one (use `LocalDate.now(clock)`)
2. __notifications__: an interface with one method, whose implementation prints on the console
3. __exchange rates__: an interface like `BigDecimal rate(String from, String to, LocalDate date)`, implemented by moving `fetchRate` into a Frankfurter-based class receiving `HttpClient` and endpoint URI via its constructor
4. __the archive__: turn the singleton into an interface, plus a file-based implementation receiving the file `Path` via its constructor (and an in-memory implementation, for tests)

## Step 4 -- Constructor injection and composition root

- `InvoiceService` receives all its dependencies via its constructor, and stores them in `final` fields
- write an `ApplicationFactory`, with one method per interface, deciding which implementation to use and how to configure it: it should be the _only_ class mentioning implementation classes
- `Main` asks the factory for an `InvoiceService`

> __Checkpoint__: `InvoiceService` contains no `new` (apart from the `Invoice`), no `now()`, no `getInstance()`, no `System.out`.

## Step 5 -- Rewrite the tests

__Unit tests__ replace every dependency with a _test double_, so they can assert exact values:

- a _stub_ for exchange rates (a lambda always returning `1.10`)
- a _fake_ archive (in memory)
- a _mock_ notifier: `mock(Notifier.class)`, then `verify(notifier).invoiceIssued(expected)` (Mockito is already among the test dependencies)
- a fixed clock: `Clock.fixed(Instant.parse("2026-01-15T10:00:00Z"), ZoneOffset.UTC)`

Now you can assert that the first invoice has number 1, total `23.65 USD` for a `21.50 EUR` cart, and due date `2026-02-14`.

__Integration tests__ use _real_ implementations, yet lightweight and isolated ones:

- a file archive on a `@TempDir` file: check that numbering goes on after creating a new service on the same file (i.e. a restart)
- the Frankfurter implementation against a fake server on localhost (`com.sun.net.httpserver.HttpServer`), checking the requested URL too

> __Checkpoint__: tests pass offline, at any time of any day, any number of times.

## Step 6 -- Reflect

Compare the old and the new `InvoiceService`: the business logic is the same, only the _wiring_ moved out.
What would you need to change to archive invoices in a database, or to send emails instead of printing?
