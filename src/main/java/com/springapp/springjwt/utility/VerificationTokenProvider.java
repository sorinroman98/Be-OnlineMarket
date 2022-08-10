package com.springapp.springjwt.utility;

import com.springapp.springjwt.domain.User;
import com.springapp.springjwt.domain.VerificationToken;
import com.springapp.springjwt.exception.domain.ActivationTokenException;
import com.springapp.springjwt.repository.UserRepository;
import com.springapp.springjwt.repository.VerificationTokenRepository;
import com.springapp.springjwt.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import static com.springapp.springjwt.constant.ActivationTokenConstant.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VerificationTokenProvider {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public void deleteUnconfirmedAccountWithExpiredToken(){
        List<User> users = userRepository.getAllInactiveAccount();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        for (User user : users){
            if(verificationTokenRepository.findByUser(user).getExpiryDate().getTime() < cal.getTime().getTime()){
                verificationTokenRepository.delete(verificationTokenRepository.findByUser(user));
                userRepository.delete(user);
            }
        }
    }

    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Transactional
    public boolean resendVerificationToken(final String existingVerificationToken) throws MessagingException {
        VerificationToken vToken = verificationTokenRepository.findByToken(existingVerificationToken);
        if(vToken != null) {
            vToken.updateToken(UUID.randomUUID().toString());
            vToken = verificationTokenRepository.save(vToken);
            emailService.sendNewActivationTokenEmail(vToken.getUser().getFirstName(),vToken.getToken(), vToken.getUser().getEmail());
            return true;
        }
        return false;
    }

    public String validateVerificationToken(String token) throws ActivationTokenException {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new ActivationTokenException(INVALID_TOKEN);
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new ActivationTokenException(EXPIRED_TOKEN);
        }

        user.setActive(true);
        verificationTokenRepository.delete(verificationToken);
        userRepository.save(user);
        return VALID_TOKEN;
    }

}
