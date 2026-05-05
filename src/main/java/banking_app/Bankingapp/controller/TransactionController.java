package banking_app.Bankingapp.controller;

import banking_app.Bankingapp.dto.CreateTransactionRequest;
import banking_app.Bankingapp.dto.ListTransactionsResponse;
import banking_app.Bankingapp.dto.TransactionResponse;
import banking_app.Bankingapp.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable String accountNumber,
            @Valid @RequestBody CreateTransactionRequest request,
            @AuthenticationPrincipal String userId) {

        TransactionResponse response = transactionService.createTransaction(accountNumber, userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ListTransactionsResponse> listTransactions(@PathVariable String accountNumber,
            @AuthenticationPrincipal String userId) {

        ListTransactionsResponse response = transactionService.listTransactions(accountNumber, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> fetchTransaction(@PathVariable String accountNumber,
            @PathVariable String transactionId,
            @AuthenticationPrincipal String userId) {

        TransactionResponse response = transactionService.fetchTransaction(accountNumber, transactionId, userId);

        return ResponseEntity.ok(response);
    }
}
