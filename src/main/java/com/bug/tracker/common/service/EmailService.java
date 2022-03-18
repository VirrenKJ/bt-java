package com.bug.tracker.common.service;

public interface EmailService {

  void sendSimpleMessage(String to, String subject, String text);
}
