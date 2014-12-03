package com.cgi.poc.customer.service.converter;

import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Converter used to generate a {@link XMLGregorianCalendar}
 * from a given {@link Date}.
 */

@Component
public class DateConverter {

    /**
     * Converts the {@link Date} received to a {@link XMLGregorianCalendar}
     * TimeZone and Time (HH:MM:SS:MMs) information should not be present in the generated calendar
     *
     * @param date the input date to convert
     * @return The {@link XMLGregorianCalendar} with only the Date information (YYYYmmDD)
     */
    public XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            // Just copy the date as we are not interested in the time information.
            xmlGregorianCalendar.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            xmlGregorianCalendar.setMonth(calendar.get(Calendar.MONTH) + 1);
            xmlGregorianCalendar.setYear(calendar.get(Calendar.YEAR));
            return xmlGregorianCalendar;
        } catch (DatatypeConfigurationException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
