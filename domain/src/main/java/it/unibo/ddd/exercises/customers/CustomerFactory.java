package it.unibo.ddd.exercises.customers;

/** Factory: the only way to create customers and their identifiers, so that invariants are checked once. */
public interface CustomerFactory {
    TaxCode taxCode(String value);

    VatNumber vatNumber(String value);

    Customer newCustomerPerson(TaxCode code, String fullName, String email);

    Customer newCustomerCompany(VatNumber number, String fullName, String email);
}
