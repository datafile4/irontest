package io.irontest.core.assertion;

import io.irontest.models.TestResult;
import io.irontest.models.assertion.Assertion;
import io.irontest.models.assertion.AssertionVerificationResult;
import io.irontest.models.assertion.IntegerEqualAssertionProperties;

/**
 * Created by Zheng on 4/06/2016.
 */
public class IntegerEqualAssertionVerifier implements AssertionVerifier {
    /**
     *
     * @param assertion
     * @param input the Integer that the assertion is verified against
     * @return
     */
    public AssertionVerificationResult verify(Assertion assertion, Object input) {
        AssertionVerificationResult result = new AssertionVerificationResult();
        IntegerEqualAssertionProperties properties = (IntegerEqualAssertionProperties)
                assertion.getOtherProperties();
        result.setResult(new Integer(properties.getNumber()).equals(input) ? TestResult.PASSED : TestResult.FAILED);

        return result;
    }
}
