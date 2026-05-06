package banking_app.Bankingapp.service;

import banking_app.Bankingapp.dto.CreateTransactionRequest;
import banking_app.Bankingapp.dto.ListTransactionsResponse;
import banking_app.Bankingapp.dto.TransactionResponse;
import banking_app.Bankingapp.exception.ForbiddenException;
import banking_app.Bankingapp.exception.InsufficientFundsException;
import banking_app.Bankingapp.exception.TransactionNotFoundException;
import banking_app.Bankingapp.model.BankAccount;
import banking_app.Bankingapp.model.Transaction;
import banking_app.Bankingapp.model.TransactionType;
import banking_app.Bankingapp.repository.BankAccountRepository;
import banking_app.Bankingapp.repository.TransactionRepository;
import banking_app.Bankingapp.service.components.BankAccountLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountLookup bankAccountLookup;

    public TransactionService(TransactionRepository transactionRepository,
            BankAccountRepository bankAccountRepository,
            BankAccountLookup bankAccountLookup) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountLookup = bankAccountLookup;
    }

    @Transactional
    public TransactionResponse createTransaction(String accountNumber, String userId,
            CreateTransactionRequest request) {
        log.info("Creating transaction: accountNumber={} userId={} type={} amount={}", accountNumber, userId,
                request.getType(), request.getAmount());
        BankAccount account = bankAccountLookup.findAccountByAccountNumber(accountNumber);

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        if (request.getType() == TransactionType.WITHDRAWAL
                && account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException();
        }

        if (request.getType() == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(request.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(request.getAmount()));
        }

        account.setUpdatedTimestamp(LocalDateTime.now());
        bankAccountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setId("tan-" + UUID.randomUUID().toString().replace("-", ""));
        transaction.setBankAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setType(request.getType());
        transaction.setReference(request.getReference());
        transaction.setCreatedTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
        log.info("Transaction created: transactionId={} accountNumber={} type={} amount={}", transaction.getId(),
                accountNumber, request.getType(), request.getAmount());

        return toResponse(transaction);
    }

    public TransactionResponse fetchTransaction(String accountNumber, String transactionId, String userId) {
        log.info("Fetching transaction: transactionId={} accountNumber={} userId={}", transactionId, accountNumber,
                userId);
        BankAccount account = bankAccountLookup.findAccountByAccountNumber(accountNumber);

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        if (!transaction.getBankAccount().getAccountNumber().equals(accountNumber)) {
            throw new TransactionNotFoundException(transactionId);
        }

        return toResponse(transaction);
    }

    public ListTransactionsResponse listTransactions(String accountNumber, String userId) {
        log.info("Listing transactions: accountNumber={} userId={}", accountNumber, userId);
        BankAccount account = bankAccountLookup.findAccountByAccountNumber(accountNumber);

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        List<TransactionResponse> transactions = transactionRepository.findByBankAccount(account)
                .stream()
                .map(this::toResponse)
                .toList();
                
        return new ListTransactionsResponse(transactions);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency());
        response.setType(transaction.getType());
        response.setReference(transaction.getReference());
        response.setUserId(transaction.getBankAccount().getUser().getId());
        response.setCreatedTimestamp(transaction.getCreatedTimestamp());
        return response;
    }
}
