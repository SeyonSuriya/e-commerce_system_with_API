package com.ecommercesystem.registeruser.UserController;

import com.ecommercesystem.checkout.CheckoutDtos.AddressDto;
import com.ecommercesystem.registeruser.Email.Email;
import com.ecommercesystem.registeruser.LoginUser.LoginUser;
import com.ecommercesystem.registeruser.dto.PasswordResetDto;
import com.ecommercesystem.registeruser.dto.VerificationDto;
import com.ecommercesystem.registeruser.entity.User;
import com.ecommercesystem.registeruser.service.AccountStatusService.AccountStatusService;
import com.ecommercesystem.registeruser.service.AddressService.AddressService;
import com.ecommercesystem.registeruser.service.ResetPassword.ResetPassword;
import com.ecommercesystem.registeruser.dto.UserDto;
import com.ecommercesystem.registeruser.service.EmailService.EmailService;
import com.ecommercesystem.registeruser.service.OtpGenerator.OtpGeneratorService;
import com.ecommercesystem.registeruser.service.UserServices.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.* ;
import java.io.UnsupportedEncodingException;
import java.util.List;


@RestController
@CrossOrigin()
@RequestMapping("ecommerce")
public class RegisterUserController {
    @Autowired
    private UserService registeruser;
    @Autowired
    private AddressService addressService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpGeneratorService otpGeneratorService;
    @Autowired
    private ResetPassword resetPassword;
    @Autowired
    private AccountStatusService accountStatusService;

    @PostMapping(path = "/register")
    public String saveUser(@RequestBody UserDto userDto) throws MessagingException, UnsupportedEncodingException {
        return registeruser.addUser(userDto);
    }

    @PostMapping(path = "/login")
    public String authenticateUser(@RequestBody LoginUser loginUser)
    {
        String result = registeruser.validateUserDetails(loginUser.email,loginUser.password);
        return result;
    }

    @PostMapping(path = "/validateemail/verify")
    public String validateWithOtp(@RequestBody VerificationDto verificationDto) throws MessagingException {
        return emailService.verifyEmail(verificationDto.email,verificationDto.otp);
    }
    @GetMapping(path = "/resendverificationlink")
    public String validateWithOtp(@RequestBody Email email) throws MessagingException, UnsupportedEncodingException {
        return emailService.sendEmailVerification(email.getEmailAddress());
    }
    @PostMapping(path = "/resetpassword")
    public String resetPassword(@RequestBody Email email) throws MessagingException, UnsupportedEncodingException {
        return emailService.resetPasswordEmail(email.getEmailAddress());
    }
    @PostMapping(path = "/changepassword")
    public String changepassword(@RequestBody VerificationDto verificationDto) throws MessagingException, UnsupportedEncodingException {
        return emailService.ValidateResetLink(verificationDto.email,verificationDto.otp);
    }
    @PostMapping(path = "addnewpassword")
    public String addNewPassword(@RequestBody PasswordResetDto passwordResetDto){
        return resetPassword.addNewPassword(passwordResetDto);
    }

    @PostMapping(path = "/deactivateAccount")
    public String deactivateAccount(String email) {
        accountStatusService.deactivateAccount(email);
        return "Account is Deactivated ";
    }
    @PostMapping(path = "/getaddress")
    public List<User> getAddress(@RequestParam Integer userid) {
        return addressService.getAddressBYId(userid);
    }
    @PostMapping(path = "/changeaddress")
    public void getAddress(@RequestParam AddressDto addressDto, Integer userid) {
       addressService.changeAddress(addressDto, userid);
    }

}
