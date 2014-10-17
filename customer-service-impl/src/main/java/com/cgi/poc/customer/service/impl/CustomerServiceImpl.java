package com.cgi.poc.customer.service.impl;

import com.cgi.poc.customer.service.converter.CustomerConverter;
import com.cgi.poc.customer.service.dao.CustomerDAO;
import com.cgi.poc.customer.service.entity.Customer;
import com.cgi.service.customer.CustomerService;
import com.cgi.service.customer.CustomerTooYoungFault;
import com.cgi.service.customer.DuplicateNissFault;
import com.cgi.service.customer.dto.GetCustomerByNissRequestDTO;
import com.cgi.service.customer.dto.GetCustomerByNissResponseDTO;
import com.cgi.service.customer.dto.RegisterCustomerRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebParam;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Customer Service implementation.
 */
public class CustomerServiceImpl implements CustomerService {

    private static final int MINIMUM_AGE = 18;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private CustomerDAO<String, Customer> customerDAO;

    /**
     * Retrieve a Customer using its National Number (Niss)
     *
     * @param GetCustomerByNissRequestDTO that contains the national number to search for.
     * @return GetCustomerByNissResponseDTO with CustomerType containing a reference to the customer or with
     * a <b>null</b> reference in cas no customer has been found using the provided national number
     */
    @Override
    public GetCustomerByNissResponseDTO getCustomerByNiss(@WebParam(partName = "parameters", name = "getCustomerByNissRequest", targetNamespace = "http://service.cgi.com/customer") GetCustomerByNissRequestDTO getCustomerByNissRequestDTO) {

        GetCustomerByNissResponseDTO response = new GetCustomerByNissResponseDTO();
        Customer customer = customerDAO.findByNationalNumber(getCustomerByNissRequestDTO.getNiss());
        if (customer != null) {
            response.setCustomer(customerConverter.toDto(customer));
        }
        return response;
    }

    /**
     * Register a new customer in the system
     *
     * @param RegisterCustomerRequestDTO that contains the new customer to register.
     * @throws CustomerTooYoungFault in case the customer does not have the minimal age to register to this service
     * @throws DuplicateNissFault in case a customer with the same the national number already exists in the system.
     */
    @Override
    public void registerCustomer(@WebParam(partName = "parameters", name = "registerCustomerRequest", targetNamespace = "http://service.cgi.com/customer") RegisterCustomerRequestDTO registerCustomerRequestDTO) throws CustomerTooYoungFault, DuplicateNissFault {

        Customer customer = customerConverter.toEntity(registerCustomerRequestDTO.getCustomer());

        if (isCustomerYoungerThan(customer.getBirthDate(), MINIMUM_AGE)) {
            throw new CustomerTooYoungFault("You are too young to use this service!");
        }

        if (customerDAO.findByNationalNumber(customer.getNationalNumber()) != null) {
            throw new DuplicateNissFault("A consumer with the national number " + customer.getNationalNumber() + " already exists");
        }

        customerDAO.store(customer);
    }


    private boolean isCustomerYoungerThan(Date birthDate, int minimalAge) {

        Calendar requiredBirthDateCalendar = new GregorianCalendar();
        requiredBirthDateCalendar.add(Calendar.YEAR, -minimalAge);
        Calendar birthDateCalendar = new GregorianCalendar();
        birthDateCalendar.setTime(birthDate);
        return birthDateCalendar.after(requiredBirthDateCalendar);

    }
}
