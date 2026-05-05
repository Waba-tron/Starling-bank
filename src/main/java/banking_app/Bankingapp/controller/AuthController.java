package banking_app.Bankingapp.controller;

import banking_app.Bankingapp.dto.LoginRequest;
import banking_app.Bankingapp.dto.LoginResponse;
import banking_app.Bankingapp.service.JwtService;
import banking_app.Bankingapp.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        String userId = userService.authenticate(request.getEmail(), request.getPassword());
        String token = jwtService.generateToken(userId);
        return ResponseEntity.ok(new LoginResponse(token));

    }
}
