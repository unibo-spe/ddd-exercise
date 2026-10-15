# Exercise 4 -- Walkthrough

A suggested order for refactoring the `ParkingLot` towards dependency injection.
A possible solution is on branch `master`.

## Step 0 -- Feel the pain

1. Run `./gradlew :di:test --rerun-tasks`: why does it take so long?
2. Try to write a test checking that a 2-hour stay costs 3.00 EUR. How long would it take to run?
3. In `sameCarCannotEnterTwice`, the car never exits. Use the same plate in another test: does the result depend on the order in which tests are run?

Write down every problem: each one points to a _hidden dependency_.

## Step 1 -- Find the hidden dependencies

Go through `ParkingLot` line by line, looking for things it _reaches_ by itself, instead of receiving them:

| Where | Dependency | Why it hurts tests |
|-------|------------|--------------------|
| `Instant.now()` | the system clock | tests must _wait_ for time to pass |
| ... | ... | ... |

You should find 3 of them.
On the contrary, `fee(Duration)` is not a dependency: it is a _pure function_, always giving the same output for the same input.

## Step 2 -- Extract one dependency at a time

For each dependency: (1) find or define an interface describing _what_ the parking lot needs, (2) move the current code into an implementation of it, (3) let the parking lot use the interface.
Run the old tests after each step, to check that nothing broke.

1. __the clock__: no new interface is needed, `java.time.Clock` already is one (use `clock.instant()`)
2. __the display__: an interface with one method, e.g. `void show(String message)`, implemented by `System.out::println`
3. __the parked cars__: an interface for registering and removing entries, implemented by a class wrapping a _non-static_ map

## Step 3 -- Constructor injection and composition root

- `ParkingLot` receives all its dependencies via its constructor, and stores them in `final` fields
- `Main` creates the implementations and passes them to the parking lot: it is the _composition root_, the only place mentioning implementation classes

> __Checkpoint__: `ParkingLot` contains no `now()`, no `static` field holding state, no `System.out`.

## Step 4 -- Rewrite the tests

Replace each dependency with a _test double_:

- a _stub_ clock, telling the time you want: `mock(Clock.class)`, then `when(clock.instant()).thenReturn(t0, t0.plus(Duration.ofHours(2)))` returns `t0` at the first call, and 2 hours later at the second one
- a _mock_ display: `mock(Display.class)`, then `verify(display).show("Welcome, AB123CD")`
- a _fake_ registry: the in-memory implementation, a fresh one per test

Now test the boundaries of the fee rule: 15 minutes (free), 16 minutes (1.50), 60 minutes (1.50), 61 minutes (3.00).

> __Checkpoint__: tests run in milliseconds, in any order, and assert exact fees.

## Step 5 -- Reflect

Compare the old and the new `ParkingLot`: the business logic is the same, only the _wiring_ moved out.
What would you need to change to store entries in a database, or to show messages on an actual display?
