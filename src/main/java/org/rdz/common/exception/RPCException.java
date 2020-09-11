package org.rdz.common.exception;

/**
 * 自定义异常
 */
public class RPCException extends RuntimeException {
    public RPCException() {}

    public RPCException(String message) { super(message); }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCException(Throwable cause) {
        super(cause);
    }
}
