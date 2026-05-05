package banking_app.Bankingapp.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("You are not allowed to access this resource");
    }
}
