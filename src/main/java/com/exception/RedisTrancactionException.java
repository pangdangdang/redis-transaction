package com.exception;

/**
 *
 * @author tingmailang
 */
public class RedisTrancactionException extends RuntimeException {
  public RedisTrancactionException() {
    super();
  }

  public RedisTrancactionException(String message) {
    super("RedisTrancactionException:" + message);
  }

  public RedisTrancactionException(String message, Throwable cause) {
    super("RedisTrancactionException:" + message, cause);
  }

  public RedisTrancactionException(Throwable cause) {
    super(cause);
  }

}
