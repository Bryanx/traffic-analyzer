package be.kdg.processor.user.web;

public class UserNotFoundException extends Throwable {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
