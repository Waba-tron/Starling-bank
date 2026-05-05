package banking_app.Bankingapp.repository;

import banking_app.Bankingapp.model.BankAccount;
import banking_app.Bankingapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByBankAccount(BankAccount bankAccount);
}
