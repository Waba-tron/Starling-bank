package banking_app.Bankingapp.dto;

import banking_app.Bankingapp.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateBankAccountRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "accountType is required")
    private AccountType accountType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
