package com.cgi.poc.customer.service.converter;

import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.dto.CustomerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;

/**
 * Converter used to convert {@link Customer} entity instances
 * to {@link CustomerType} dto and vice versa.
 */

@Component
public class CustomerConverter {

    @Autowired
    private DateConverter dateConverter;

    /**
     * Converts a {@link CustomerType} dto to {@link Customer} entity.
     *
     * @param dto the dto to convert
     * @return the Customer entity created from the dto.
     */
    public Customer toEntity(CustomerType dto) {
        Customer entity = new Customer();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setNationalNumber(dto.getNationalNumber());
        entity.setBirthDate(dto.getBirthDate().toGregorianCalendar().getTime());
        return entity;
    }

    /**
     * Converts a {@link Customer} entity to {@link CustomerType} dto.
     *
     * @param entity the dto to convert
     * @return the CustomerType dto created from the entity.
     */

    public CustomerType toDto(Customer entity) {
        CustomerType dto = new CustomerType();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setNationalNumber(entity.getNationalNumber());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(entity.getBirthDate());
        dto.setBirthDate(dateConverter.toXMLGregorianCalendar(entity.getBirthDate()));
        return dto;
    }
}

