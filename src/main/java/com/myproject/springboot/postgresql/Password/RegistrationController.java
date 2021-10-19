package com.myproject.springboot.postgresql.Password;

import com.myproject.springboot.postgresql.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

//@CrossOrigin(origins = "${client.url}")
@RestController
@RequestMapping("/api/v4")
public class RegistrationController {


    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
        public String getRegisterPage(Model model){
            model.addAttribute("registerRequest", new Customer());
            return "register_page";
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/register")
        public String register(@RequestBody Customer customer){

        System.out.println("register request: " + customer);
           Customer registeredUser =  customerService.registerUser(customer.getFirstName(), customer.getLastName(), customer.getEmailId(), customer.getPassword());
            return registeredUser == null ? "error_page" : "redirect:/login";
    }

}
