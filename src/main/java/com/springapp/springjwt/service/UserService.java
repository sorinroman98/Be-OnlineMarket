package com.springapp.springjwt.service;


import com.springapp.springjwt.domain.User;
import com.springapp.springjwt.exception.domain.EmailExistException;
import com.springapp.springjwt.exception.domain.UserNotFoundException;
import com.springapp.springjwt.exception.domain.UsernameExistException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;

    List<User> getUsers();

    User findByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage);

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isNonLocked, boolean isActive, MultipartFile profileImage);

    void deleteUser(long id);

    void resetPassword(String email);

    User updateProfileImage(String username, MultipartFile profileImage);
}
