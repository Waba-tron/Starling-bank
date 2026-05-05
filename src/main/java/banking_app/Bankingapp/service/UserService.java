package banking_app.Bankingapp.service;

import banking_app.Bankingapp.dto.AddressDto;
import banking_app.Bankingapp.dto.CreateUserRequest;
import banking_app.Bankingapp.dto.UpdateUserRequest;
import banking_app.Bankingapp.dto.UserResponse;
import banking_app.Bankingapp.exception.InvalidCredentialsException;
import banking_app.Bankingapp.exception.UserHasAccountsException;
import banking_app.Bankingapp.model.Address;
import banking_app.Bankingapp.model.User;
import banking_app.Bankingapp.repository.BankAccountRepository;
import banking_app.Bankingapp.repository.UserRepository;
import banking_app.Bankingapp.service.components.UserLookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserLookup userLookup;

    public UserService(UserRepository userRepository,
                       BankAccountRepository bankAccountRepository,
                       PasswordEncoder passwordEncoder,
                       UserLookup userLookup) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.userLookup = userLookup;
    }

    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with email={}", request.getEmail());
        String id = "usr-" + UUID.randomUUID().toString().replace("-", "");
        LocalDateTime now = LocalDateTime.now();

        Address address = new Address();
        address.setLine1(request.getAddress().getLine1());
        address.setLine2(request.getAddress().getLine2());
        address.setLine3(request.getAddress().getLine3());
        address.setTown(request.getAddress().getTown());
        address.setCounty(request.getAddress().getCounty());
        address.setPostcode(request.getAddress().getPostcode());

        User user = new User();
        user.setId(id);
        user.setName(request.getName());
        user.setAddress(address);
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedTimestamp(now);
        user.setUpdatedTimestamp(now);

        userRepository.save(user);

        log.info("User created: userId={}", user.getId());
        return toResponse(user);
    }

    public UserResponse fetchUser(String userId) {
        log.info("Fetching user: userId={}", userId);
        User user = userLookup.findById(userId);
        return toResponse(user);
    }

    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        log.info("Updating user: userId={}", userId);
        User user = userLookup.findById(userId);

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        if (request.getAddress() != null) {
            Address address = user.getAddress();
            if (address == null) {
                address = new Address();
                user.setAddress(address);
            }
            if (request.getAddress().getLine1() != null) address.setLine1(request.getAddress().getLine1());
            if (request.getAddress().getLine2() != null) address.setLine2(request.getAddress().getLine2());
            if (request.getAddress().getLine3() != null) address.setLine3(request.getAddress().getLine3());
            if (request.getAddress().getTown() != null) address.setTown(request.getAddress().getTown());
            if (request.getAddress().getCounty() != null) address.setCounty(request.getAddress().getCounty());
            if (request.getAddress().getPostcode() != null) address.setPostcode(request.getAddress().getPostcode());
        }

        user.setUpdatedTimestamp(LocalDateTime.now());
        userRepository.save(user);
        log.info("User updated: userId={}", userId);
        return toResponse(user);
    }

    public void deleteUser(String userId) {
        log.info("Deleting user: userId={}", userId);
        User user = userLookup.findById(userId);

        if (bankAccountRepository.existsByUser(user)) {
            throw new UserHasAccountsException();
        }

        userRepository.delete(user);
        log.info("User deleted: userId={}", userId);
    }

    public String authenticate(String email, String password) {

        log.info("Authentication attempt: email={}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        
        log.info("Authentication successful: userId={}", user.getId());
        return user.getId();
    }

    private UserResponse toResponse(User user) {
        AddressDto addressDto = new AddressDto();
        Address address = user.getAddress();
        if (address != null) {
            addressDto.setLine1(address.getLine1());
            addressDto.setLine2(address.getLine2());
            addressDto.setLine3(address.getLine3());
            addressDto.setTown(address.getTown());
            addressDto.setCounty(address.getCounty());
            addressDto.setPostcode(address.getPostcode());
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setAddress(addressDto);
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        response.setCreatedTimestamp(user.getCreatedTimestamp());
        response.setUpdatedTimestamp(user.getUpdatedTimestamp());
        return response;
    }
}
