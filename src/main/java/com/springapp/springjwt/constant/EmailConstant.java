package com.springapp.springjwt.constant;

public class EmailConstant {
    private EmailConstant() {
    }

    public static final String SIMPLE_MAIL_TRANSPORT_PROTOCOL = "smtps";
    public static final String USERNAME = "garagesalehelper80@gmail.com";
    public static final String PASSWORD = "ytbesypxgffxdlrh";
    public static final String FROM_EMAIL = "garagesalehelper80@gmail.com\n";
    public static final String CC_EMAIL = "";
    public static final String EMAIL_SUBJECT = "Get Arrays, LLC - New Password";
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final int DEFAULT_PORT = 465;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    public static final String CONFIRMATION_URL = "http://localhost:4200/verify?token=";
    public static final String INVALID_EMAIL_FORMAT = "The format of email is invalid";
}
