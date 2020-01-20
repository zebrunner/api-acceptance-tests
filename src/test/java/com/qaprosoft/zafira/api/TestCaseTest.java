package com.qaprosoft.zafira.api;

import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.manager.APIContextManager;
import com.qaprosoft.zafira.service.impl.TestSuiteServiceImpl;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.Test;

public class TestCaseTest extends ZariraAPIBaseTest {
    @Test
    public void testCreateTestCase() {
        int testSuiteId = new TestSuiteServiceImpl().create();

        PostTestCaseMethod postTestCaseMethod = new PostTestCaseMethod(testSuiteId);
        apiExecutor.expectStatus(postTestCaseMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postTestCaseMethod);
        apiExecutor.validateResponse(postTestCaseMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }
}
