package com.qaprosoft.zafira.api;

import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.projectTestRuns.*;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.domain.EmailMsg;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.manager.APIContextManager;
import com.qaprosoft.zafira.manager.EmailManager;
import com.qaprosoft.zafira.service.impl.*;
import com.qaprosoft.zafira.util.AbstractAPIMethodUtil;
import com.qaprosoft.zafira.util.CryptoUtil;
import com.zebrunner.agent.core.annotation.Maintainer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Date;

@Maintainer("obabich")
public class ProjectTestRunControllerTest extends ZafiraAPIBaseTest {
    private static int projectId;
    private int testRunId;
    private TestRunServiceAPIImplV1 testRunServiceAPIImplV1 = new TestRunServiceAPIImplV1();
    private ProjectV1ServiceImpl projectV1Service = new ProjectV1ServiceImpl();
    private ProjectV1TestRunServiceImpl projectV1TestRunService = new ProjectV1TestRunServiceImpl();
    private MilestoneServiceImpl milestoneService = new MilestoneServiceImpl();
    private static Logger LOGGER = Logger.getLogger(ProjectTestRunControllerTest.class);
    private final String TEST_EMAIL = R.TESTDATA.get(ConfigConstant.TEST_EMAIL_KEY);
    private final EmailManager EMAIL = new EmailManager(
            CryptoUtil.decrypt(R.TESTDATA.get(ConfigConstant.GMAIL_USERNAME_KEY)),
            CryptoUtil.decrypt(R.TESTDATA.get(ConfigConstant.GMAIL_PASSWORD_KEY)));


    @BeforeTest
    public void testCreateProject() {
        projectId = projectV1Service.createProject();
    }

    @AfterTest
    public void testDeleteProject() {
        projectV1Service.deleteProjectById(projectId);
    }

    @AfterMethod
    public void deleteTestRun() {
        projectV1TestRunService.deleteProjectTestRun(testRunId);
    }

    @Test
    public void testGetTestRunByCiRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);

        GetProjectTestRunByCiRunIdMethod getProjectTestRunByCiRunIdMethod =
                new GetProjectTestRunByCiRunIdMethod(ciRunId);
        apiExecutor.expectStatus(getProjectTestRunByCiRunIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getProjectTestRunByCiRunIdMethod);
        apiExecutor.validateResponse(getProjectTestRunByCiRunIdMethod, JSONCompareMode.STRICT,
                JsonCompareKeywords.ARRAY_CONTAINS.getKey() + "labels");
    }

    @Test(groups = {"negative"})
    public void testGetTestRunByCiRunIdWithNonExistentCiRunId() {
        String ciRunId = RandomStringUtils.randomAlphabetic(5);
        GetProjectTestRunByCiRunIdMethod getProjectTestRunByCiRunIdMethod =
                new GetProjectTestRunByCiRunIdMethod(ciRunId);
        apiExecutor.expectStatus(getProjectTestRunByCiRunIdMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(getProjectTestRunByCiRunIdMethod);
    }

    @Test(groups = {"negative"})
    public void testGetTestRunByCiRunIdWithoutQueryParams() {
        String ciRunId = RandomStringUtils.randomAlphabetic(5);
        GetProjectTestRunByCiRunIdMethod getProjectTestRunByCiRunIdMethod =
                new GetProjectTestRunByCiRunIdMethod(ciRunId);
        AbstractAPIMethodUtil.deleteQuery(getProjectTestRunByCiRunIdMethod);
        apiExecutor.expectStatus(getProjectTestRunByCiRunIdMethod, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(getProjectTestRunByCiRunIdMethod);
    }

    @Test
    public void testGetTestRunByTestRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);

        GetProjectTestRunByTestRunIdMethod projectTestRunByTestRunIdMethod =
                new GetProjectTestRunByTestRunIdMethod(testRunId);
        apiExecutor.expectStatus(projectTestRunByTestRunIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(projectTestRunByTestRunIdMethod);
        apiExecutor.validateResponse(projectTestRunByTestRunIdMethod, JSONCompareMode.STRICT,
                JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test(groups = {"negative"})
    public void testGetTestRunByNonexistentTestRunId() {
        testRunId = testRunServiceAPIImplV1.start();
        testRunServiceAPIImplV1.deleteTestRun(testRunId);

        GetProjectTestRunByTestRunIdMethod projectTestRunByTestRunIdMethod =
                new GetProjectTestRunByTestRunIdMethod(testRunId);
        apiExecutor.expectStatus(projectTestRunByTestRunIdMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(projectTestRunByTestRunIdMethod);
    }

    @Test()
    public void testSearchProjectTestRuns() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);

        GetSearchProjectTestRunMethod getSearchProjectTestRunMethod =
                new GetSearchProjectTestRunMethod(projectId);
        apiExecutor.expectStatus(getSearchProjectTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getSearchProjectTestRunMethod);
        apiExecutor.validateResponse(getSearchProjectTestRunMethod, JSONCompareMode.STRICT,
                JsonCompareKeywords.ARRAY_CONTAINS.getKey() + "results");
    }

    @Test
    public void testDeleteTestRunByTestRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.start(projectKey);

        DeleteProjectTestRunByIdMethod deleteProjectTestRunByIdMethod = new DeleteProjectTestRunByIdMethod(testRunId);
        apiExecutor.expectStatus(deleteProjectTestRunByIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(deleteProjectTestRunByIdMethod);
        Assert.assertFalse(projectV1TestRunService.getAllProjectTestRunIds(projectId).contains(testRunId));
    }

    @Test(groups = {"negative"})
    public void testDeleteTestRunByTestRunIdWithNonexistentId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        DeleteProjectTestRunByIdMethod deleteProjectTestRunByIdMethod = new DeleteProjectTestRunByIdMethod(testRunId);
        apiExecutor.expectStatus(deleteProjectTestRunByIdMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(deleteProjectTestRunByIdMethod);
    }

    @Test
    public void testPostProjectIAAnalysis() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        PostProjectIAAnalysisMethod postProjectIAAnalysisMethod = new PostProjectIAAnalysisMethod(testRunId);
        apiExecutor.expectStatus(postProjectIAAnalysisMethod, HTTPStatusCodeType.ACCEPTED);
        apiExecutor.callApiMethod(postProjectIAAnalysisMethod);
    }

    @Test(groups = {"negative"})
    public void testPostProjectIAAnalysisWithNonexistentTestRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        PostProjectIAAnalysisMethod postProjectIAAnalysisMethod = new PostProjectIAAnalysisMethod(testRunId);
        apiExecutor.expectStatus(postProjectIAAnalysisMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(postProjectIAAnalysisMethod);
    }


    @Test(enabled = false)
    public void testBuildProjectTestRun() {
        testRunId = createTestRun(1);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        BuildProjectTestRunMethod buildProjectTestRunMethod = new BuildProjectTestRunMethod(testRunId);
        apiExecutor.expectStatus(buildProjectTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(buildProjectTestRunMethod);
    }

    @Test
    public void testBuildProjectTestRunWithoutJob() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        BuildProjectTestRunMethod buildProjectTestRunMethod = new BuildProjectTestRunMethod(testRunId);
        apiExecutor.expectStatus(buildProjectTestRunMethod, HTTPStatusCodeType.FORBIDDEN);
        apiExecutor.callApiMethod(buildProjectTestRunMethod);
    }

    @Test
    public void testSendTestRunResultsViaEmail() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        String status = testRunServiceAPIImplV1.finishTestRun(testRunId);

        SendTestRunResultsViaEmailMethod sendTestRunResultsViaEmailMethod = new SendTestRunResultsViaEmailMethod(testRunId, TEST_EMAIL);
        apiExecutor.expectStatus(sendTestRunResultsViaEmailMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(sendTestRunResultsViaEmailMethod);
        verifyIfEmailWasDelivered(status);
        EMAIL.deleteMsg(status);
    }

    /**
     * Checks the expected testRun url contains in the last email
     *
     * @param expStatus expected testrun URL to get from email
     * @return
     */
    private boolean verifyIfEmailWasDelivered(String expStatus) {
        final int lastEmailIndex = 0;
        final int emailsCount = 1;
        LOGGER.info("Will get last email from inbox.");
        EMAIL.waitForEmailDelivered(new Date(), expStatus); // decency from connection, wait a little bit
        EmailMsg email = EMAIL.getInbox(emailsCount)[lastEmailIndex];
        return email.getContent().contains(expStatus);
    }

    @Test(groups = {"negative"})
    public void testSendTestRunResultsViaEmailWithNonexistentTestRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        SendTestRunResultsViaEmailMethod sendTestRunResultsViaEmailMethod = new SendTestRunResultsViaEmailMethod(testRunId, TEST_EMAIL);
        apiExecutor.expectStatus(sendTestRunResultsViaEmailMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(sendTestRunResultsViaEmailMethod);
    }

    @Test(groups = {"negative"})
    public void testSendTestRunResultsViaEmailWithRecipients() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        SendTestRunResultsViaEmailMethod sendTestRunResultsViaEmailMethod = new SendTestRunResultsViaEmailMethod(testRunId, TEST_EMAIL);
        sendTestRunResultsViaEmailMethod.removeProperty("recipients");
        apiExecutor.expectStatus(sendTestRunResultsViaEmailMethod, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(sendTestRunResultsViaEmailMethod);
    }

    @Test
    public void testExportTestRunHTML() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        ExportTestRunHTMLMethod exportTestRunHTMLMethod = new ExportTestRunHTMLMethod(testRunId);
        apiExecutor.expectStatus(exportTestRunHTMLMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(exportTestRunHTMLMethod);
    }

    @Test(groups = {"negative"})
    public void testExportTestRunHTMLWithNonexistentTestRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        ExportTestRunHTMLMethod exportTestRunHTMLMethod = new ExportTestRunHTMLMethod(testRunId);
        apiExecutor.expectStatus(exportTestRunHTMLMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(exportTestRunHTMLMethod);
    }

    @Test(enabled = false)
    public void testGetJobParametersMethod() {
        testRunId = createTestRun(1);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);

        GetJobParametersMethod getJobParametersMethod = new GetJobParametersMethod(testRunId);
        apiExecutor.expectStatus(getJobParametersMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getJobParametersMethod);
        apiExecutor.validateResponse(getJobParametersMethod, JSONCompareMode.STRICT,
                JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test(groups = {"negative"})
    public void testGetJobParametersMethodWithNonexistentTestRunId() {
        testRunId = createTestRun(1);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        GetJobParametersMethod getJobParametersMethod = new GetJobParametersMethod(testRunId);
        apiExecutor.expectStatus(getJobParametersMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(getJobParametersMethod);
    }

    @Test
    public void testMarkTestRunAsReviewedMethod() {
        String comment = "New comment_".concat(RandomStringUtils.randomAlphabetic(5));
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        PostMarkTestRunAsReviewedMethod postMarkTestRunAsReviewedMethod = new PostMarkTestRunAsReviewedMethod(testRunId, comment);
        apiExecutor.expectStatus(postMarkTestRunAsReviewedMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postMarkTestRunAsReviewedMethod);

        String actualComment = projectV1TestRunService.getProjectTestRunComment(projectId, testRunId);
        Assert.assertEquals(actualComment, comment, "Comment is not as expected!");
        Boolean isReviewedAct = projectV1TestRunService.getProjectTestRunReviewedIs(projectId, testRunId);
        Assert.assertTrue(isReviewedAct, "Test run is not reviewed!");
    }

    @Test
    public void testCheckIsReviewedIfTestRunIsNotMarkAsReviewed() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        Boolean isReviewedAct = projectV1TestRunService.getProjectTestRunReviewedIs(projectId, testRunId);
        Assert.assertFalse(isReviewedAct, "Test run is reviewed!");
    }

    @Test(groups = {"negative"})
    public void testMarkTestRunAsReviewedTestRunIN_PROGRESS() {
        String comment = "New comment_".concat(RandomStringUtils.randomAlphabetic(5));
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);

        PostMarkTestRunAsReviewedMethod postMarkTestRunAsReviewedMethod = new PostMarkTestRunAsReviewedMethod(testRunId, comment);
        apiExecutor.expectStatus(postMarkTestRunAsReviewedMethod, HTTPStatusCodeType.FORBIDDEN);
        apiExecutor.callApiMethod(postMarkTestRunAsReviewedMethod);
    }

    @Test(groups = {"negative"})
    public void testMarkTestRunAsReviewedMethodWithNonexistentTestRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        PostMarkTestRunAsReviewedMethod getJobParametersMethod =
                new PostMarkTestRunAsReviewedMethod(testRunId, "New comment");
        apiExecutor.expectStatus(getJobParametersMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(getJobParametersMethod);
    }

    @DataProvider(name = "rerunFailuresDataProvider")
    public Object[][] getRerunFailuresFlag() {
        return new Object[][]{{true}, {false}};
    }

    @Test(dataProvider = "rerunFailuresDataProvider", enabled = false)
    public void testRerunProjectTestRunMethod(Boolean rerunFailures) {
        testRunId = createTestRun(1);
        new TestRunServiceAPIImpl().finishTestRun(testRunId);

        RerunProjectTestRunMethod rerunProjectTestRunMethod = new RerunProjectTestRunMethod(testRunId, rerunFailures);
        apiExecutor.expectStatus(rerunProjectTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(rerunProjectTestRunMethod);
        projectV1TestRunService.getAllProjectTestRunIds(1);
    }

    @Test(groups = {"negative"}, enabled = false)
    public void testRerunProjectTestRunMethodWithNonexistentTestRunId() {
        testRunId = createTestRun(1);
        projectV1TestRunService.deleteProjectTestRun(testRunId);
        RerunProjectTestRunMethod rerunProjectTestRunMethod = new RerunProjectTestRunMethod(testRunId, true);
        apiExecutor.expectStatus(rerunProjectTestRunMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(rerunProjectTestRunMethod);
    }

    @Test
    public void testAbortProjectTestRun() {
        String comment = "New comment_".concat(RandomStringUtils.randomAlphabetic(5));
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);

        AbortProjectTestRunMethod abortProjectTestRunMethod = new AbortProjectTestRunMethod(ciRunId, comment);
        apiExecutor.expectStatus(abortProjectTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(abortProjectTestRunMethod);
        apiExecutor.validateResponse(abortProjectTestRunMethod, JSONCompareMode.STRICT,
                JsonCompareKeywords.ARRAY_CONTAINS.getKey());

        String actualComment = projectV1TestRunService.getProjectTestRunComment(projectId, testRunId);
        Assert.assertEquals(actualComment, comment, "Comment is not as expected!");
    }

    @Test(groups = {"negative"})
    public void testAbortProjectTestRunWithNonexistentTestRunId() {
        String comment = "New comment_".concat(RandomStringUtils.randomAlphabetic(5));
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        AbortProjectTestRunMethod abortProjectTestRunMethod = new AbortProjectTestRunMethod(ciRunId, comment);
        apiExecutor.expectStatus(abortProjectTestRunMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(abortProjectTestRunMethod);
    }

    @Test(enabled = false)
    public void testAbortProjectTestRunAfterFinish() {
        String comment = "New comment_".concat(RandomStringUtils.randomAlphabetic(5));
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        String ciRunId = testRunServiceAPIImplV1.getCiRunId(testRunId);

        AbortProjectTestRunMethod abortProjectTestRunMethod = new AbortProjectTestRunMethod(ciRunId, comment);
        apiExecutor.expectStatus(abortProjectTestRunMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(abortProjectTestRunMethod);
        apiExecutor.validateResponse(abortProjectTestRunMethod, JSONCompareMode.STRICT,
                JsonCompareKeywords.ARRAY_CONTAINS.getKey());

        String actualComment = projectV1TestRunService.getProjectTestRunComment(projectId, testRunId);
        Assert.assertEquals(actualComment, comment, "Comment is not as expected!");
    }

    @Test(enabled = false)
    public void testAbortCIJob() {
        testRunId = createTestRun(1);
        String ciRunId = new TestRunServiceAPIImpl().getCiRunId(testRunId);

        AbortCIJobMethod ciJobMethod = new AbortCIJobMethod(ciRunId);
        apiExecutor.expectStatus(ciJobMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(ciJobMethod);
    }

    @Test(groups = {"negative"})
    public void testAbortCIJobWithNonexistentCiRunId() {
        testRunId = createTestRun(1);
        String ciRunId = new TestRunServiceAPIImpl().getCiRunId(testRunId);
        projectV1TestRunService.deleteProjectTestRun(testRunId);

        AbortCIJobMethod ciJobMethod = new AbortCIJobMethod(ciRunId);
        apiExecutor.expectStatus(ciJobMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(ciJobMethod);
    }

    @Test
    public void testAttachTestRunToMilestoneMethod() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        int milestoneId = milestoneService.create(projectId);
        AttachTestRunToMilestoneMethod attachTestRunToMilestoneMethod = new AttachTestRunToMilestoneMethod(testRunId, milestoneId, projectId);
        apiExecutor.expectStatus(attachTestRunToMilestoneMethod, HTTPStatusCodeType.NO_CONTENT);
        apiExecutor.callApiMethod(attachTestRunToMilestoneMethod);

        int actualMilestoneId = projectV1TestRunService.getAttachedToTestRunMilestoneId(testRunId);
        milestoneService.delete(projectId, milestoneId);
        Assert.assertEquals(actualMilestoneId, milestoneId, "Milestone was not attached!");
    }

    @Test
    public void testAttachTestRunToTwoMilestonesMethod() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        int milestoneId = milestoneService.create(projectId);
        projectV1TestRunService.attachToMilestone(testRunId, milestoneId, projectId);

        int milestoneId1 = milestoneService.create(projectId);
        AttachTestRunToMilestoneMethod attachTestRunToMilestoneMethod = new AttachTestRunToMilestoneMethod(testRunId, milestoneId1, projectId);
        apiExecutor.expectStatus(attachTestRunToMilestoneMethod, HTTPStatusCodeType.NO_CONTENT);
        apiExecutor.callApiMethod(attachTestRunToMilestoneMethod);

        int actualMilestoneId = projectV1TestRunService.getAttachedToTestRunMilestoneId(testRunId);
        milestoneService.delete(projectId, milestoneId);
        milestoneService.delete(projectId, milestoneId1);
        Assert.assertEquals(actualMilestoneId, milestoneId1, "Milestone was not attached!");
    }

    @Test
    public void testAttachTestRunToCompletedMilestoneMethod() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        int milestoneId = milestoneService.createCompletedMilestone(projectId);

        AttachTestRunToMilestoneMethod attachTestRunToMilestoneMethod = new AttachTestRunToMilestoneMethod(testRunId, milestoneId, projectId);
        apiExecutor.expectStatus(attachTestRunToMilestoneMethod, HTTPStatusCodeType.FORBIDDEN);
        apiExecutor.callApiMethod(attachTestRunToMilestoneMethod);
    }

    @Test(groups = {"negative"})
    public void testAttachTestRunToMilestoneMethodWithNonexistentTestRunId() {
        testRunId = testRunServiceAPIImplV1.start(APIContextManager.PROJECT_NAME_KEY);
        testRunServiceAPIImplV1.finishTestRun(testRunId);
        testRunServiceAPIImplV1.deleteTestRun(testRunId);

        AttachTestRunToMilestoneMethod attachTestRunToMilestoneMethod = new AttachTestRunToMilestoneMethod(testRunId, 2, 1);
        apiExecutor.expectStatus(attachTestRunToMilestoneMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(attachTestRunToMilestoneMethod);
    }

    @Test
    public void testDeAttachTestRunToMilestoneMethod() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        int milestoneId = milestoneService.create(projectId);
        projectV1TestRunService.attachToMilestone(testRunId, milestoneId, projectId);

        DeleteTestRunFromMilestoneMethod deleteTestRunFromMilestoneMethod = new DeleteTestRunFromMilestoneMethod(testRunId, milestoneId, projectId);
        apiExecutor.expectStatus(deleteTestRunFromMilestoneMethod, HTTPStatusCodeType.NO_CONTENT);
        apiExecutor.callApiMethod(deleteTestRunFromMilestoneMethod);
        milestoneService.delete(projectId,milestoneId);
    }

    @Test(groups = {"negative"})
    public void testDeAttachTestRunToMilestoneMethodWithoutQueryParams() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        int milestoneId = milestoneService.create(projectId);
        projectV1TestRunService.attachToMilestone(testRunId, milestoneId, projectId);

        DeleteTestRunFromMilestoneMethod deleteTestRunFromMilestoneMethod = new DeleteTestRunFromMilestoneMethod(testRunId, milestoneId, projectId);
        AbstractAPIMethodUtil.deleteQuery(deleteTestRunFromMilestoneMethod);
        apiExecutor.expectStatus(deleteTestRunFromMilestoneMethod, HTTPStatusCodeType.BAD_REQUEST);
        apiExecutor.callApiMethod(deleteTestRunFromMilestoneMethod);
    }

    @Test(groups = {"negative"})
    public void testDeAttachTestRunToMilestoneMethodWithNonexistentTestRunId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        int milestoneId = milestoneService.create(projectId);
        projectV1TestRunService.attachToMilestone(testRunId, milestoneId, projectId);

        DeleteTestRunFromMilestoneMethod deleteTestRunFromMilestoneMethod = new DeleteTestRunFromMilestoneMethod(testRunId * (-1), milestoneId, projectId);
        apiExecutor.expectStatus(deleteTestRunFromMilestoneMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(deleteTestRunFromMilestoneMethod);
    }

    @Test
    public void testReAttachTestRunToAnotherMilestone() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        int milestoneId = milestoneService.create(projectId);
        projectV1TestRunService.attachToMilestone(testRunId, milestoneId, projectId);

        int anotherMilestoneId = milestoneService.create(projectId);
        projectV1TestRunService.attachToMilestone(testRunId, anotherMilestoneId, projectId);

        int actualMilestoneId = projectV1TestRunService.getAttachedToTestRunMilestoneId(testRunId);
        Assert.assertEquals(actualMilestoneId, anotherMilestoneId, "Test run was not re-attached!");
    }

    @Test(groups = {"negative"})
    public void testDeAttachTestRunToMilestoneMethodWithNonexistentMilestoneId() {
        String projectKey = projectV1Service.getProjectKeyById(projectId);
        testRunId = testRunServiceAPIImplV1.start(projectKey);
        testRunServiceAPIImplV1.finishTestRun(testRunId);

        int milestoneId = milestoneService.create(projectId);
        projectV1TestRunService.attachToMilestone(testRunId, milestoneId, projectId);

        DeleteTestRunFromMilestoneMethod deleteTestRunFromMilestoneMethod = new DeleteTestRunFromMilestoneMethod(testRunId, milestoneId * (-1), projectId);
        apiExecutor.expectStatus(deleteTestRunFromMilestoneMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(deleteTestRunFromMilestoneMethod);
    }
}
