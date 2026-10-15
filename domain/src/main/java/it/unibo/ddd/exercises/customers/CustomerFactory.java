package it.unibo.ddd.exercises.customers;

import java.time.LocalDate;

/** Factory: the only way to create customers and their identifiers, so that invariants are checked once. */
public interface CustomerFactory {
    TaxCode taxCode(String value);

    /**
     * Computes the Italian tax code of a person.
     *
     * @param birthPlace the name of an Italian municipality, e.g. {@code "Roma"},
     *                   possibly followed by the province, e.g. {@code "Castro (LE)"}, if ambiguous
     * @throws IllegalArgumentException if the birth place is unknown or ambiguous
     */
    TaxCode computeTaxCode(String name, String surname, LocalDate birthDate, Gender gender, String birthPlace);

    VatNumber vatNumber(String value);

    Customer newCustomerPerson(TaxCode code, String fullName, String email);

    Customer newCustomerPerson(String name, String surname, LocalDate birthDate, Gender gender, String birthPlace, String email);

    Customer newCustomerCompany(VatNumber number, String fullName, String email);
}
