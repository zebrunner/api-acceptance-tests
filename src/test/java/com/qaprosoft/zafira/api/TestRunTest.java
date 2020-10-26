package com.qaprosoft.zafira.api;

import java.util.Date;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.zafira.api.testRunController.v1.*;
import com.qaprosoft.zafira.service.TestServiceAPIV1;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Now;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.jayway.restassured.path.json.JsonPath;
import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.testRunController.*;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.constant.JSONConstant;
import com.qaprosoft.zafira.domain.EmailMsg;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.manager.APIContextManager;
import com.qaprosoft.zafira.manager.EmailManager;
import com.qaprosoft.zafira.service.impl.*;
import com.qaprosoft.zafira.util.CryptoUtil;

/**
 * Test Run Controller
 */

public class TestRunTest extends ZafiraAPIBaseTest {

    private final EmailManager EMAIL = new EmailManager(
            CryptoUtil.decrypt(R.TESTDATA.get(ConfigConstant.GMAIL_USERNAME_KEY)),
            CryptoUtil.decrypt(R.TESTDATA.get(ConfigConstant.GMAIL_PASSWORD_KEY)));
    private static final String RESULT_SKIPPED = "SKIPPED";
    private static final String RESULT_FAILED = "FAILED";
    private static final String RESULT_PASSED = "PASSED";
    private static final String RESULT_IN_PROGRESS = "IN_PROGRESS";
    private final static Logger LOGGER = Logger.getLogger(TestRunTest.class);
    private final int TESTS_TO_ADD = 1;

    @Test
    public void testStartTestRun() {
        int testSuiteId = new TestSuiteServiceImpl().create();
        int jobId = new JobServiceImpl().create();
        PostStartTestRunMethod postStartTestRunMethod = new PostStartTestRunMethod(testSuiteId, jobId);
        apiExecutor.expectStatus(postStartTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postStartTestRunMethod);
        apiExecutor.validateResponse(postStartTestRunMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testUpdateTestRun() {
        int testSuiteId = new TestSuiteServiceImpl().create();
        int jobId = new JobServiceImpl().create();
        int testRunId = new TestRunServiceAPIImpl().create(testSuiteId, jobId);
        String expectedWorkItemValue = R.TESTDATA.get(ConfigConstant.WORK_ITEM_KEY);
        PutTestRunMethod putTestRunMethod = new PutTestRunMethod(testSuiteId, jobId, testRunId, expectedWorkItemValue);
        apiExecutor.expectStatus(putTestRunMethod, HTTPStatusCodeType.OK);
        String putTestRunRs = apiExecutor.callApiMethod(putTestRunMethod);
        apiExecutor.validateResponse(putTestRunMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        String workItem = JsonPath.from(putTestRunRs).get(JSONConstant.WORK_ITEM_RS_KEY);
        Assert.assertEquals(workItem, expectedWorkItemValue, "Launcher was not updated!");
    }

    @Test
    public void testGetTestRun() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        GetTestRunMethod getTestRunMethod = new GetTestRunMethod(testRunId);
        apiExecutor.expectStatus(getTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestRunMethod);
        apiExecutor.validateResponse(getTestRunMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testDeleteTestRun() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        apiExecutor.callApiMethod(new DeleteTestRunMethod(testRunId));
        GetTestRunMethod getTestRunMethod = new GetTestRunMethod(testRunId);
        apiExecutor.expectStatus(getTestRunMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(getTestRunMethod);
    }

    @Test
    public void testFinishTestRun() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        String expectedTestRunStatus = R.TESTDATA.get(ConfigConstant.STATUS_EXPECTED_KEY);
        String testRunStatus = new TestRunServiceAPIImpl().finishTestRun(testRunId);
        Assert.assertEquals(testRunStatus, expectedTestRunStatus, "TestRun was not finish!");
    }

    @Test
    public void testSendTestRunResultEmail() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        String expStatus = new TestRunServiceAPIImpl().finishTestRun(testRunId);
        LOGGER.info("TestRun status to verify: ".concat(expStatus));
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        String expectedTestRunIdInMail = String.format("%s/%s/%s", APIContextManager.BASE_URL,
                R.TESTDATA.get(ConfigConstant.TEST_RUN_URL_PREFIX_KEY), testRunId);
        LOGGER.info("TestRun url to verify: ".concat(expectedTestRunIdInMail));
        PostTestRunResultEmailMethod postTestRunResultEmailMethod = new PostTestRunResultEmailMethod(testRunId);
        apiExecutor.expectStatus(postTestRunResultEmailMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postTestRunResultEmailMethod);
        verifyIfEmailWasDelivered(expStatus);
    }

    @Test
    public void testGetTestRunResultHtmlText() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        GetTestRunResultHtmlTextMethod getTestRunResultHtmlTextMethod = new GetTestRunResultHtmlTextMethod(testRunId);
        apiExecutor.expectStatus(getTestRunResultHtmlTextMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestRunResultHtmlTextMethod);
    }

    @Test
    public void testMarkTestRunReviewed() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        PostMarkTestRunReviewedMethod postMarkTestRunReviewedMethod = new PostMarkTestRunReviewedMethod(testRunId);
        apiExecutor.expectStatus(postMarkTestRunReviewedMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postMarkTestRunReviewedMethod);
    }

    @Test
    public void testGetTestByTestRunId() {
        TestServiceImpl testServiceImpl = new TestServiceImpl();
        int testSuiteId = new TestSuiteServiceImpl().create();
        int jobId = new JobServiceImpl().create();
        int testRunId = new TestRunServiceAPIImpl().create(testSuiteId, jobId);
        int testCaseId = new TestCaseServiceImpl().create(testSuiteId);
        int testId = testServiceImpl.create(testCaseId, testRunId);
        testServiceImpl.finishTest(testCaseId, testRunId, testId);
        GetTestByTestRunIdMethod getTestByTestRunIdMethod = new GetTestByTestRunIdMethod(testRunId);
        apiExecutor.expectStatus(getTestByTestRunIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestByTestRunIdMethod);
        apiExecutor.validateResponse(getTestByTestRunIdMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testBuildTestRunJob() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        PostTestRunByJobMethod postTestRunByJobMethod = new PostTestRunByJobMethod(testRunId);
        apiExecutor.expectStatus(postTestRunByJobMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postTestRunByJobMethod);
    }

    @Test
    public void testDebugJob() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        GetDebugJobMethod getDebugJobMethod = new GetDebugJobMethod(testRunId);
        apiExecutor.expectStatus(getDebugJobMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getDebugJobMethod);
    }

    @Test
    public void testGetTestRunJobParameters() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        GetTestRunJobParametersMethod getTestRunJobParametersMethod = new GetTestRunJobParametersMethod(testRunId);
        apiExecutor.expectStatus(getTestRunJobParametersMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestRunJobParametersMethod);
        apiExecutor.validateResponse(getTestRunJobParametersMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @DataProvider(name = "rerunFailuresDataProvider")
    public Object[][] getRerunFailuresFlag() {
        return new Object[][]{{true}, {false}};
    }

    @Test(dataProvider = "rerunFailuresDataProvider")
    public void testGetTestRunJobById(boolean rerunFailures) {
        int testRunId = createTestRun(TESTS_TO_ADD);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        GetRerunTestRunByIdMethod getRerunTestRunByIdMethod = new GetRerunTestRunByIdMethod(testRunId, rerunFailures);
        apiExecutor.expectStatus(getRerunTestRunByIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getRerunTestRunByIdMethod);
        apiExecutor.validateResponse(getRerunTestRunByIdMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test(dataProvider = "rerunFailuresDataProvider")
    public void testRerunTestRunJob(boolean rerunFailures) {
        int testRunId = createTestRun(TESTS_TO_ADD);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        PostRerunTestRunJobsMethod postRerunTestRunJobsMethod = new PostRerunTestRunJobsMethod(rerunFailures);
        apiExecutor.expectStatus(postRerunTestRunJobsMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postRerunTestRunJobsMethod);
    }

    @Test
    public void testAbortTestRun() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        String ciRunId = new TestRunServiceAPIImpl().getCiRunId(testRunId);
        PostAbortTestRunMethod postAbortTestRunMethod = new PostAbortTestRunMethod(testRunId, ciRunId);
        apiExecutor.expectStatus(postAbortTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postAbortTestRunMethod);
    }

    @Test
    public void testAbortTestRunCi() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        String ciRunId = new TestRunServiceAPIImpl().getCiRunId(testRunId);
        GetAbortTestRunCiMethod getAbortTestRunCiMethod = new GetAbortTestRunCiMethod(testRunId, ciRunId);
        apiExecutor.expectStatus(getAbortTestRunCiMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getAbortTestRunCiMethod);
    }

    @Test
    public void testAbortTestRunDebug() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        String ciRunId = new TestRunServiceAPIImpl().getCiRunId(testRunId);
        GetAbortDebugTestRunMethod getAbortDebugTestRunMethod = new GetAbortDebugTestRunMethod(testRunId, ciRunId);
        apiExecutor.expectStatus(getAbortDebugTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getAbortDebugTestRunMethod);
    }

    @Test(enabled = false)
    public void testGetBuildConsoleOutput() {
        int testRunId = createTestRun(TESTS_TO_ADD);
        String ciRunId = new TestRunServiceAPIImpl().getCiRunId(testRunId);
        GetBuildConsoleOutputMethod getBuildConsoleOutputMethod = new GetBuildConsoleOutputMethod(ciRunId);
        apiExecutor.expectStatus(getBuildConsoleOutputMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getBuildConsoleOutputMethod);
    }

    @Test
    public void testGetTestRunBySearchCriteria() {
        String searchCriteriaType = R.TESTDATA.get(ConfigConstant.SEARCH_CRITERIA_TYPE_KEY);
        int testSuiteId = new TestSuiteServiceImpl().create();
        int jobId = new JobServiceImpl().create();
        apiExecutor.callApiMethod(new PostStartTestRunMethod(testSuiteId, jobId));
        GetTestRunBySearchCriteriaMethod getTestRunBySearchCriteriaMethod = new GetTestRunBySearchCriteriaMethod(searchCriteriaType, testSuiteId);
        apiExecutor.expectStatus(getTestRunBySearchCriteriaMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestRunBySearchCriteriaMethod);
        apiExecutor.validateResponse(getTestRunBySearchCriteriaMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    /**
     * Checks the expected testrun url contains in the last email
     *
     * @param expStatus expected testrun URL to get from email
     * @return
     */
    private boolean verifyIfEmailWasDelivered(String expStatus) {
        final int lastEmailIndex = 0;
        final int emailsCount = 1;
        LOGGER.info("Will get last email from inbox.");
        EMAIL.waitForEmailDelivered(new Date(),expStatus); // decency from connection, wait a little bit
        EmailMsg email = EMAIL.getInbox(emailsCount)[lastEmailIndex];
        return email.getContent().contains(expStatus);
    }

    /**
     * Test Run Controller v1
     */
    @Test
    public void testStartTestRunV1WithoutTests() {
        PostStartTestRunV1Method postStartTestRunV1Method = new PostStartTestRunV1Method();
        apiExecutor.expectStatus(postStartTestRunV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postStartTestRunV1Method);
        apiExecutor.validateResponse(postStartTestRunV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testGetTestsByCiRunIdV1() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        String ciRunId = new TestRunServiceAPIImplV1().getCiRunId(testRunId);
        GetTestsByCiRunIdV1Method getTestsByCiRunIdV1Method = new GetTestsByCiRunIdV1Method(ciRunId);
        apiExecutor.expectStatus(getTestsByCiRunIdV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestsByCiRunIdV1Method);
    }

    @Test
    public void testFinishTests() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        PutFinishTestRunV1Method putFinishTestRunV1Method = new PutFinishTestRunV1Method(testRunId);
        apiExecutor.expectStatus(putFinishTestRunV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(putFinishTestRunV1Method);
    }

    @Test
    public void testStartTestsInTestRunV1() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        PostStartTestsInTestRunV1Method postStartTestsInTestRunV1Method = new PostStartTestsInTestRunV1Method(testRunId);
        apiExecutor.expectStatus(postStartTestsInTestRunV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postStartTestsInTestRunV1Method);
        apiExecutor.validateResponse(postStartTestsInTestRunV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
    }

    @Test
    public void testGetTestsByCiRunIdV1WithTests() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        String ciRunId = new TestRunServiceAPIImplV1().getCiRunId(testRunId);
        new TestServiceAPIV1Impl().createTest(testRunId);
        GetTestsByCiRunIdV1Method getTestsByCiRunIdV1Method = new GetTestsByCiRunIdV1Method(ciRunId);
        apiExecutor.expectStatus(getTestsByCiRunIdV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestsByCiRunIdV1Method);
        apiExecutor.validateResponse(getTestsByCiRunIdV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
    }

    @Test
    public void testUpdateTestWithResultPassedInTestRunV1() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        PutUpdateTestInTestRunV1Method putUpdateTestInTestRunV1Method = new PutUpdateTestInTestRunV1Method(testRunId, testId, RESULT_PASSED);
        apiExecutor.expectStatus(putUpdateTestInTestRunV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(putUpdateTestInTestRunV1Method);
        apiExecutor.validateResponse(putUpdateTestInTestRunV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        String response = apiExecutor.callApiMethod(putUpdateTestInTestRunV1Method);
        String actualResult = JsonPath.from(response).getString(JSONConstant.RESULT);
        Assert.assertEquals(actualResult, RESULT_PASSED, "Result is not as expected!");
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
    }

    @Test
    public void testUpdateTestWithResultFailedInTestRunV1() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        PutUpdateTestInTestRunV1Method putUpdateTestInTestRunV1Method = new PutUpdateTestInTestRunV1Method(testRunId, testId, RESULT_FAILED);
        apiExecutor.expectStatus(putUpdateTestInTestRunV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(putUpdateTestInTestRunV1Method);
        apiExecutor.validateResponse(putUpdateTestInTestRunV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        String response = apiExecutor.callApiMethod(putUpdateTestInTestRunV1Method);
        String actualResult = JsonPath.from(response).getString(JSONConstant.RESULT);
        Assert.assertEquals(actualResult, RESULT_FAILED, "Result is not as expected!");
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
    }

    @Test
    public void testUpdateTestWithResultSkippedInTestRunV1() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        PutUpdateTestInTestRunV1Method putUpdateTestInTestRunV1Method = new PutUpdateTestInTestRunV1Method(testRunId, testId, RESULT_SKIPPED);
        apiExecutor.expectStatus(putUpdateTestInTestRunV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(putUpdateTestInTestRunV1Method);
        apiExecutor.validateResponse(putUpdateTestInTestRunV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        String response = apiExecutor.callApiMethod(putUpdateTestInTestRunV1Method);
        String actualResult = JsonPath.from(response).getString(JSONConstant.RESULT);
        Assert.assertEquals(actualResult, RESULT_SKIPPED, "Result is not as expected!");
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
    }

    @Test
    public void testGetTestRunStatusV1WithTestResultsFAILEDAndPASSED() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        int testId1 = new TestServiceAPIV1Impl().createTest(testRunId);
        int testId2 = new TestServiceAPIV1Impl().createTest(testRunId);
        new TestServiceAPIV1Impl().updateResultInTest(testRunId, testId1, RESULT_FAILED);
        new TestServiceAPIV1Impl().updateResultInTest(testRunId, testId2, RESULT_PASSED);
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
        String actualResult = new TestRunServiceAPIImplV1().getTestRunResult(testRunId);
        Assert.assertEquals(actualResult, RESULT_FAILED, "Result is not as expected!");
    }

    @Test
    public void testGetTestRunStatusV1WithTestResultsPASSED() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        int testId1 = new TestServiceAPIV1Impl().createTest(testRunId);
        int testId2 = new TestServiceAPIV1Impl().createTest(testRunId);
        new TestServiceAPIV1Impl().updateResultInTest(testRunId, testId1, RESULT_PASSED);
        new TestServiceAPIV1Impl().updateResultInTest(testRunId, testId2, RESULT_PASSED);
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
        String actualResult = new TestRunServiceAPIImplV1().getTestRunResult(testRunId);
        Assert.assertEquals(actualResult, RESULT_PASSED, "Result is not as expected!");
    }

    @Test
    public void testGetTestRunStatusV1WithTestResultsSKIPPEDAndPASSED() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        int testId1 = new TestServiceAPIV1Impl().createTest(testRunId);
        int testId2 = new TestServiceAPIV1Impl().createTest(testRunId);
        new TestServiceAPIV1Impl().updateResultInTest(testRunId, testId1, RESULT_SKIPPED);
        new TestServiceAPIV1Impl().updateResultInTest(testRunId, testId2, RESULT_PASSED);
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
        String actualResult = new TestRunServiceAPIImplV1().getTestRunResult(testRunId);
        Assert.assertEquals(actualResult, RESULT_FAILED, "Result is not as expected!");
    }

    @Test
    public void testGetTestRunStatusV1WithUnfinishedProcess() {
        int testRunId = new TestRunServiceAPIImplV1().create();
        int testId1 = new TestServiceAPIV1Impl().createTest(testRunId);
        String actualResult = new TestRunServiceAPIImplV1().getTestRunResult(testRunId);
        Assert.assertEquals(actualResult, RESULT_IN_PROGRESS, "Result is not as expected!");
        new TestRunServiceAPIImplV1().finishTestRun(testRunId);
    }
}
