package it.unibo.ddd.exercises.customers;

import it.unibo.ddd.exercises.customers.impl.CustomerFactoryImpl;
import it.unibo.ddd.exercises.customers.impl.InMemoryCustomerRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomersTest {
    private final CustomerFactory factory = new CustomerFactoryImpl();

    @Test
    void valueObjectsAreEqualIffTheirValuesAreEqual() {
        assertEquals(factory.taxCode("RSSMRA80A01H501U"), factory.taxCode(" rssmra80a01h501u "));
        assertNotEquals(factory.taxCode("RSSMRA80A01H501U"), factory.taxCode("VRDLGU80A01H501U"));
    }

    @Test
    void factoryRejectsMalformedIdentifiers() {
        assertThrows(IllegalArgumentException.class, () -> factory.taxCode("not a tax code"));
        assertThrows(IllegalArgumentException.class, () -> factory.vatNumber("123"));
    }

    @Test
    void entitiesAreEqualIffTheirIdentifiersAreEqual() {
        var id = factory.vatNumber("01234567890");
        var acme = factory.newCustomerCompany(id, "ACME", "info@acme.com");
        var renamed = factory.newCustomerCompany(id, "ACME Inc.", "contact@acme.com");
        assertEquals(acme, renamed);
        assertNotEquals(acme, factory.newCustomerCompany(factory.vatNumber("09876543210"), "ACME", "info@acme.com"));
    }

    @Test
    void entitiesEnforceTheirInvariants() {
        var mario = factory.newCustomerPerson(factory.taxCode("RSSMRA80A01H501U"), "Mario Rossi", "mario@rossi.it");
        mario.setEmail("mario.rossi@unibo.it");
        assertEquals("mario.rossi@unibo.it", mario.getEmail());
        assertThrows(IllegalArgumentException.class, () -> mario.setEmail("not an email"));
        assertThrows(IllegalArgumentException.class, () -> mario.setName(" "));
    }

    @Test
    void registryStoresAndRetrievesCustomers() {
        CustomerRegistry registry = new InMemoryCustomerRegistry();
        var mario = factory.newCustomerPerson(factory.taxCode("RSSMRA80A01H501U"), "Mario Rossi", "mario@rossi.it");
        registry.addNewCustomer(mario);
        assertThrows(IllegalArgumentException.class, () -> registry.addNewCustomer(mario));
        assertEquals(mario, registry.findCustomerById(factory.taxCode("RSSMRA80A01H501U")).orElseThrow());
        assertEquals(List.of(mario), registry.findCustomersByName("Mario Rossi"));
        registry.removeCustomer(mario);
        assertTrue(registry.findCustomerById(mario.getID()).isEmpty());
    }
}
