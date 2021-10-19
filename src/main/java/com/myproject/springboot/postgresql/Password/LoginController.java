package com.myproject.springboot.postgresql.Password;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.myproject.springboot.postgresql.model.Customer;
import com.myproject.springboot.postgresql.repository.CustomerRepository;


@RestController
@RequestMapping("/api/v2")

public class LoginController{
    @Autowired
        private CustomerRepository customerRepository;

    @Autowired
        CustomerService customerService;

//    @GetMapping(value = "/login")
//        public String getLoginPage(Model model) {
//            model.addAttribute("loginRequest", new Customer());
//            return "login_page";
//        }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
        public String login(@Validated @RequestBody Customer customer, Model model)
        {
            // return (customer != null || !customer.getEmailId().isEmpty()) ? customer.getEmailId() : "error";
                Customer authenticated = customerService.authenticate(customer.getEmailId(), customer.getPassword());
                if (authenticated != null) {
                    model.addAttribute("userLogin", authenticated.getEmailId());
                    return "My_Account";
                } else {
                    return "Error";
                 }
        }
}
