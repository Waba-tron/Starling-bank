package banking_app.Bankingapp.exception;

public class UserHasAccountsException extends RuntimeException {

    public UserHasAccountsException() {
        super("A user cannot be deleted when they are associated with a bank account");
    }
}
