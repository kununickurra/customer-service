package com.cgi.poc.customer.service.fixture;

import com.cgi.poc.customer.service.converter.CustomerConverter;
import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.dto.CustomerType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Test fixtures for the Customer
 */
public class CustomerTestFixtures {

    private CustomerTestFixtures() { /* Do not instantiate me */}

    public static final String DEFAULT_NATIONAL_NUMBER = "1";

    private static final String LAST_NAME = "Last";
    private static final String FIRST_NAME = "First";
    private static final GregorianCalendar ENTITY_BIRTH_DATE;
    public static final XMLGregorianCalendar DTO_BIRTH_DATE;

    static {
        // Initialize default birth date for a customer that has 18 years old.
        ENTITY_BIRTH_DATE = new GregorianCalendar();
        ENTITY_BIRTH_DATE.add(Calendar.YEAR, -18);
        // Initialize the XMLGregorianCalendar based on the Entity Birth date create above.
        try {
            DTO_BIRTH_DATE = DatatypeFactory.newInstance().newXMLGregorianCalendar(ENTITY_BIRTH_DATE);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException();
        }
    }

    public static Customer createCustomerEntity() {
        Customer entity = new Customer();
        entity.setLastName(LAST_NAME);
        entity.setFirstName(FIRST_NAME);
        entity.setNationalNumber(DEFAULT_NATIONAL_NUMBER);
        entity.setBirthDate(ENTITY_BIRTH_DATE.getTime());
        return entity;
    }

    public static CustomerType createCustomerDTO() {
        CustomerType dto = new CustomerType();
        dto.setLastName(LAST_NAME);
        dto.setFirstName(FIRST_NAME);
        dto.setNiss(DEFAULT_NATIONAL_NUMBER);
        dto.setBirthDate(DTO_BIRTH_DATE);
        return dto;
    }

    public static Customer createTooYoungCustomerEntity() {
        // Create a customer that is 1 day too young to be allowed to register.
        // Default is 18 years old.
        Customer customer = createCustomerEntity();
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);
        customer.setBirthDate(calendar.getTime());
        return customer;
    }

}
