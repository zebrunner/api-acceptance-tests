package com.qaprosoft.zafira.api;

import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.testController.*;
import com.qaprosoft.zafira.api.testRunController.v1.DeleteTestByIdV1Method;
import com.qaprosoft.zafira.api.testRunController.v1.GetTestsByCiRunIdV1Method;
import com.qaprosoft.zafira.api.testRunController.v1.PostStartTestsInTestRunV1Method;
import com.qaprosoft.zafira.api.testRunController.v1.PutFinishTestInTestRunV1Method;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.constant.ConstantName;
import com.qaprosoft.zafira.constant.JSONConstant;
import com.qaprosoft.zafira.constant.TestRailConstant;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.service.impl.TestRunServiceAPIImplV1;
import com.qaprosoft.zafira.service.impl.TestServiceV1Impl;
import com.qaprosoft.zafira.service.impl.TestServiceImpl;
import com.zebrunner.agent.core.annotation.TestLabel;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;
import java.util.List;

public class TestV1Test extends ZafiraAPIBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestV1Test.class);
    private static TestRunServiceAPIImplV1 testRunServiceAPIImplV1 = new TestRunServiceAPIImplV1();
    private static TestServiceV1Impl testServiceV1 = new TestServiceV1Impl();
    private int testRunId;
    private static final String RESULT_SKIPPED = "SKIPPED";
    private static final String RESULT_FAILED = "FAILED";
    private static final String RESULT_PASSED = "PASSED";
    private static final String RESULT_IN_PROGRESS = "IN_PROGRESS";
    private static final String RESULT_ABORTED = "ABORTED";

    @AfterMethod
    public void deleteTestRun() {
        new TestRunServiceAPIImplV1().deleteTestRun(testRunId);
    }

    /**
     * Test execution start
     */

    @Test
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38974")
    public void testStartTestsInTestRunV1() {
        testRunId = new TestRunServiceAPIImplV1().start();
        PostStartTestsInTestRunV1Method postStartTestsInTestRunV1Method = new PostStartTestsInTestRunV1Method(testRunId);
        apiExecutor.expectStatus(postStartTestsInTestRunV1Method, HTTPStatusCodeType.OK);
        String rs = apiExecutor.callApiMethod(postStartTestsInTestRunV1Method);
        int testId = JsonPath.from(rs).getInt(JSONConstant.ID_KEY);
        apiExecutor.validateResponse(postStartTestsInTestRunV1Method,
                JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        Assert.assertTrue(new TestServiceImpl().getAllTestIdsByTestRunId(testRunId).contains(testId),
                "Test with id " + testId + " was not found!");
    }

    @Test(dataProvider = "rqForTestMandatoryFieldsDataProvider")
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38977")
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38978")
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38979")
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38980")
    public void testStartTestsInTestRunV1WithoutField(String field) {
        testRunId = new TestRunServiceAPIImplV1().start();
        PostStartTestsInTestRunV1Method postStartTestsInTestRunV1Method = new PostStartTestsInTestRunV1Method(testRunId);
        postStartTestsInTestRunV1Method.removeProperty(field);
        apiExecutor.expectStatus(postStartTestsInTestRunV1Method, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(postStartTestsInTestRunV1Method);
    }

    @DataProvider(name = "rqForTestMandatoryFieldsDataProvider")
    public Object[][] getRqForTestMandatoryFields() {
        return new Object[][]{{JSONConstant.NAME}, {JSONConstant.CLASS_NAME},
                {JSONConstant.METHOD_NAME}, {JSONConstant.STARTED_AT}};
    }

    @Test(dataProvider = "startedAtDataProvider")
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38981")
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38984")
    public void testStartTestWithDifferentStartedAt(String description, String date) {
        testRunId = new TestRunServiceAPIImplV1().start();
        PostStartTestsInTestRunV1Method postStartTestMethod = new PostStartTestsInTestRunV1Method(testRunId);
        postStartTestMethod.addProperty(JSONConstant.STARTED_AT, date);
        apiExecutor.expectStatus(postStartTestMethod, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(postStartTestMethod);
    }

    @Test(dataProvider = "rqForTestMandatoryFieldsDataProvider")
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38985")
    public void testStartTestWithEmptyMandatoryField(String field) {
        testRunId = new TestRunServiceAPIImplV1().start();
        PostStartTestsInTestRunV1Method postStartTestMethod = new PostStartTestsInTestRunV1Method(testRunId);
        postStartTestMethod.addProperty(field, ConstantName.EMPTY);
        apiExecutor.expectStatus(postStartTestMethod, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(postStartTestMethod);
    }

    @Test
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38986")
    public void testStartTestsInTestRunV1WithNonExistentTestRunId() {
        TestRunServiceAPIImplV1 testRunServiceAPIImplV1 = new TestRunServiceAPIImplV1();
        testRunId = testRunServiceAPIImplV1.start();
        testRunServiceAPIImplV1.deleteTestRun(testRunId);
        PostStartTestsInTestRunV1Method postStartTestsInTestRunV1Method = new PostStartTestsInTestRunV1Method(testRunId);
        apiExecutor.expectStatus(postStartTestsInTestRunV1Method, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(postStartTestsInTestRunV1Method);
    }

    @Test
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "38989")
    public void testStartTestV1WithInvalidTestRunId() {
        testRunId = new TestRunServiceAPIImplV1().start();
        PostStartTestsInTestRunV1Method postStartTestsInTestRunV1Method = new PostStartTestsInTestRunV1Method(testRunId);
        String methodPath = postStartTestsInTestRunV1Method.getMethodPath().replace(String.valueOf(testRunId), "!");
        postStartTestsInTestRunV1Method.setMethodPath(methodPath);
        apiExecutor.expectStatus(postStartTestsInTestRunV1Method, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(postStartTestsInTestRunV1Method);
    }

    @Test
    @TestLabel(name = TestRailConstant.TESTCASE_ID, value = "39918")
    public void testStartTestV1WithEmptyLabels() {
        testRunId = new TestRunServiceAPIImplV1().start();
        PostStartTestsInTestRunV1Method postStartTestsInTestRunV1Method = new PostStartTestsInTestRunV1Method(testRunId);
        postStartTestsInTestRunV1Method.addProperty(JSONConstant.LABEL_KEY, R.TESTDATA.get(ConfigConstant.LABEL_KEY));
        apiExecutor.expectStatus(postStartTestsInTestRunV1Method, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(postStartTestsInTestRunV1Method);
    }

    /**
     * Test execution finish
     */

    @DataProvider(name = "testResult")
    public Object[][] getTestResult() {
        return new Object[][]{{RESULT_PASSED}, {RESULT_FAILED},
                {RESULT_ABORTED}, {RESULT_SKIPPED}, {RESULT_IN_PROGRESS}};
    }

    @DataProvider(name = "rqMandatoryFields")
    public Object[][] getRqMandatoryFieldsForUpdateTest() {
        return new Object[][]{{JSONConstant.RESULT}, {JSONConstant.ENDED_AT}};
    }

    @Test(dataProvider = "testResult")
    public void testFinishTestWithResult(String result) {
        TestRunServiceAPIImplV1 testRunServiceAPIImplV11 = new TestRunServiceAPIImplV1();
        testRunId = testRunServiceAPIImplV11.start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        PutFinishTestInTestRunV1Method putFinishTestInTestRunV1Method = new PutFinishTestInTestRunV1Method(testRunId, testId, result);
        apiExecutor.expectStatus(putFinishTestInTestRunV1Method, HTTPStatusCodeType.OK);
        String response = apiExecutor.callApiMethod(putFinishTestInTestRunV1Method);
        apiExecutor.validateResponse(putFinishTestInTestRunV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        String actualResult = JsonPath.from(response).getString(JSONConstant.RESULT);
        Assert.assertEquals(actualResult, result, "Result is not as expected!");
        String actualTestStatus = testRunServiceAPIImplV11.getTestStatusByTestId(testRunId, testId);
        Assert.assertEquals(actualTestStatus, result, "Status is not as expected!");
    }

    @Test(dataProvider = "testResult", description = "validTestResultToLowerCase")
    public void testFinishTestWithResultToLowerCase(String result) {
        testRunId = new TestRunServiceAPIImplV1().start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        PutFinishTestInTestRunV1Method putFinishTestInTestRunV1Method = new PutFinishTestInTestRunV1Method(testRunId, testId, result.toLowerCase());
        apiExecutor.expectStatus(putFinishTestInTestRunV1Method, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(putFinishTestInTestRunV1Method);
    }

    @Test()
    public void testFinishTestWithInvalidResult() {
        String invalidResult = "!";
        testRunId = new TestRunServiceAPIImplV1().start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        PutFinishTestInTestRunV1Method putFinishTestInTestRunV1Method = new PutFinishTestInTestRunV1Method(testRunId, testId, invalidResult);
        apiExecutor.expectStatus(putFinishTestInTestRunV1Method, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(putFinishTestInTestRunV1Method);
    }

    @Test(dataProvider = "rqMandatoryFields")
    public void testFinishTestWithoutField(String field) {
        testRunId = new TestRunServiceAPIImplV1().start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        PutFinishTestInTestRunV1Method putFinishTestInTestRunV1Method = new PutFinishTestInTestRunV1Method(testRunId, testId, RESULT_PASSED);
        putFinishTestInTestRunV1Method.removeProperty(field);
        apiExecutor.expectStatus(putFinishTestInTestRunV1Method, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(putFinishTestInTestRunV1Method);
    }

    @DataProvider(name = "startedAtDataProvider")
    public Object[][] getStartedAt() {
        return new Object[][]{{"invalid", "!"},
                {"in future", OffsetDateTime.now().plusDays(5).toString()}};
    }

    @Test(dataProvider = "startedAtDataProvider")
    public void testFinishTestWithEndedAt(String description, String endedAt) {
        testRunId = new TestRunServiceAPIImplV1().start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        PutFinishTestInTestRunV1Method putFinishTestInTestRunV1Method = new PutFinishTestInTestRunV1Method(testRunId, testId, RESULT_PASSED);
        putFinishTestInTestRunV1Method.addProperty("endedAt", endedAt);
        apiExecutor.expectStatus(putFinishTestInTestRunV1Method, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(putFinishTestInTestRunV1Method);
    }

    /**
     * Delete test from testRun
     */

    @Test
    public void testDeleteTest() {
        testRunId = new TestRunServiceAPIImplV1().start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        DeleteTestByIdV1Method deleteTestByIdV1Method = new DeleteTestByIdV1Method(testRunId, testId);
        apiExecutor.expectStatus(deleteTestByIdV1Method, HTTPStatusCodeType.NO_CONTENT);
        apiExecutor.callApiMethod(deleteTestByIdV1Method);
        Assert.assertFalse(new TestServiceImpl().getAllTestIdsByTestRunId(testRunId).contains(testId),
                "Test with id " + testId + " was not found!");
    }

    @Test
    public void testDeleteTestWithNonExistentTestId() {
        testRunId = new TestRunServiceAPIImplV1().start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        new TestServiceV1Impl().deleteTest(testRunId, testId);
        DeleteTestByIdV1Method deleteTestByIdV1Method = new DeleteTestByIdV1Method(testRunId, testId);
        apiExecutor.expectStatus(deleteTestByIdV1Method, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(deleteTestByIdV1Method);
    }

    @Test
    public void testDeleteTestWithNonExistentTestRunId() {
        testRunId = new TestRunServiceAPIImplV1().start();
        int testId = new TestServiceV1Impl().startTest(testRunId);
        new TestRunServiceAPIImplV1().deleteTestRun(testRunId);
        DeleteTestByIdV1Method deleteTestByIdV1Method = new DeleteTestByIdV1Method(testRunId, testId);
        apiExecutor.expectStatus(deleteTestByIdV1Method, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(deleteTestByIdV1Method);
    }

    /**
     * Get tests by ciRunId
     */

    @Test
    public void testGetTestsByTestRunIdV1WithoutQuery() {
        TestRunServiceAPIImplV1 testRunServiceAPIImplV1 = new TestRunServiceAPIImplV1();
        testRunId = testRunServiceAPIImplV1.start();
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);
        new TestServiceV1Impl().startTest(testRunId);
        GetTestsByCiRunIdV1Method getTestsByCiRunIdV1Method = new GetTestsByCiRunIdV1Method(ciRunId);
        apiExecutor.expectStatus(getTestsByCiRunIdV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestsByCiRunIdV1Method);
        apiExecutor.validateResponse(getTestsByCiRunIdV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test(dataProvider = "testResult")
    public void testGetTestsByTestRunIdV1WithQueryStatuses(String result) {
        String methodName = "NewMethodName".concat(RandomStringUtils.randomAlphabetic(5));
        TestRunServiceAPIImplV1 testRunServiceAPIImplV1 = new TestRunServiceAPIImplV1();
        testRunId = testRunServiceAPIImplV1.start();
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);
        int testId = testServiceV1.startTestWithMethodName(testRunId, methodName);
        int testId1 = new TestServiceV1Impl().startTest(testRunId);
        new TestServiceV1Impl().finishTestAsResult(testRunId, testId, result);
        GetTestsByCiRunIdV1Method getTestsByCiRunIdV1Method = new GetTestsByCiRunIdV1Method(ciRunId);
        getTestsByCiRunIdV1Method.addUrlParameter(JSONConstant.STATUSES_KEY, result);
        apiExecutor.expectStatus(getTestsByCiRunIdV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestsByCiRunIdV1Method);
        apiExecutor.validateResponse(getTestsByCiRunIdV1Method, JSONCompareMode.LENIENT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testGetTestsByTestRunIdV1WithQueryTests() {
        TestRunServiceAPIImplV1 testRunServiceAPIImplV1 = new TestRunServiceAPIImplV1();
        testRunId = testRunServiceAPIImplV1.start();
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);
        int testId = new TestServiceV1Impl().startTest(testRunId);
        int testId1 = new TestServiceV1Impl().startTest(testRunId);
        GetTestsByCiRunIdV1Method getTestsByCiRunIdV1Method = new GetTestsByCiRunIdV1Method(ciRunId);
        getTestsByCiRunIdV1Method.addUrlParameter("tests", String.valueOf(testId));
        apiExecutor.expectStatus(getTestsByCiRunIdV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestsByCiRunIdV1Method);
        apiExecutor.validateResponse(getTestsByCiRunIdV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testGetTestsByTestRunIdV1WithQueryTestsAndStatuses() {
        TestRunServiceAPIImplV1 testRunServiceAPIImplV1 = new TestRunServiceAPIImplV1();
        testRunId = testRunServiceAPIImplV1.start();
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);
        int testId = new TestServiceV1Impl().startTest(testRunId);
        new TestServiceV1Impl().startTest(testRunId);
        GetTestsByCiRunIdV1Method getTestsByCiRunIdV1Method = new GetTestsByCiRunIdV1Method(ciRunId);
        getTestsByCiRunIdV1Method.addUrlParameter("statuses", RESULT_IN_PROGRESS);
        getTestsByCiRunIdV1Method.addUrlParameter("tests", String.valueOf(testId));
        apiExecutor.expectStatus(getTestsByCiRunIdV1Method, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getTestsByCiRunIdV1Method);
        apiExecutor.validateResponse(getTestsByCiRunIdV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testGetTestsByCiRunIdV1WithoutTests() {
        testRunId = new TestRunServiceAPIImplV1().start();
        String ciRunId = new TestRunServiceAPIImplV1().getCiRunId(testRunId);
        GetTestsByCiRunIdV1Method getTestsByCiRunIdV1Method = new GetTestsByCiRunIdV1Method(ciRunId);
        apiExecutor.expectStatus(getTestsByCiRunIdV1Method, HTTPStatusCodeType.OK);
        String rs = apiExecutor.callApiMethod(getTestsByCiRunIdV1Method);
        Assert.assertEquals(JsonPath.from(rs).getList("").size(), 0,
                "Test run does not contain tests, but the list of result is not empty!");
    }

    @Test
    public void testGetTestResultsHistoryById() {
        testRunId = testRunServiceAPIImplV1.start();
        int testId = testServiceV1.startTest(testRunId);
        int testId1 = testServiceV1.startTest(testRunId);
        testServiceV1.finishTestAsResult(testRunId, testId, RESULT_PASSED);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        GetTestResultsHistoryByIdMethod getTestResultsHistoryByIdMethod = new GetTestResultsHistoryByIdMethod(testId1);
        apiExecutor.callApiMethod(getTestResultsHistoryByIdMethod);
        apiExecutor.expectStatus(getTestResultsHistoryByIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.validateResponse(getTestResultsHistoryByIdMethod, JSONCompareMode.STRICT,
                JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testLinkWorkItemToTest() {
        String methodName = "NewMethodName".concat(RandomStringUtils.randomAlphabetic(5));
        String expectedJiraIdValue = R.TESTDATA.get(ConfigConstant.EXPECTED_JIRA_ID_KEY);
        String workItemType = R.TESTDATA.get(ConfigConstant.WORK_ITEM_TYPE_KEY);
        testRunId = startTestRunV1();
        int testId = testServiceV1.startTestWithMethodName(testRunId, methodName);
        new TestServiceV1Impl().finishTestAsResult(testRunId, testId, "FAILED");
        PostLinkWorkItemMethod postLinkWorkItemMethod = new PostLinkWorkItemMethod(1, expectedJiraIdValue,
                testId, workItemType);
        apiExecutor.expectStatus(postLinkWorkItemMethod, HTTPStatusCodeType.OK);
        String linkWorkItemRs = apiExecutor.callApiMethod(postLinkWorkItemMethod);
        String jiraId = JsonPath.from(linkWorkItemRs).get(JSONConstant.JIRA_ID_KEY);
        Assert.assertEquals(jiraId, expectedJiraIdValue, "Work item was not linked to test!");
    }

    @Test
    public void testLinkWorkItemToTestWithStatusPASSED() {
        String methodName = "NewMethodName".concat(RandomStringUtils.randomAlphabetic(5));
        String expectedJiraIdValue = R.TESTDATA.get(ConfigConstant.EXPECTED_JIRA_ID_KEY);
        String workItemType = R.TESTDATA.get(ConfigConstant.WORK_ITEM_TYPE_KEY);
        testRunId = startTestRunV1();
        int testId = testServiceV1.startTestWithMethodName(testRunId, methodName);
        new TestServiceV1Impl().finishTestAsResult(testRunId, testId, "PASSED");
        PostLinkWorkItemMethod postLinkWorkItemMethod = new PostLinkWorkItemMethod(1, expectedJiraIdValue,
                testId, workItemType);
        apiExecutor.expectStatus(postLinkWorkItemMethod, HTTPStatusCodeType.FORBIDDEN);
    }

    @Test
    public void testGetWorkItem() {
        String methodName = "NewMethodName".concat(RandomStringUtils.randomAlphabetic(5));
        String workItemType = R.TESTDATA.get(ConfigConstant.WORK_ITEM_TYPE_KEY);
        String jiraId = R.TESTDATA.get(ConfigConstant.EXPECTED_JIRA_ID_KEY);
        testRunId = startTestRunV1();
        int testId = testServiceV1.startTestWithMethodName(testRunId, methodName);
        new TestServiceV1Impl().finishTestAsResult(testRunId, testId, "FAILED");
        apiExecutor.callApiMethod(new PostLinkWorkItemMethod(1, jiraId, testId, workItemType));
        GetWorkItemMethod getWorkItemMethod = new GetWorkItemMethod(testId, workItemType);
        apiExecutor.expectStatus(getWorkItemMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getWorkItemMethod);
        apiExecutor.validateResponse(getWorkItemMethod,
                JSONCompareMode.LENIENT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testCreateBatchWorkItem() {
        String methodName = "NewMethodName".concat(RandomStringUtils.randomAlphabetic(5));
        String jiraId = R.TESTDATA.get(ConfigConstant.EXPECTED_JIRA_ID_KEY);
        testRunId = startTestRunV1();
        int testId = testServiceV1.startTestWithMethodName(testRunId, methodName);
        new TestServiceV1Impl().finishTestAsResult(testRunId, testId, "FAILED");
        PostCreateBatchWorkItemsMethod postCreateBatchWorkItemsMethod = new PostCreateBatchWorkItemsMethod(testRunId, testId, jiraId);
        apiExecutor.expectStatus(postCreateBatchWorkItemsMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postCreateBatchWorkItemsMethod);
        apiExecutor.validateResponse(postCreateBatchWorkItemsMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testDeleteWorkItem() {
        String methodName = "NewMethodName".concat(RandomStringUtils.randomAlphabetic(5));
        TestServiceImpl testServiсeImpl = new TestServiceImpl();
        String expectedJiraIdValue = R.TESTDATA.get(ConfigConstant.EXPECTED_JIRA_ID_KEY);
        String workItemType = R.TESTDATA.get(ConfigConstant.WORK_ITEM_TYPE_KEY);
        testRunId = startTestRunV1();
        int testId = testServiceV1.startTestWithMethodName(testRunId, methodName);
        new TestServiceV1Impl().finishTestAsResult(testRunId, testId, "FAILED");
        String linkWorkItemRs = apiExecutor.callApiMethod(new PostLinkWorkItemMethod(1, expectedJiraIdValue,
                testId, workItemType));
        int workItemId = JsonPath.from(linkWorkItemRs).get(JSONConstant.ID_KEY);
        String testRs = testServiсeImpl.getAllTest(testRunId);
        int workItemIdRs = JsonPath.from(testRs).get(JSONConstant.WORK_ITEM_ID_CHECK_KEY);
        Assert.assertNotEquals(0, workItemIdRs, "Work item was not linked!");
        apiExecutor.callApiMethod(new DeleteWorkItemMethod(testId, workItemId));
        String testRsAfterDelete = testServiсeImpl.getAllTest(testRunId);
        List<Integer> workItemsAfterDelete = JsonPath.from(testRsAfterDelete).getList(JSONConstant.WORK_ITEMS_ARRAY_KEY);
        Assert.assertTrue(workItemsAfterDelete.isEmpty(), "Work item was not deleted!");
    }

}
