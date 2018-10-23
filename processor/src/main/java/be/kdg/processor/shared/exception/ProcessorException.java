package be.kdg.processor.shared.exception;

public class ProcessorException extends Throwable {

    public ProcessorException(String message) {
        super(message);
    }

    public ProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessorException(Throwable cause) {
        super(cause);
    }
}
