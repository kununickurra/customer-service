package com.cgi.poc.customer.service.converter;

import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.dto.CustomerType;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Converter used to convert {@link Customer} entity instances
 * to {@link CustomerType} dto and vice versa.
 */

@Component
public class CustomerConverter {

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
        entity.setNationalNumber(dto.getNiss());
        System.out.println(dto.getBirthDate());
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
        dto.setNiss(entity.getNationalNumber());
        dto.setBirthDate(convertToXMLGregorianCalendar(entity.getBirthDate()));
        return dto;
    }

    private XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            // Just copy the date as we are not interested in the time information.
            xmlGregorianCalendar.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            xmlGregorianCalendar.setMonth(calendar.get(Calendar.MONTH));
            xmlGregorianCalendar.setYear(calendar.get(Calendar.YEAR));
            return xmlGregorianCalendar;
        } catch (DatatypeConfigurationException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

