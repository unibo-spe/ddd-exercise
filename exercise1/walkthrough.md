# Exercise 1 -- Walkthrough

A suggested order for modelling the Simple Store.
Each step ends with a _checkpoint_: do not move on until it holds.
A possible solution is on branch `master`. Peek at it only after trying.

## Step 0 -- Get to know the worked example

The `customers` package (in `exercise1/domain`) is already modelled, and it shows the conventions to follow:

- __interfaces__ in `it.unibo.ddd.exercises.customers`, e.g. `Customer`, `CustomerID`, `CustomerRegistry`
- __implementations__ in the `impl` sub-package
    * value objects and entities are package-private: nobody can bypass the factory
    * only the factory (`CustomerFactoryImpl`) and the repository (`InMemoryCustomerRegistry`) are public
- __tests__ in `exercise1/domain/src/test`

Read them in this order: `CustomerID`, `TaxCode`, `Customer`, `CustomerFactory`, `CustomerRegistry`, then their implementations, then `CustomersTest`.
Notice that:

- value objects are `record`s, so `equals`/`hashCode` compare _all_ fields
- `CustomerImpl.equals` compares _identifiers only_
- invariants (e.g. well-formed emails) are checked by the entity itself, so they hold whatever the way the entity is modified

> __Checkpoint__: `./gradlew :domain:test` passes, and you can explain why two `Customer`s with different names may be equal.

## Step 1 -- Build the ubiquitous language

Read the domain description in the [README](../README.md#domain-description), and write a glossary: one row per term, with its meaning and its building block.
Nouns are candidate _types_, verbs are candidate _operations_ (or _events_, when they are in the past tense: "an order _has been registered_").

| Term          | Meaning                                         | Building block |
|---------------|-------------------------------------------------|----------------|
| Customer      | a person or a company buying products           | entity         |
| Tax code      | identifier of a person                          | value object   |
| Customer registry | where customers are stored and looked up    | repository     |
| ...           | ...                                             | ...            |

To choose the building block, ask:

1. Does the thing have an _identity_ that stays the same while its attributes change? Then it is an __entity__.
2. Is it fully defined by its attributes, so that two copies with the same attributes are interchangeable? Then it is a __value object__, and it should be immutable.
3. Do some objects need to change _together_ to stay consistent? Then they form an __aggregate__: pick the __root__, the only entry point.
4. Does an entity need to be stored and retrieved? It needs a __repository__.
5. Is its creation non-trivial (validation, generated IDs, derived values)? It needs a __factory__.
6. Is there an operation that does not naturally belong to any entity, or that involves many of them? It is a __service__.
7. Should something happen _in reaction_ to something else? That is a __domain event__.

> __Checkpoint__: every noun of the description is in the glossary, or you decided on purpose to leave it out.

## Step 2 -- Money: value objects first

Start from the money context, since it depends on nothing else, and contains value objects only:
`Currency`, `Money`, `ExchangeRate`, plus a factory creating them.

Write the tests _first_ (e.g. `MoneyTest`), covering:

- equality: `10.5 EUR` equals `10.50 EUR`, but not `10.50 USD`
- immutability: `ten.plus(two)` returns a _new_ `Money`, and `ten` is unchanged
- invariants: summing different currencies fails, exchange rates are positive
- conversion: an `ExchangeRate` from EUR to USD converts EUR amounts, and rejects USD ones

Then implement them. Pitfalls:

- never use `double` for money: use `BigDecimal`
- `BigDecimal.equals` considers the scale (`10.5` ≠ `10.50`): normalise it in the constructor
- prefer `java.time.Instant` to `java.util.Date`

> __Checkpoint__: money tests pass, and no money type has setters.

## Step 3 -- Products: entity, factory, repository

Model `ProductID` (value object: name + type), `Product` (entity: price and availability may change), `ProductFactory`, and `Warehouse` (repository).
Follow the very same structure as `customers`.

Tests to write:

- two products with the same ID are equal, even with different prices
- negative prices or quantities are rejected, both at creation and when set
- the warehouse finds products by ID (returning `Optional`), refuses duplicates, and forgets removed products

> __Checkpoint__: you can store a product in an in-memory `Warehouse`, change its price, and find it again by ID.

## Step 4 -- Orders: the aggregate

`Order` is the most interesting type, as it is the __root__ of an aggregate also containing the ordered amounts.
Design decisions to take:

- an order refers to its customer and products __by ID__ (`CustomerID`, `ProductID`), not by object: they are roots of _other_ aggregates
- items are only changed via the root (`addProduct`, `removeProduct`): `getAmounts()` returns an _unmodifiable_ view
- the root enforces the rules of the description, e.g. amounts are positive, and nothing may change once the order is delivered
- the `OrderFactory` assigns fresh serial numbers and the current time
    * do not call `Instant.now()` inside it: _inject_ a `java.time.Clock` via the constructor, so that tests can use `Clock.fixed(...)`

Tests to write: fresh IDs and timestamps from the factory, amounts summing up when the same product is added twice, attempts to modify a delivered order failing.

> __Checkpoint__: no domain type calls `now()`, and no `Order` method returns a mutable collection.

## Step 5 -- Application layer: services and events

Some requirements involve the outside world, or many aggregates at once. They go into the `application` module:

- __exchange rates__ come from the Internet: model them as a `CurrencyExchangeService` _interface_ (a port), whose implementations are adapters
- __prices in any currency, at any moment__: a `PricingService` looking prices up in the `Warehouse` and converting them via the `CurrencyExchangeService`
- __"when a new order is registered, many actions should be performed in reaction"__: an `OrderManagementService` that
    1. validates the order (known customer? known products? not empty?)
    2. stores it in the `OrderStore`
    3. publishes an `OrderEvent` (e.g. a `record`), via an RxJava `Observable`, so that any number of other components can subscribe

Rule of thumb for choosing the module: `domain` must compile without knowing anything about the Internet, databases, or frameworks.

> __Checkpoint__: `exercise1/domain/build.gradle.kts` still has no dependencies.

## Step 6 -- Wiring and integration tests

All services receive their dependencies (repositories, other services, the clock) via __constructor injection__: no `new` of dependencies inside them.

- __unit test__ the `PricingService` with a _stub_ exchange service: `CurrencyExchangeService` has one method only, so a lambda will do
- __integration test__ the `OrderManagementService` with _real_ in-memory repositories: the test itself is the composition root
    * subscribe to the events _before_ performing orders, and collect them into a list
    * check that rejected orders produce no events

> __Checkpoint__: `./gradlew :domain:test :application:test` passes, and every test asserts _exact_ values.

## Step 7 (optional) -- A real exchange-rate service

Write an adapter for the free [Frankfurter API](https://frankfurter.dev), e.g. `GET https://api.frankfurter.dev/v1/2024-01-05?base=EUR&symbols=USD`.

- put it into a new `exercise1/infrastructure` module, depending on `application`
- turn the JSON response (a DTO) into an `ExchangeRate` inside the adapter: JSON must not leak into the domain
- inject the `HttpClient` and the endpoint URI, so that tests can run against a fake server (`com.sun.net.httpserver.HttpServer`) on localhost, with no Internet
