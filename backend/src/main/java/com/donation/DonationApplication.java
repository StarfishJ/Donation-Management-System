package com.donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Donation Management System Main Application Class
 * 
 * @author Your Name
 * @version 1.0.0
 */
@SpringBootApplication
public class DonationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonationApplication.class, args);
        System.out.println("Donation Management System Backend Service Started!");
        System.out.println("Access URL: http://localhost:8080/api");
    }
}
