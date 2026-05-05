package banking_app.Bankingapp.dto;

import banking_app.Bankingapp.model.AccountType;

public class UpdateBankAccountRequest {

    private String name;
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
