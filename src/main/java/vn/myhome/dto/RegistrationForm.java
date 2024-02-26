package vn.myhome.dto;

import org.springframework.format.annotation.DateTimeFormat;
import vn.myhome.validation.annotations.FieldMatch;

import javax.validation.constraints.*;
import java.util.Date;
@FieldMatch.List({
@FieldMatch(first = "password", second = "confirmPassword", message = "Passwords must match")})
public class RegistrationForm {
    @NotBlank(message = "Fill in required !")
    @Email(message = " Invalid Email !")
    private String email;

    @NotBlank(message = "Fill in required !")
    @Size(min = 6,max = 30,message = "Password must consist of 6-30 characters!")
    public String password;

    @NotBlank(message = "Fill in required !")
    public String confirmPassword;

    @NotEmpty(message = "Fill in required !")
    private String firstName;

    @NotEmpty(message = "Fill in required !")
    private String lastName;

    @NotNull(message = "Fill in required !")
    @Digits(integer = 15 , fraction = 0,message = "Invalid Phone Number !")
    private Integer phoneNumber;

    @NotEmpty(message = "Fill in required !")
    private String address;

    @NotNull(message = "Fill in required !")
    @Past(message = "Birthdate not valid")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private boolean enabled;
    public RegistrationForm() {
    }

    public RegistrationForm(@NotBlank(message = "Fill in required !") @Email(message = "Invalid Email !") String email, @NotBlank(message = "Fill in required !") @Size(min = 6, max = 30, message = "Password must consist of 6-30 characters!") String password, @NotBlank(message = "Fill in required !") String confirmPassword, @NotEmpty(message = "Fill in required !") @Digits(integer = 15, fraction = 0, message = "Invalid Phone Number !") Integer phoneNumber, @NotEmpty(message = "Fill in required !") String address, @NotEmpty(message = "Fill in required !") @Past(message = "Invalid Date of Birth !") Date dateOfBirth) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public RegistrationForm(@NotBlank(message = "Fill in required !") @Email(message = "Invalid Email !") String email, @NotBlank(message = "Fill in required !") @Size(min = 6, max = 30, message = "Password must consist of 6-30 characters!") String password, @NotBlank(message = "Fill in required !") String confirmPassword, @NotEmpty(message = "Fill in required !") String firstName, @NotEmpty(message = "Fill in required !") String lastName, @NotNull(message = "Fill in required !") @Digits(integer = 15, fraction = 0, message = "Invalid Phone Number !") Integer phoneNumber, @NotEmpty(message = "Fill in required !") String address, @NotNull(message = "Fill in required!") @Past(message = "Birthdate not valid!") Date dateOfBirth) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
