package com.springapp.springjwt.controller;

import com.springapp.springjwt.exception.domain.ActivationTokenException;
import com.springapp.springjwt.modeldto.ApiResponse;
import com.springapp.springjwt.utility.VerificationTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/activationToken")
public class ActivationTokenController {
    private VerificationTokenProvider verificationTokenProvider;

    @Autowired
    public ActivationTokenController(VerificationTokenProvider verificationTokenProvider) {
        this.verificationTokenProvider = verificationTokenProvider;
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> confirmRegistration(@RequestBody String token) throws ActivationTokenException {
        final String result = verificationTokenProvider.validateVerificationToken(token);
        return ResponseEntity.ok().body(new ApiResponse(true, result));
    }

    @PostMapping("/resend")
    @ResponseBody
    public ResponseEntity<ApiResponse> resendRegistrationToken(@RequestBody String expiredToken) throws MessagingException {
        if (!verificationTokenProvider.resendVerificationToken(expiredToken)) {
            return new ResponseEntity<>(new ApiResponse(false, "Token not found!"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(new ApiResponse(true, HttpStatus.OK.toString()));    }

}
