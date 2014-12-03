Simple SOAP service implementation of a customer registration service with in memory storage.
It offers a registration functionality and a simple search based on a unique identifier (National Number)

!!! WARNING !!!
The specifications of the web service are needed to build this project. (contract-first implementation)
The required specification can be found by checking-out the repository "customer-service-spec" on git hub
https://github.com/kununickurra/customer-service-spec
and install it using maven (run mvn clean install inside the main directory)

How to start the service :
---------------------------

Only a debug mode has been created for now, simply run the com.cgi.poc.service.customer.ws.AppLauncher class and the
service will start.

Project can be easily refactored to generate a war file instead.


Where can I access the service :
--------------------------------

Default url is http://localhost:9001/CustomerService

Url is configured in the bean-context.xml file located in the customer-service-ws project.

Enjoy.

Comments are welcome :-)