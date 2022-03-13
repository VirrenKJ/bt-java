package com.bug.tracker.exception;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorMessage implements Serializable {

  private static final long serialVersionUID = -662898084152740675L;

  private int status;
  private String code;
  private String message;
  private String document;

}
