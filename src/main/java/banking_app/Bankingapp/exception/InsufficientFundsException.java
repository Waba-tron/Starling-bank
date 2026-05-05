package banking_app.Bankingapp.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("Insufficient funds to process transaction");
    }
}
