package com.cgi.poc.customer.service.dao.impl;


import com.cgi.poc.customer.service.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryCustomerDAOTest {

    private static final String NATIONAL_NUMBER = "1";

    @Mock
    private Map<String, Customer> storage;

    @InjectMocks
    private InMemoryCustomerDAO testedClass = new InMemoryCustomerDAO();

    @Test
    public void shouldFindCustomerByNationalNumberSuccessfully() throws Exception {
        // Given
        Customer expected = new Customer();
        given(storage.get(NATIONAL_NUMBER)).willReturn(expected);
        // When
        Customer actual = testedClass.findByNationalNumber(NATIONAL_NUMBER);
        // Then
        assertThat(actual, is(expected));
        verify(storage, times(1)).get(NATIONAL_NUMBER);
    }

    @Test
    public void shouldReturnNullWhenSearchingNonExistingCustomerByNationalNumber() throws Exception {
        // Given
        given(storage.get(NATIONAL_NUMBER)).willReturn(null);
        // When
        Customer actual = testedClass.findByNationalNumber(NATIONAL_NUMBER);
        // Then
        assertNull(actual);
    }

    @Test
    public void shouldStoreCustomerSuccessfully() throws Exception {
        // Given
        Customer customer = new Customer();
        customer.setNationalNumber(NATIONAL_NUMBER);
        // When
        testedClass.store(customer);
        // Then
        verify(storage, times(1)).put(NATIONAL_NUMBER, customer);
    }
}