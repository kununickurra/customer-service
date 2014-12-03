package com.cgi.poc.customer.service.fixture;

import com.cgi.poc.customer.service.converter.DateConverter;
import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.dto.CustomerType;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Test fixtures for the Customer and CustomerType
 */
public class CustomerTestFixtures {

    private CustomerTestFixtures() { /* Do not instantiate me please*/}

    public static final int MINIMAL_AGE = 18;

    public static final String NATIONAL_NUMBER = "1";
    private static final String LAST_NAME = "Last";
    private static final String FIRST_NAME = "First";
    public static final Date ENTITY_BIRTH_DATE;
    public static final XMLGregorianCalendar DTO_BIRTH_DATE;

    static {
        // Initialize default birth date
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        // Change the default date to test the edge case with a customer that has just the minimal age to register.
        gregorianCalendar.add(Calendar.YEAR, -MINIMAL_AGE);
        gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0);
        gregorianCalendar.set(Calendar.MINUTE, 0);
        gregorianCalendar.set(Calendar.SECOND, 0);
        gregorianCalendar.set(Calendar.MILLISECOND, 0);
        ENTITY_BIRTH_DATE = gregorianCalendar.getTime();
        // Transform it into an XMLGregorianCalendar using DateConverter.
        // This can be done here as the DateConverter is tested separately in it's own test class.
        DTO_BIRTH_DATE = new DateConverter().toXMLGregorianCalendar(ENTITY_BIRTH_DATE);
    }

    public static CustomerType createCustomerDTO() {
        CustomerType dto = new CustomerType();
        dto.setLastName(LAST_NAME);
        dto.setFirstName(FIRST_NAME);
        dto.setNationalNumber(NATIONAL_NUMBER);
        dto.setBirthDate(DTO_BIRTH_DATE);
        return dto;
    }

    public static Customer createValidCustomerEntity() {
        Customer entity = new Customer();
        entity.setLastName(LAST_NAME);
        entity.setFirstName(FIRST_NAME);
        entity.setNationalNumber(NATIONAL_NUMBER);
        entity.setBirthDate(ENTITY_BIRTH_DATE);
        return entity;
    }

    public static Customer createMinorCustomerEntity() {
        // Create a customer that is 1 day too young to be allowed to register.
        Customer customer = createValidCustomerEntity();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(ENTITY_BIRTH_DATE.getTime()));
        // Tomorrow I will be able to register but NOT today
        calendar.add(Calendar.DATE, 1);
        customer.setBirthDate(calendar.getTime());
        return customer;
    }

}

