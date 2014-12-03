package com.cgi.poc.customer.service.impl;

import com.cgi.poc.customer.service.converter.CustomerConverter;
import com.cgi.poc.customer.service.dao.CustomerDAO;
import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.CustomerNotAllowedFault;
import com.cgi.service.customer.CustomerService;
import com.cgi.service.customer.NationalNumberAlreadyRegisteredFault;
import com.cgi.service.customer.dto.GetCustomerByNationalNumberRequestDTO;
import com.cgi.service.customer.dto.GetCustomerByNationalNumberResponseDTO;
import com.cgi.service.customer.dto.RegisterCustomerRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebParam;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Customer Registration Service implementation.
 * <p/>
 * Simple implementation of a contract-first SOAP service.
 */
public class CustomerServiceImpl implements CustomerService {

    private static final int MINIMUM_AGE = 18;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private CustomerDAO customerDAO;

    /**
     * Register a new customer in the system
     *
     * @param registerCustomerRequestDTO containing the new Customer to register.
     * @throws CustomerNotAllowedFault              in case the customer does not have the minimal age to register to this service
     * @throws NationalNumberAlreadyRegisteredFault in case a customer with the same the national number already exists in the system.
     */
    @Override
    public void registerCustomer(@WebParam(partName = "parameters", name = "registerCustomerRequest", targetNamespace = "http://service.cgi.com/customer") RegisterCustomerRequestDTO registerCustomerRequestDTO) throws CustomerNotAllowedFault, NationalNumberAlreadyRegisteredFault {
        Customer customer = customerConverter.toEntity(registerCustomerRequestDTO.getCustomer());
        validateCustomer(customer);
        customerDAO.store(customer);
    }

    /**
     * Retrieve a Customer using its National Number.
     *
     * @param getCustomerByNationalNumberRequestDTO that contains the national number to search for.
     * @return dto containing a reference to a ${@link com.cgi.service.customer.dto.CustomerType} in case of
     * successful search or with a <b>null</b> reference in cas no customer has been found.
     */
    @Override
    public GetCustomerByNationalNumberResponseDTO getCustomerByNationalNumber(@WebParam(partName = "parameters", name = "getCustomerByNationalNumberRequest", targetNamespace = "http://service.cgi.com/customer") GetCustomerByNationalNumberRequestDTO getCustomerByNationalNumberRequestDTO) {
        GetCustomerByNationalNumberResponseDTO response = new GetCustomerByNationalNumberResponseDTO();
        Customer customer = customerDAO.findByNationalNumber(getCustomerByNationalNumberRequestDTO.getNationalNumber());
        if (customer != null) {
            response.setCustomer(customerConverter.toDto(customer));
        }
        return response;
    }

    private void validateCustomer(Customer customer) throws CustomerNotAllowedFault, NationalNumberAlreadyRegisteredFault {
        if (isCustomerYoungerThan(customer.getBirthDate(), MINIMUM_AGE)) {
            throw new CustomerNotAllowedFault("You are too young to use this service!");
        }
        if (customerDAO.findByNationalNumber(customer.getNationalNumber()) != null) {
            throw new NationalNumberAlreadyRegisteredFault("A consumer with the national number " + customer.getNationalNumber() + " already exists");
        }
    }

    private boolean isCustomerYoungerThan(Date birthDate, int minimalAge) {
        Calendar requiredBirthDateCalendar = new GregorianCalendar();
        requiredBirthDateCalendar.add(Calendar.YEAR, -minimalAge);
        Calendar birthDateCalendar = new GregorianCalendar();
        birthDateCalendar.setTime(birthDate);
        return birthDateCalendar.after(requiredBirthDateCalendar);
    }
}
