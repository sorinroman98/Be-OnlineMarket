package com.springapp.springjwt.modeldto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDTOLoginRequest {
    String username;
    String password;

}
