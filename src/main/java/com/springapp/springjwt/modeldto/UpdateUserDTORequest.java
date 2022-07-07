package com.springapp.springjwt.modeldto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateUserDTORequest {
    private String currentUsername;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private boolean isActive;
    private boolean isNotLocked;
    private MultipartFile profileImage;
}
