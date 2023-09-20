## Exercise -- Modelling the Simple Store

A simple domain keeping track of: _customers_, _products_, and _orders_.

## TO-DO

1. Read informal domain description
2. Identify the main domain concepts composing the ubiquitous language
3. Model the domain as Java types (classes or interfaces)
    - the model should include entities, value objects, repositories, factories, and services
4. Structure the Java types according to some module structure compliant with hexagonal architecture
5. Sketch tests and, then, implementation for at least one
    - entity
    - value object 
    - factory
    - repository
    - value object

## Domain description

### Customers

- Customers can either be companies or persons

- Companies are identified by a VAT number

- Persons are identified by tax codes

- In any case a name and email are recorded for each customer

- Both the name and email may vary over time

### Products

- Products are identified by name and type/category

- They have a nominal price, expressed using some currency

- The availability of each product is tracked as well

- Both the price and the available quantity may vary over time

### Money and exchange rates

- Money is represented by a decimal value and a currency

- Currencies are represented by a name, a symbol and an acronym
    - e.g. Euro, EUR, €
    - e.g. USA Dollar, USD, $

- Exchange rates keep track of the conversion rate
    + from a source currency
    + to a destination currency
    + in a particular moment

- Information about exchange rates can be attained from the Internet

- We can compute the price of each product, in any currency, at any moment

### Orders

- Orders are identified by a serial number

- They keep track of the many products ordered by a customer
    * and the amount of copies ordered for each product

- Also, orders keep track of when they have been performed

- All such information may be modified before the order is delivered

- When a new order is registered, many actions should be performed in reaction

- It must be possible to compute the actual total price of an order
    * in a particular moment, using a particular currency