package banking_app.Bankingapp.service.components;

import org.springframework.stereotype.Component;

import banking_app.Bankingapp.exception.AccountNotFoundException;
import banking_app.Bankingapp.model.BankAccount;
import banking_app.Bankingapp.repository.BankAccountRepository;

@Component
public class BankAccountLookup {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountLookup(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount findAccountByAccountNumber(String accountNumber) {

        return bankAccountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

    }
}
