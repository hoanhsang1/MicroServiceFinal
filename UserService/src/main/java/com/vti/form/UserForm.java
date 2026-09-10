package com.vti.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor 
public class UserForm {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
}