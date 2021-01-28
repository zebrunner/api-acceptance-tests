package com.qaprosoft.zafira.api;

import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.artifactsController.GetLogsV1Method;
import com.qaprosoft.zafira.api.artifactsController.GetScreenshotsV1Method;
import com.qaprosoft.zafira.api.artifactsController.PostLogsV1Method;
import com.qaprosoft.zafira.api.artifactsController.PostScreenshotsV1Method;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.manager.APIContextManager;
import com.qaprosoft.zafira.service.impl.ArtifactsControllerV1ServiceImpl;
import com.qaprosoft.zafira.service.impl.TestRunServiceAPIImpl;
import com.qaprosoft.zafira.service.impl.TestRunServiceAPIImplV1;
import com.qaprosoft.zafira.service.impl.TestServiceAPIV1Impl;
import com.qaprosoft.zafira.util.WaitUtil;
import org.apache.log4j.Logger;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;


public class ArtifactsControllerTest extends ZafiraAPIBaseTest {

    private final static Logger LOGGER = Logger.getLogger(ArtifactsControllerTest.class);
    int testRunId = new TestRunServiceAPIImplV1().create();
    private final static int EXPECTED_COUNT_THREE = 3;
    private final static int EXPECTED_COUNT_ONE = 1;

    @AfterTest
    public void testFinishTestRun() {
        new TestRunServiceAPIImpl().deleteById(testRunId);
    }

    @Test
    public void testGetLogs() {
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        new ArtifactsControllerV1ServiceImpl().createLogRecord(testRunId, testId);
        GetLogsV1Method getLogsV1Method = new GetLogsV1Method(testRunId, testId);
        apiExecutor.callApiMethod(getLogsV1Method);
        apiExecutor.expectStatus(getLogsV1Method,HTTPStatusCodeType.OK);
        Assert.assertTrue(WaitUtil.waitForLogFound(getLogsV1Method, EXPECTED_COUNT_ONE), "Log was not found!");
        apiExecutor.validateResponse(getLogsV1Method, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testGetScreenshots() {
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        String filePath = R.TESTDATA.get(ConfigConstant.SCREENSHOT_PATH_KEY);
        new ArtifactsControllerV1ServiceImpl().createScreenshot(testRunId, testId, filePath);
        GetScreenshotsV1Method getScreenshotsV1Method = new GetScreenshotsV1Method(testRunId, testId);
        Assert.assertTrue(WaitUtil.waitForScreenshotFound(getScreenshotsV1Method, EXPECTED_COUNT_ONE), "Screenshot was not found!");
    }

    @Test
    public void testSendTestExecutionLogRecord() {
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        PostLogsV1Method postLogsV1Method = new PostLogsV1Method(testRunId, testId);
        apiExecutor.expectStatus(postLogsV1Method, HTTPStatusCodeType.ACCEPTED);
        apiExecutor.callApiMethod(postLogsV1Method);
    }

    @Test
    public void testSendOneScreenshot() {
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        String filePath = R.TESTDATA.get(ConfigConstant.SCREENSHOT_PATH_KEY);
        PostScreenshotsV1Method postScreenshotsV1Method = new PostScreenshotsV1Method(testRunId, testId, filePath);
        apiExecutor.expectStatus(postScreenshotsV1Method, HTTPStatusCodeType.CREATED);
        apiExecutor.callApiMethod(postScreenshotsV1Method);
    }

    @Test
    public void testGetLogsWithThreeRecords() {
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        new ArtifactsControllerV1ServiceImpl().createLogRecord(testRunId, testId);
        new ArtifactsControllerV1ServiceImpl().createLogRecord(testRunId, testId);
        new ArtifactsControllerV1ServiceImpl().createLogRecord(testRunId, testId);
        GetLogsV1Method getLogsV1Method = new GetLogsV1Method(testRunId, testId);
        Assert.assertTrue(WaitUtil.waitForLogFound(getLogsV1Method, EXPECTED_COUNT_THREE), "Log was not found!");
    }

    @Test
    public void testGetScreenshotsWithThreeScreenshot() {
        int testId = new TestServiceAPIV1Impl().createTest(testRunId);
        String filePath = R.TESTDATA.get(ConfigConstant.SCREENSHOT_PATH_KEY);
        new ArtifactsControllerV1ServiceImpl().createScreenshot(testRunId, testId, filePath);
        new ArtifactsControllerV1ServiceImpl().createScreenshot(testRunId, testId, filePath);
        new ArtifactsControllerV1ServiceImpl().createScreenshot(testRunId, testId, filePath);
        GetScreenshotsV1Method getScreenshotsV1Method = new GetScreenshotsV1Method(testRunId, testId);
        Assert.assertTrue(WaitUtil.waitForScreenshotFound(getScreenshotsV1Method, EXPECTED_COUNT_THREE), "Screenshot was not found!");
    }
}
