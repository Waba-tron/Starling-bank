package banking_app.Bankingapp.repository;

import banking_app.Bankingapp.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import banking_app.Bankingapp.model.User;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findByUser(User user);
    boolean existsByUser(User user);
}
