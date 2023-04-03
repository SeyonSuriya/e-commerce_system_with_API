package com.ecommercesystem.registeruser.service.EmailService;


import com.ecommercesystem.registeruser.entity.User;
import com.ecommercesystem.registeruser.repository.OtpRepo;
import com.ecommercesystem.registeruser.repository.UserRepo;
import com.ecommercesystem.registeruser.service.OtpGenerator.OtpGeneratorService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class EmailServiceCenter implements EmailService{
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private OtpRepo otpRepo;
    @Autowired
    private OtpGeneratorService otpGeneratorService;
    @Autowired
    private UserRepo userRepo;

    Integer otp=0;
    @Override
    public String sendEmailVerification(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        otp = otpGeneratorService.generateOtp();
        List<User> user = userRepo.findUserByEmail(email);

        String toAddress = email;
        String fromAddress = "basnayakasanjeewa3@gmail.com";
        String senderName = "Book World";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.get(0).getFirstname()+user.get(0).getSecondname());
        String verifyURL = "http://localhost:3000/verifyemail?email="+email+"&otp="+otp;
        if (otpRepo.validateEmail(email)!=null){
            otpRepo.deletePreviousOtp(email);
        }
        otpRepo.saveOtp(user.get(0).getEmail(),otp);
        content = content.replace("[[URL]]",verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
        return "Email verification link sent";
    }

    @Override
    public String verifyEmail(String email, Integer otp) {
        Integer otp1=otpRepo.validateEmail(email).getOtp();
        if (otp1.equals(otp)){
            userRepo.changeActiveStatus(email);
            return "Email Verified";
        }
        return "System error";
    }

    @Override
    public String resetPasswordEmail(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        otp = otpGeneratorService.generateOtp();
        List<User> user = userRepo.findUserByEmail(email);

        String toAddress = email;
        String fromAddress = "basnayakasanjeewa3@gmail.com";
        String senderName = "Book World";
        String subject = "Please Reset Your Password";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to reset your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Reset</a></h3>"
                + "Thank you,<br>"
                + "Tech World.";
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.get(0).getFirstname()+user.get(0).getSecondname());
        String resetPasswordURL = "http://localhost:3000/changepassword?email="+email+"&otp="+otp;
        if (otpRepo.validateEmail(email)!=null){
            otpRepo.deletePreviousOtp(email);
        }
        otpRepo.saveOtp(user.get(0).getEmail(),otp);
        content = content.replace("[[URL]]",resetPasswordURL);
        helper.setText(content, true);
        mailSender.send(message);
        return "Reset Password link sent";
    }

    @Override
    public String ValidateResetLink(String email, Integer otp) {
        Integer otp1=otpRepo.validateEmail(email).getOtp();
        if (otp1.equals(otp)){
            return "Create new password";
        }
        return "System error";

    }
}
