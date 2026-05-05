package banking_app.Bankingapp.dto;

import java.util.List;

public class ListBankAccountsResponse {

    private List<BankAccountResponse> accounts;

    public ListBankAccountsResponse(List<BankAccountResponse> accounts) {
        this.accounts = accounts;
    }

    public List<BankAccountResponse> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccountResponse> accounts) {
        this.accounts = accounts;
    }
}
