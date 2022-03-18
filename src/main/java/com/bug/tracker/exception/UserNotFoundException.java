package com.bug.tracker.exception;

public class UserNotFoundException extends Exception {

  private static final long serialVersionUID = -3777748522628991534L;

  public UserNotFoundException() {
    super("No User Found.");
  }
}
