package com.vti.form;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor 
public class UserFormUpdate {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Pattern(regexp = "^\\+?[0-9]\\d{0,15}$", message = "Invalid phone number format")
    @Size (min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phone;
}