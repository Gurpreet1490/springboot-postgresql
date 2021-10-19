package com.myproject.springboot.postgresql.Password;

/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/
import com.myproject.springboot.postgresql.exception.ResourceNotFoundException;
import com.myproject.springboot.postgresql.model.Customer;
import com.myproject.springboot.postgresql.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.math.BigInteger;

import javax.transaction.Transactional;
import java.security.MessageDigest;

@Service
@Transactional
@Component
//@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer registerUser(String firstName, String lastName, String emailId, String password) {
        String passwordHash = this.getSecurePassword(password);
        if (emailId == null || password == null) {
            return null;
        } else {
//            if(customerRepository.findByEmailId(emailId).isPresent()) {
//                    System.out.println("Duplicate User");
//                    return null;
//            }
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setEmailId(emailId);
            customer.setPassword(passwordHash);
            return customerRepository.save(customer);
        }
    }

    public Customer authenticate(String emailId, String password) {
        String passwordHash = this.getSecurePassword(password);
        return customerRepository.findByEmailIdAndPassword(emailId, passwordHash).orElse(null);
    }

    public Customer accountUser(String firstName, String lastName){
        return customerRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);
    }

    public String getSecurePassword(String password) {
        String toReturn = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(password.getBytes("utf8"));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }


    public void updatePasswordToken(String token, String emailId) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByEmailId(emailId);
        if (customer != null) {
            customer.setPasswordToken(token);
            customerRepository.save(customer);
        } else {
            throw new ResourceNotFoundException("Could not find any customer with the email " + emailId);
        }
    }

    public Customer get(String passwordToken) {
        return customerRepository.findByPasswordToken(passwordToken);
    }

    public void updatePassword(Customer customer, String newPassword) {
        String passwordHash = this.getSecurePassword(newPassword);
        customer.setPassword(passwordHash);
        customer.setPasswordToken("");
        customerRepository.save(customer);
    }
}
/*
    public void updatePassword(Customer customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);

        customer.setResetPasswordToken(null);
        customerRepository.save(customer);
    }
*/



