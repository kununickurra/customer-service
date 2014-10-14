package com.cgi.poc.customer.service.converter;

import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.dto.CustomerType;
import org.junit.Test;

import static com.cgi.poc.customer.service.fixture.CustomerTestFixtures.createCustomerDTO;
import static com.cgi.poc.customer.service.fixture.CustomerTestFixtures.createCustomerEntity;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CustomerConverterTest {

    // The class currently under test.
    private CustomerConverter testedClass = new CustomerConverter();

    @Test
    public void shouldConvertToEntitySuccessfully() throws Exception {
        Customer expected = createCustomerEntity();
        Customer result = testedClass.toEntity(createCustomerDTO());
        // This can be done in one statement as the Customer class has a equals() method implemented.
        assertThat(result, is(expected));
    }

    @Test
    public void shouldConvertToDtoSuccessfully() throws Exception {
        CustomerType expected = createCustomerDTO();
        CustomerType result = testedClass.toDto(createCustomerEntity());
        // Need to check fields one by one as the CustomerType class does not have a equals method.
        // and is part of the spec imported from another JAR file.
        // This class is a DTO class generated with a wsdl2java from a wsdl/xsd - note that it is possible to
        // generate also a equals method with wsdl2java.
        assertThat(result.getLastName(), is(expected.getLastName()));
        assertThat(result.getFirstName(), is(expected.getFirstName()));
        assertThat(result.getNiss(), is(expected.getNiss()));
        assertThat(result.getBirthDate(), is(expected.getBirthDate()));
    }

}