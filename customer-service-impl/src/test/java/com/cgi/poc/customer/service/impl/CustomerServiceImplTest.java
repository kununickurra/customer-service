package com.cgi.poc.customer.service.impl;

import com.cgi.poc.customer.service.converter.CustomerConverter;
import com.cgi.poc.customer.service.dao.CustomerDAO;
import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.CustomerNotAllowedFault;
import com.cgi.service.customer.CustomerService;
import com.cgi.service.customer.NationalNumberAlreadyRegisteredFault;
import com.cgi.service.customer.dto.CustomerType;
import com.cgi.service.customer.dto.GetCustomerByNationalNumberRequestDTO;
import com.cgi.service.customer.dto.GetCustomerByNationalNumberResponseDTO;
import com.cgi.service.customer.dto.RegisterCustomerRequestDTO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.cgi.poc.customer.service.fixture.CustomerTestFixtures.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerConverter mockCustomerConverter;

    @Mock
    private CustomerDAO mockCustomerDAO;

    @InjectMocks
    private CustomerService testedClass = new CustomerServiceImpl();

    @Test
    public void shouldRetrieveCustomerByNationalNumberSuccessfully() throws Exception {
        // Given
        Customer customerEntityFixture = createValidCustomerEntity();
        CustomerType customerDtoFixture = createCustomerDTO();
        given(mockCustomerDAO.findByNationalNumber(NATIONAL_NUMBER)).willReturn(customerEntityFixture);
        given(mockCustomerConverter.toDto(customerEntityFixture)).willReturn(customerDtoFixture);
        // When
        GetCustomerByNationalNumberRequestDTO request = new GetCustomerByNationalNumberRequestDTO();
        request.setNationalNumber(NATIONAL_NUMBER);
        GetCustomerByNationalNumberResponseDTO dto = testedClass.getCustomerByNationalNumber(request);
        // Then
        verifyDtoContent(dto.getCustomer(), customerDtoFixture);
        verify(mockCustomerDAO, times(1)).findByNationalNumber(NATIONAL_NUMBER);
        verify(mockCustomerConverter, times(1)).toDto(customerEntityFixture);
    }

    @Test
    public void shouldReturnNullValueWhenRetrievingNonExistingCustomerByNationalNumber() throws Exception {
        // Given
        GetCustomerByNationalNumberRequestDTO request = new GetCustomerByNationalNumberRequestDTO();
        request.setNationalNumber(NATIONAL_NUMBER);
        given(mockCustomerDAO.findByNationalNumber(NATIONAL_NUMBER)).willReturn(null);
        // When
        GetCustomerByNationalNumberResponseDTO dto = testedClass.getCustomerByNationalNumber(request);
        // Then
        assertNull(dto.getCustomer());
        verify(mockCustomerDAO, times(1)).findByNationalNumber(NATIONAL_NUMBER);
        verify(mockCustomerConverter, never()).toDto(any(Customer.class));
    }

    @Test
    public void shouldRegisterNewCustomerSuccessFully() throws Exception {
        // Given
        Customer successfulCustomerEntityFixture = createValidCustomerEntity();
        CustomerType customerDtoFixture = new CustomerType();
        given(mockCustomerConverter.toEntity(customerDtoFixture)).willReturn(successfulCustomerEntityFixture);
        given(mockCustomerDAO.findByNationalNumber(NATIONAL_NUMBER)).willReturn(null);
        // When
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        request.setCustomer(customerDtoFixture);
        testedClass.registerCustomer(request);
        // Then
        verify(mockCustomerConverter, times(1)).toEntity(request.getCustomer());
        verify(mockCustomerDAO, times(1)).store(successfulCustomerEntityFixture);
        verify(mockCustomerDAO, times(1)).findByNationalNumber(successfulCustomerEntityFixture.getNationalNumber());
    }

    @Test(expected = NationalNumberAlreadyRegisteredFault.class)
    public void shouldRegistrationFailWhenRegisteringDuplicateNationalNumber() throws Exception {
        // Given
        Customer successfulCustomerEntityFixture = createValidCustomerEntity();
        CustomerType customerDtoFixture = new CustomerType();
        given(mockCustomerConverter.toEntity(customerDtoFixture)).willReturn(successfulCustomerEntityFixture);
        given(mockCustomerDAO.findByNationalNumber(NATIONAL_NUMBER)).willReturn(new Customer());

        // When
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        try {
            request.setCustomer(customerDtoFixture);
            testedClass.registerCustomer(request);
        } finally {
            // Then
            verify(mockCustomerConverter, times(1)).toEntity(request.getCustomer());
            verify(mockCustomerDAO, times(1)).findByNationalNumber(successfulCustomerEntityFixture.getNationalNumber());
            verify(mockCustomerDAO, never()).store(any(Customer.class));
        }
    }

    @Test(expected = CustomerNotAllowedFault.class)
    public void shouldRegistrationFailWhenTryingToRegisterMinorVisitor() throws Exception {
        Customer minorCustomerEntityFixture = createMinorCustomerEntity();
        CustomerType customerDtoFixture = new CustomerType();
        // Given
        given(mockCustomerConverter.toEntity(customerDtoFixture)).willReturn(minorCustomerEntityFixture);
        given(mockCustomerDAO.findByNationalNumber(NATIONAL_NUMBER)).willReturn(null);

        // When
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        request.setCustomer(customerDtoFixture);
        try {
            testedClass.registerCustomer(request);
        } finally {
            // Then
            // Make sure that the customer entity is NOT stored!
            verify(mockCustomerDAO, never()).store(any(Customer.class));
            // Just to make sure that there is no useless call to
            verify(mockCustomerDAO, never()).findByNationalNumber(any(String.class));
        }
    }

    private void verifyDtoContent(CustomerType actual, CustomerType expected) {
        // Make sure it is not null with an error messages will be more clean
        // than a nullPointerException on the next assertion.
        assertThat("Expecting a customer and not a null value", actual, is(notNullValue()));
        assertThat(EqualsBuilder.reflectionEquals(actual, expected), is(true));
        assertThat(actual.getFirstName(), equalTo(expected.getFirstName()));
        assertThat(actual.getLastName(), equalTo(expected.getLastName()));
        assertThat(actual.getNationalNumber(), equalTo(expected.getNationalNumber()));
        assertThat(actual.getBirthDate(), equalTo(expected.getBirthDate()));
    }
}