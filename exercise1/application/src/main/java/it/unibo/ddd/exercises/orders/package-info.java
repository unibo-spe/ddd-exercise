/**
 * Application services of the orders context, and the domain events they publish.
 * <p>
 * TODO: model here (cf. step 5 of exercise1/walkthrough.md)
 * <ul>
 *     <li>a service computing prices of products and orders, in any currency, at any moment</li>
 *     <li>a service registering orders, and publishing an event whenever "a new order is registered"</li>
 *     <li>the event itself: which information should it carry?</li>
 * </ul>
 * Services should receive their dependencies (repositories, other services, the clock) via their constructors.
 */
package it.unibo.ddd.exercises.orders;
