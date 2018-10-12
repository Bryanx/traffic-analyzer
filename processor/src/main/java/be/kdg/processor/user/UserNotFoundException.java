package be.kdg.processor.user;

public class UserNotFoundException extends Throwable {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
