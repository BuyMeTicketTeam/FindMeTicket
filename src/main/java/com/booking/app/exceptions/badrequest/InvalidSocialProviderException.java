package com.booking.app.exceptions.badrequest;

import com.booking.app.constants.ApiMessagesConstants;

public class InvalidSocialProviderException extends RuntimeException {
    public InvalidSocialProviderException() {
        super(ApiMessagesConstants.NO_SOCIAL_IDENTITY_PROVIDERS_MESSAGE);
    }
}
