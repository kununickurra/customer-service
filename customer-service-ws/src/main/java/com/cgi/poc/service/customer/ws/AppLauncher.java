package com.cgi.poc.service.customer.ws;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Simply launch the app by creating a spring app context that will start the Jetty engine
 */
public class AppLauncher {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-context.xml");
    }
}
