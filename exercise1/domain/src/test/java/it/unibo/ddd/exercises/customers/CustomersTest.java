package it.unibo.ddd.exercises.customers;

import it.unibo.ddd.exercises.customers.impl.CustomerFactoryImpl;
import it.unibo.ddd.exercises.customers.impl.InMemoryCustomerRegistry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
    void factoryComputesTaxCodes() {
        var mario = factory.computeTaxCode("Mario", "Rossi", LocalDate.of(1980, 1, 1), Gender.MALE, "Roma");
        assertEquals(factory.taxCode("RSSMRA80A01H501U"), mario);
        // names with >= 4 consonants skip the 2nd one, women get +40 on the day, accents are ignored
        var maria = factory.computeTaxCode("Gianfranca", "Lucà", LocalDate.of(1995, 12, 31), Gender.FEMALE, "Castro (LE)");
        assertEquals("LCUGFR95T71M261", maria.getValue().substring(0, 15));
    }

    @Test
    void factoryRejectsUnknownOrAmbiguousBirthPlaces() {
        var birthDate = LocalDate.of(1980, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> factory.computeTaxCode("Mario", "Rossi", birthDate, Gender.MALE, "Atlantide"));
        assertThrows(IllegalArgumentException.class, () -> factory.computeTaxCode("Mario", "Rossi", birthDate, Gender.MALE, "Castro"));
    }

    @Test
    void personsCanBeCreatedOutOfTheirPersonalData() {
        var mario = factory.newCustomerPerson("Mario", "Rossi", LocalDate.of(1980, 1, 1), Gender.MALE, "Roma", "mario@rossi.it");
        assertEquals("RSSMRA80A01H501U", mario.getID().getValue());
        assertEquals("Mario Rossi", mario.getName());
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
