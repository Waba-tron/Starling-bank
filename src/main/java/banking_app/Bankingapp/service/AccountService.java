package banking_app.Bankingapp.service;

import banking_app.Bankingapp.dto.BankAccountResponse;
import banking_app.Bankingapp.dto.CreateBankAccountRequest;
import banking_app.Bankingapp.dto.ListBankAccountsResponse;
import banking_app.Bankingapp.dto.UpdateBankAccountRequest;
import banking_app.Bankingapp.exception.AccountNotFoundException;
import banking_app.Bankingapp.exception.ForbiddenException;
import banking_app.Bankingapp.model.BankAccount;
import banking_app.Bankingapp.model.User;
import java.math.BigDecimal;
import banking_app.Bankingapp.repository.BankAccountRepository;
import banking_app.Bankingapp.service.components.BankAccountLookup;
import banking_app.Bankingapp.service.components.UserLookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private static final String SORT_CODE = "10-10-10";
    private static final String CURRENCY = "GBP";

    private final BankAccountRepository bankAccountRepository;
    private final UserLookup userLookup;
    private final BankAccountLookup accountLookup;

    public AccountService(BankAccountRepository bankAccountRepository,
            UserLookup userLookup,
            BankAccountLookup accountLookup) {
        this.bankAccountRepository = bankAccountRepository;
        this.userLookup = userLookup;
        this.accountLookup = accountLookup;
    }

    public BankAccountResponse createAccount(String userId, CreateBankAccountRequest request) {

        log.info("Creating bank account for userId={}", userId);
        User user = userLookup.findById(userId);

        LocalDateTime now = LocalDateTime.now();

        BankAccount account = new BankAccount();
        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        account.setSortCode(SORT_CODE);
        account.setName(request.getName());
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(CURRENCY);
        account.setCreatedTimestamp(now);
        account.setUpdatedTimestamp(now);

        bankAccountRepository.save(account);

        log.info("Bank account created: accountNumber={} userId={}", account.getAccountNumber(), userId);
        return toResponse(account);

    }

    public BankAccountResponse fetchAccount(String accountNumber, String userId) {

        log.info("Fetching account: accountNumber={} userId={}", accountNumber, userId);
        BankAccount account = accountLookup.findAccountByAccountNumber(accountNumber);

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        return toResponse(account);

    }

    public BankAccountResponse updateAccount(String accountNumber, String userId, UpdateBankAccountRequest request) {

        log.info("Updating account: accountNumber={} userId={}", accountNumber, userId);
        BankAccount account = accountLookup.findAccountByAccountNumber(accountNumber);

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        if (request.getName() != null)
            account.setName(request.getName());
        if (request.getAccountType() != null)
            account.setAccountType(request.getAccountType());

        account.setUpdatedTimestamp(LocalDateTime.now());
        bankAccountRepository.save(account);
        log.info("Account updated: accountNumber={} userId={}", accountNumber, userId);

        return toResponse(account);

    }

    public void deleteAccount(String accountNumber, String userId) {

        log.info("Deleting account: accountNumber={} userId={}", accountNumber, userId);
        BankAccount account = accountLookup.findAccountByAccountNumber(accountNumber);

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        bankAccountRepository.delete(account);
        log.info("Account deleted: accountNumber={} userId={}", accountNumber, userId);

    }

    public ListBankAccountsResponse listAccounts(String userId) {

        log.info("Listing accounts: userId={}", userId);
        User user = userLookup.findById(userId);

        List<BankAccountResponse> accounts = bankAccountRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();

        return new ListBankAccountsResponse(accounts);
    }

    private String generateAccountNumber() {
        Random random = new Random();
        int suffix = random.nextInt(1_000_000);
        String accountNumber = String.format("01%06d", suffix);
        // Ensure uniqueness - regenerate if collision
        while (bankAccountRepository.existsById(accountNumber)) {
            suffix = random.nextInt(1_000_000);
            accountNumber = String.format("01%06d", suffix);
        }
        return accountNumber;
    }

    private BankAccountResponse toResponse(BankAccount account) {
        BankAccountResponse response = new BankAccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setSortCode(account.getSortCode());
        response.setName(account.getName());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setCreatedTimestamp(account.getCreatedTimestamp());
        response.setUpdatedTimestamp(account.getUpdatedTimestamp());
        return response;
    }
}
