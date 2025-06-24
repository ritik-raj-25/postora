package com.postora.postora_backend.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterAndUpdateDto {

	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
	@Pattern(regexp = "^[a-zA-Z. ]+$", message = "Name must contain only letters, dots, and spaces")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email")
	private String email;

	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "^[\\d]{10}$", message = "Invalid mobile number")
	private String mobNo;

	@NotBlank(message = "Password is required")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&])[a-zA-Z\\d!@#$%^&]{8,}$", message = "Password must have at least 1 uppercase, 1 lowercase, 1 digit, 1 special char, and be 8+ characters")
	private String password;

	@NotNull(message = "Date of birth is required")
	@PastOrPresent(message = "DOB can't be future date")
	private LocalDate dob;

	@NotNull(message = "Bio cannot be null")
	private String bio;

}
