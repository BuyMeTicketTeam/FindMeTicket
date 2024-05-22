package com.booking.app.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResetPasswordConstantMessages {
    public static final String MESSAGE_CODE_HAS_BEEN_SENT = "Code is sent";
    public static final String MESSAGE_WRONG_CONFIRMATION_CODE_IS_PROVIDED = "Wrong confirmation code is provided";
    public static final String MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET = "Password has been successfully reset";
    public static final String MESSAGE_NEW_PASSWORD_HAS_BEEN_CREATED = "New password has been created";
    public static final String MESSAGE_NEW_PASSWORDS_DO_NOT_MATCH = "New passwords do not match";
    public static final String THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED = "The specified email is not registered or the account is disabled";
}