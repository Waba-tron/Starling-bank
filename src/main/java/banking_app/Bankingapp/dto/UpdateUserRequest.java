package banking_app.Bankingapp.dto;

import jakarta.validation.Valid;

public class UpdateUserRequest {

    private String name;

    @Valid
    private UpdateAddressDto address;

    private String phoneNumber;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UpdateAddressDto getAddress() {
        return address;
    }

    public void setAddress(UpdateAddressDto address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
