/**
 * Orders context: the orders performed by customers.
 * <p>
 * TODO: model the concepts of this context as interfaces (cf. step 4 of exercise1/walkthrough.md)
 * <ul>
 *     <li>which objects form the aggregate? which one is its root?</li>
 *     <li>how should an order refer to customers and products, which are roots of other aggregates?</li>
 *     <li>which rules must the root enforce? (e.g. what happens once an order is delivered?)</li>
 *     <li>where does "now" come from, when creating an order? (hint: {@link java.time.Clock})</li>
 * </ul>
 */
package it.unibo.ddd.exercises.orders;
