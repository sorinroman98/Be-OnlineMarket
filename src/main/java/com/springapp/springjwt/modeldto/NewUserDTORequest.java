package com.springapp.springjwt.modeldto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
public class NewUserDTORequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private boolean isActive;
    private boolean isNotLocked;
    private MultipartFile profileImage;
}
