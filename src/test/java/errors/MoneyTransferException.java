package errors;

public class MoneyTransferException extends RuntimeException {

    public MoneyTransferException(String msg) {
        super(msg);
    }
}
