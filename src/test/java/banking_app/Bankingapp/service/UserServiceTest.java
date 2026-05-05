package banking_app.Bankingapp.service;

import banking_app.Bankingapp.dto.AddressDto;
import banking_app.Bankingapp.dto.CreateUserRequest;
import banking_app.Bankingapp.dto.UserResponse;
import banking_app.Bankingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest buildRequest() {
        AddressDto address = new AddressDto();
        address.setLine1("1 Main Street");
        address.setTown("London");
        address.setCounty("Greater London");
        address.setPostcode("SW1A 1AA");

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setPassword("secret123");
        request.setPhoneNumber("+447700900123");
        request.setAddress(address);
        return request;
    }

    @Test
    void createUser_returnsUserResponseWithCorrectFields() {

        UserResponse response = userService.createUser(buildRequest());

        assertThat(response.getName()).isEqualTo("Jane Doe");
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(response.getPhoneNumber()).isEqualTo("+447700900123");
        assertThat(response.getAddress().getLine1()).isEqualTo("1 Main Street");
        assertThat(response.getAddress().getTown()).isEqualTo("London");
        assertThat(response.getAddress().getPostcode()).isEqualTo("SW1A 1AA");
    }

   
}
