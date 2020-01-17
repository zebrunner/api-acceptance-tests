package com.qaprosoft.zafira.api;

import com.qaprosoft.carina.core.foundation.AbstractTest;
import com.qaprosoft.zafira.service.impl.*;
import org.apache.log4j.Logger;

public class ZariraAPIBaseTest extends AbstractTest {
    private static final Logger LOGGER = Logger.getLogger(ZariraAPIBaseTest.class);
    protected ExecutionServiceImpl apiExecutor = new ExecutionServiceImpl();

    protected int createTestRun(String accessToken, int numOfTests) {
        TestRunServiceAPIImpl testRunServiceImpl = new TestRunServiceAPIImpl();
        int testSuiteId = new TestSuiteServiceImpl().create(accessToken);
        int jobId = new JobServiceImpl().create(accessToken);
        int testRunId = testRunServiceImpl.create(accessToken, testSuiteId, jobId);
        int testCaseId = new TestCaseServiceImpl().create(accessToken, testSuiteId);

        for (int i = 0; i < numOfTests; ++i) {
            new TestServiceImpl().create(accessToken, testCaseId, testRunId);
        }
        return testRunId;
    }

}