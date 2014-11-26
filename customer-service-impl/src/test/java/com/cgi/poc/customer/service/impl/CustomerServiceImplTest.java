package com.cgi.poc.customer.service.impl;

import com.cgi.poc.customer.service.converter.CustomerConverter;
import com.cgi.poc.customer.service.dao.CustomerDAO;
import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.CustomerService;
import com.cgi.service.customer.CustomerTooYoungFault;
import com.cgi.service.customer.DuplicateNissFault;
import com.cgi.service.customer.dto.CustomerType;
import com.cgi.service.customer.dto.GetCustomerByNissRequestDTO;
import com.cgi.service.customer.dto.GetCustomerByNissResponseDTO;
import com.cgi.service.customer.dto.RegisterCustomerRequestDTO;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.cgi.poc.customer.service.fixture.CustomerTestFixtures.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {


    @Mock
    private CustomerConverter mockCustomerConverter;

    @Mock
    private CustomerDAO<String, Customer> mockCustomerDAO;

    @InjectMocks
    private CustomerService testedClass = new CustomerServiceImpl();

    @Test
    public void shouldRetrieveCustomerByNationalNumberSuccessfully() throws Exception {

        Customer customerEntityFixture = createCustomerEntity();
        CustomerType customerDtoFixture = createCustomerDTO();

        GetCustomerByNissRequestDTO request = new GetCustomerByNissRequestDTO();
        request.setNiss(DEFAULT_NATIONAL_NUMBER);
        // Given
        given(mockCustomerDAO.findByNationalNumber(DEFAULT_NATIONAL_NUMBER)).willReturn(customerEntityFixture);
        given(mockCustomerConverter.toDto(customerEntityFixture)).willReturn(customerDtoFixture);
        // When
        GetCustomerByNissResponseDTO dto = testedClass.getCustomerByNiss(request);
        // Then
        verifyDtoContent(dto.getCustomer(), customerDtoFixture);
        verify(mockCustomerDAO, times(1)).findByNationalNumber(DEFAULT_NATIONAL_NUMBER);
        verify(mockCustomerConverter, times(1)).toDto(customerEntityFixture);
    }

    @Test
    public void shouldNotFailWhenRetrievingNonExistingCustomerByNationalNumber() throws Exception {
        // Given
        GetCustomerByNissRequestDTO request = new GetCustomerByNissRequestDTO();
        request.setNiss(DEFAULT_NATIONAL_NUMBER);
        given(mockCustomerDAO.findByNationalNumber(DEFAULT_NATIONAL_NUMBER)).willReturn(null);
        // When
        GetCustomerByNissResponseDTO dto = testedClass.getCustomerByNiss(request);
        // Then
        assertNull(dto.getCustomer());
        verify(mockCustomerDAO, times(1)).findByNationalNumber(DEFAULT_NATIONAL_NUMBER);
        verify(mockCustomerConverter, never()).toDto(any(Customer.class));
    }

    @Test
    public void shouldRegisterNewCustomerSuccessFully() throws Exception {
        Customer successfulCustomerEntityFixture = createCustomerEntity();
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        CustomerType customerDtoFixture = new CustomerType();
        request.setCustomer(customerDtoFixture);
        // Given
        given(mockCustomerConverter.toEntity(customerDtoFixture)).willReturn(successfulCustomerEntityFixture);
        given(mockCustomerDAO.findByNationalNumber(DEFAULT_NATIONAL_NUMBER)).willReturn(null);
        // When
        testedClass.registerCustomer(request);
        // Then
        verify(mockCustomerConverter, times(1)).toEntity(request.getCustomer());
        verify(mockCustomerDAO, times(1)).store(successfulCustomerEntityFixture);
        verify(mockCustomerDAO, times(1)).findByNationalNumber(successfulCustomerEntityFixture.getNationalNumber());
    }

    @Test(expected = CustomerTooYoungFault.class)
    public void shouldRegistrationFailWhenRegisteringMinorVisitor() throws Exception {
        Customer tooYoungCustomerEntityFixture = createTooYoungCustomerEntity();
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        CustomerType customerDtoFixture = new CustomerType();
        request.setCustomer(customerDtoFixture);
        // Given
        given(mockCustomerConverter.toEntity(customerDtoFixture)).willReturn(tooYoungCustomerEntityFixture);
        given(mockCustomerDAO.findByNationalNumber(DEFAULT_NATIONAL_NUMBER)).willReturn(null);
        // When
        try {
            testedClass.registerCustomer(request);
        } finally {
            // Then
            verify(mockCustomerDAO, never()).store(any(Customer.class));
            verify(mockCustomerDAO, never()).findByNationalNumber(any(String.class));
        }
    }

    @Test(expected = DuplicateNissFault.class)
    public void shouldRegistrationFailWhenRegisteringDuplicateNatiolanNumber() throws Exception {
        Customer successfulCustomerEntityFixture = createCustomerEntity();
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        CustomerType customerDtoFixture = new CustomerType();
        request.setCustomer(customerDtoFixture);
        // Given
        when(mockCustomerConverter.toEntity(customerDtoFixture)).thenReturn(successfulCustomerEntityFixture);
        when(mockCustomerDAO.findByNationalNumber(DEFAULT_NATIONAL_NUMBER)).thenReturn(new Customer());
        try {
            // When
            testedClass.registerCustomer(request);
        } finally {
            // Then
            verify(mockCustomerConverter, times(1)).toEntity(request.getCustomer());
            verify(mockCustomerDAO, times(1)).findByNationalNumber(successfulCustomerEntityFixture.getNationalNumber());
            verify(mockCustomerDAO, never()).store(any(Customer.class));
        }
    }

    private void verifyDtoContent(CustomerType actual, CustomerType expected) {
        // Make sure it is not not null by checking with an assertNotNull so that error messages will be more clean
        // than a nullPointerException on the next assertion.
        assertNotNull("Returned customer cannot be null", actual);
        assertThat(actual.getFirstName(), equalTo(expected.getFirstName()));
        assertThat(actual.getLastName(), equalTo(expected.getLastName()));
        assertThat(actual.getNiss(), equalTo(expected.getNiss()));
        assertThat(actual.getBirthDate(), equalTo(expected.getBirthDate()));
    }
}