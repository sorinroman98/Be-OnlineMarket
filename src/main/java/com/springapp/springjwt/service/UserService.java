package com.springapp.springjwt.service;


import com.springapp.springjwt.domain.User;
import com.springapp.springjwt.exception.domain.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;

    List<User> getUsers();

    User findByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException;

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException;

    void deleteUser(String username) throws IOException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException;
}
