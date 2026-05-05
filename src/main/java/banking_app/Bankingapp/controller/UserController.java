package banking_app.Bankingapp.controller;

import banking_app.Bankingapp.dto.CreateUserRequest;
import banking_app.Bankingapp.dto.UpdateUserRequest;
import banking_app.Bankingapp.dto.UserResponse;
import banking_app.Bankingapp.exception.ForbiddenException;
import banking_app.Bankingapp.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> fetchUser(@PathVariable String userId,
            @AuthenticationPrincipal String authenticatedUserId) {

        UserResponse response = userService.fetchUser(userId);

        if (!authenticatedUserId.equals(userId)) {
            throw new ForbiddenException();
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal String authenticatedUserId) {

        userService.fetchUser(userId);

        if (!authenticatedUserId.equals(userId)) {
            throw new ForbiddenException();
        }

        UserResponse response = userService.updateUser(userId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId,
            @AuthenticationPrincipal String authenticatedUserId) {

        if (!authenticatedUserId.equals(userId)) {
            throw new ForbiddenException();
        }

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
