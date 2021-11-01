package com.myproject.springboot.postgresql.Password;

import  java.io.UnsupportedEncodingException;
import com.myproject.springboot.postgresql.exception.ResourceNotFoundException;
import com.myproject.springboot.postgresql.model.Customer;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/api/v3")
public class ForgotPasswordController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/forgot_password")
        public String showForgotPasswordForm(Model model) {
            model.addAttribute("pageTitle", "Forgot Password");
            return "forgot_password_form";
        }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Async
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @RequestMapping(value = "/forgot_password", method = RequestMethod.POST, produces = { "application/json" })
    //@ResponseBody
    public String processForgotPassword (@Validated @RequestBody Customer customer, Model model) {


        String emailId = customer.getEmailId();
        String token = RandomString.make(45);

            try {
                customerService.updatePasswordToken(token, emailId);
                String resetPasswordLink = "http://localhost:3000/reset_password?token=" + token;
                sendEmail(emailId, resetPasswordLink);
                model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

            } catch (ResourceNotFoundException ex) {
                model.addAttribute("error", ex.getMessage());
            }
            catch (UnsupportedEncodingException | MessagingException e) {
                model.addAttribute("error", "Error while sending email.");
            }

            return "forgot_password_form";
        }

        private void sendEmail(String emailId, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
            String token = RandomString.make(45);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("gpreetwebsite14@gmail.com", "Customer Support");
            helper.setTo(emailId);

            String subject = "Here's the link to reset your password";

            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p"
                    + "<p>Click the link below to change your password:"
                    + "<p><b><a href=' " + resetPasswordLink + "'>Change my password</a></b></p>"
                    + "<p>Ignore this email if you do remember your password, or you have not made the request.</p>";

            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        }

    @CrossOrigin(origins = "*")
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam(value = "token") String token, Model model) {
        Customer customer = customerService.get(token);
        model.addAttribute("token", token);
        if (customer == null) {
            model.addAttribute("title", "Reset your password");
            model.addAttribute("message", "Invalid Token");
            return "message";}
        model.addAttribute("token", token);
        model.addAttribute("pageTitle", "Reset Your Password");
        return "reset_password_form";
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        Customer customer = customerService.get(token);
        if (customer == null) {
            model.addAttribute("title", "Reset your password");
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            customerService.updatePassword(customer, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }
        return "message";
    }

}
