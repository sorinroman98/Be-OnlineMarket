package com.springapp.springjwt.service.impl;

import com.springapp.springjwt.domain.User;
import com.springapp.springjwt.domain.UserPrincipal;
import com.springapp.springjwt.enumeration.Role;
import com.springapp.springjwt.exception.domain.EmailExistException;
import com.springapp.springjwt.exception.domain.UserNotFoundException;
import com.springapp.springjwt.exception.domain.UsernameExistException;
import com.springapp.springjwt.repository.UserRepository;
import com.springapp.springjwt.service.EmailService;
import com.springapp.springjwt.service.LoginAttemptService;
import com.springapp.springjwt.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.springapp.springjwt.constant.UserImplConstant.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;
    private EmailService emailService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           LoginAttemptService loginAttemptService,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }



    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null){
            logger.error(NO_USER_FOUND_BY_USERNAME, username);
            throw new UsernameNotFoundException("User not found by username");
        } else {
          validateLoginAttempt(user);
          user.setLastLoginDateDisplay(user.getLastLoginDate());
          user.setLastLoginDate(new Date());
          userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            logger.info(RETURNING_FOUND_USER_BY_USERNAME, username);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user)  {
        if (user.isNotLocked()){

                user.setNotLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        }else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setPassword(encodedPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImage());
        userRepository.save(user);
        emailService.sendNewPasswordEmail(firstName,password,email);
        return user;
    }

    private String encodePassword(String password) {

        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String getTemporaryProfileImage() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH).toUriString();
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername,String newUsername, String newEmail)
    throws UserNotFoundException, UsernameExistException, EmailExistException  {

        User newUserByUsername = findByUsername(newUsername);
        User newUserByEmail = findUserByEmail(newEmail);

        if (StringUtils.isNotBlank(currentUsername)){
            User currentUser = findByUsername(currentUsername);
            if (currentUser == null ){
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (newUserByUsername != null && !currentUser.getId().equals(newUserByUsername.getId())){
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }

            if (newUserByEmail != null && !currentUser.getId().equals(newUserByEmail.getId())){
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }

            return currentUser;
        } else {
            if (newUserByUsername != null){
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }

            if (newUserByEmail != null){
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) {
        return null;
    }

    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isNonLocked, boolean isActive, MultipartFile profileImage) {
        return null;
    }

    @Override
    public void deleteUser(long id) {

    }

    @Override
    public void resetPassword(String email) {

    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) {
        return null;
    }
}
