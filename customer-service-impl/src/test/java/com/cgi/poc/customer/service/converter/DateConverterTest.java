package com.cgi.poc.customer.service.converter;

import org.junit.Test;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class DateConverterTest {

    private DateConverter testClass = new DateConverter();

    @Test
    public void shouldIgnoreTimeInformationAndConvertToXMLGregorianCalendar() throws Exception {
        // Given
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.YEAR, 1988);
        calendar.set(Calendar.MONTH, 12);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 30);
        calendar.set(Calendar.MILLISECOND, 40);
        // When
        XMLGregorianCalendar xmlCalendar = testClass.toXMLGregorianCalendar(calendar.getTime());
        // Then
        verifyDate(xmlCalendar, calendar);
        verifyCalendarTimeAndTimeZoneAreEmpty(xmlCalendar);
    }

    private void verifyDate(XMLGregorianCalendar xmlCalendar, Calendar expected) {
        GregorianCalendar gregorianCalendar = xmlCalendar.toGregorianCalendar();
        assertThat(gregorianCalendar.get(Calendar.YEAR), is(expected.get(Calendar.YEAR)));
        assertThat(gregorianCalendar.get(Calendar.MONTH), is(expected.get(Calendar.MONTH)));
        assertThat(gregorianCalendar.get(Calendar.YEAR), is(expected.get(Calendar.YEAR)));
    }

    private void verifyCalendarTimeAndTimeZoneAreEmpty(XMLGregorianCalendar calendar) {
        assertThat(calendar.getHour(), is(DatatypeConstants.FIELD_UNDEFINED));
        assertThat(calendar.getMinute(), is(DatatypeConstants.FIELD_UNDEFINED));
        assertThat(calendar.getSecond(), is(DatatypeConstants.FIELD_UNDEFINED));
        assertThat(calendar.getTimezone(), is(DatatypeConstants.FIELD_UNDEFINED));
        assertNull(calendar.getFractionalSecond());
        assertNull(calendar.getEon());
    }
}