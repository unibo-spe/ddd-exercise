# Exercise 2 -- Walkthrough

A suggested order for moving the `Counter` towards event sourcing and CQRS.
A possible solution is on branch `master`.

## Step 0 -- Understand the starting point

Read `Counter`, `Variation`, and `CounterImpl`.
`CounterImpl` stores a _snapshot_ (the current value) and publishes a `Variation` whenever the value changes.
Then, the variation is _lost_: questions like "what was the value yesterday at noon?" have no answer.

Then read the two interfaces to be implemented, in package `it.unibo.ddd.cqrs`:

- `CounterWriter`: the __write model__, accepting _commands_ (`setValue`) and storing their outcome as _events_ (`Variation`s)
- `CounterReader`: the __read model__, answering _queries_ about the current or past value

## Step 1 -- Test the write model

Write a test for `CounterWriterImpl` _before_ implementing it.
Timestamps matter here, so the writer should receive a `java.time.Clock` via its constructor.
In tests, use a clock that moves 1 minute ahead whenever it is read: extend `java.time.Clock`, overriding `instant()`, `getZone()`, and `withZone()`.

Check that `setValue(5)` and then `setValue(3)` produce exactly `Variation.of(0, 5, t0)` and `Variation.of(5, 3, t0 + 1 min)`.

## Step 2 -- Implement the write model

- the __event store__ is just a list of variations, only ever _appended_ to
- the current value is not stored: it is the `after()` of the last variation (or 0)
- each new variation is also published via an RxJava `Subject` (cf. `CounterImpl`)

> __Checkpoint__: the writer has no field holding the current value.

## Step 3 -- Test the read model

Write tests for `CounterReaderImpl`, with variations at `t0`, `t0 + 1 min`, `t0 + 2 min`, querying the value:

- before `t0` (expected: 0)
- exactly at `t0`
- between two variations
- long after the last one

Also test a reader created _after_ some writes: it must know the past too.

## Step 4 -- Implement the read model

The reader keeps its own representation of the data (a __projection__), optimised for queries, and kept up to date by the events of the writer:

1. in the constructor, _replay_ the past variations (`writer.getVariations()`)...
2. ... then _subscribe_ to the new ones (`writer.onVariation()`)

Hint: in a `TreeMap<Instant, Long>` from timestamps to values, `floorEntry(at)` is the latest change before or at `at`.

> __Checkpoint__: the reader never iterates over the whole history to answer a query.

## Step 5 (optional) -- Think about consistency

Here the read model is updated _synchronously_, while the writer publishes.
Try `writer.onVariation().observeOn(Schedulers.single())`: what may a query return right after a `setValue`?
This is _eventual consistency_ (cf. the slides on when the read model is updated).
