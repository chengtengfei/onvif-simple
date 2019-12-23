package space.fei.code.onvif.exception;

public class LogException extends OnvifException {
    public LogException() {
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogException(Throwable cause) {
        super(cause);
    }
}
