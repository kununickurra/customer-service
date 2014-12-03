package com.cgi.poc.customer.service.converter;

import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.dto.CustomerType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.cgi.poc.customer.service.fixture.CustomerTestFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CustomerConverterTest {

    @Mock
    DateConverter dateConverter;

    @InjectMocks
    private CustomerConverter testedClass = new CustomerConverter();

    @Test
    public void shouldConvertToEntitySuccessfully() throws Exception {
        // Given
        Customer expected = createValidCustomerEntity();
        // When
        Customer result = testedClass.toEntity(createCustomerDTO());
        // Then
        // This can be done in one statement as the Customer class has a equals() method implemented.
        assertThat(result, is(expected));
    }

    @Test
    public void shouldConvertToDtoSuccessfully() throws Exception {
        // Given
        given(dateConverter.toXMLGregorianCalendar(ENTITY_BIRTH_DATE)).willReturn(DTO_BIRTH_DATE);
        // When
        CustomerType expected = createCustomerDTO();
        // Then
        CustomerType result = testedClass.toDto(createValidCustomerEntity());
        // Need to check fields one by one as the CustomerType class does not have a equals method.
        // and is part of the spec imported from another JAR file.
        // This class is a DTO class generated with a wsdl2java from a wsdl/xsd - note that it is possible to
        // generate also a equals method with wsdl2java.
        assertThat(result.getLastName(), is(expected.getLastName()));
        assertThat(result.getFirstName(), is(expected.getFirstName()));
        assertThat(result.getNationalNumber(), is(expected.getNationalNumber()));
        assertThat(result.getBirthDate(), is(expected.getBirthDate()));
    }

}