package banking_app.Bankingapp.controller;

import banking_app.Bankingapp.dto.BankAccountResponse;
import banking_app.Bankingapp.dto.CreateBankAccountRequest;
import banking_app.Bankingapp.dto.ListBankAccountsResponse;
import banking_app.Bankingapp.dto.UpdateBankAccountRequest;
import banking_app.Bankingapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<BankAccountResponse> createAccount(
            @Valid @RequestBody CreateBankAccountRequest request,
            @AuthenticationPrincipal String userId) {

        BankAccountResponse response = accountService.createAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    public ResponseEntity<ListBankAccountsResponse> listAccounts(@AuthenticationPrincipal String userId) {

        ListBankAccountsResponse response = accountService.listAccounts(userId);
        return ResponseEntity.ok(response);
        
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> fetchAccount(@PathVariable String accountNumber,
            @AuthenticationPrincipal String userId) {

        BankAccountResponse response = accountService.fetchAccount(accountNumber, userId);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> updateAccount(@PathVariable String accountNumber,
            @Valid @RequestBody UpdateBankAccountRequest request,
            @AuthenticationPrincipal String userId) {

        BankAccountResponse response = accountService.updateAccount(accountNumber, userId, request);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber,
            @AuthenticationPrincipal String userId) {

        accountService.deleteAccount(accountNumber, userId);
        return ResponseEntity.noContent().build();

    }
}
