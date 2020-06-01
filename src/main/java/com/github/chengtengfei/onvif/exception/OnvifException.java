package com.github.chengtengfei.onvif.exception;

public class OnvifException extends RuntimeException {

    public OnvifException() {
    }

    public OnvifException(String message) {
        super(message);
    }

    public OnvifException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnvifException(Throwable cause) {
        super(cause);
    }
}
