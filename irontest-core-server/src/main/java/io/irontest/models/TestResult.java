package io.irontest.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Result for running a test step or test case, or for verifying an assertion.
 * Created by Zheng on 24/07/2016.
 */
public enum TestResult {
    PASSED("Passed"), FAILED("Failed");

    private final String text;

    private TestResult(String text) {
        this.text = text;
    }

    @Override
    @JsonValue
    public String toString() {
        return text;
    }

    public static TestResult getByText(String text) {
        for (TestResult e : values()) {
            if (e.text.equals(text)) {
                return e;
            }
        }
        return null;
    }
}