package it.unibo.ddd.exercises.customers;

import java.util.Date;

public interface CustomerFactory {
    TaxCode ComputeTaxCode(String name, String surname, Date birthDate, String birthPlace);

    Customer NewCustomerPerson(String name, String surname, Date birthDate, String birthPlace, String email);

    Customer NewCustomerPerson(TaxCode code, String fullName, String email);

    Customer NewCustomerCompany(VatNumber number, String fullName, String email);
}