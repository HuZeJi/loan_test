package com.victor_jimenez.test.service;

public record PaymentRegistrationResult(Status status, String message) {

    public enum Status {
        SUCCESS,
        ALREADY_SETTLED,
        AMOUNT_EXCEEDS_PENDING
    }
}
