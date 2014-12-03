package com.cgi.poc.service.customer.ws;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Simply launch the app by creating a spring app context that will start the Jetty engine
 * Easy launch within your IDE.
 *
 * Jetty engine will keep the application up until you kill it.
 */
public class AppLauncher {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("bean-context.xml");
    }

}
