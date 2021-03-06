package com.qaprosoft.zafira.service;

import java.util.List;

public interface TestRunServiceAPI {

    int create(int testSuiteId, int jobId);

    String getCiRunId(int testSuiteId);

    String finishTestRun(int testRunId);

    List<Integer> getAllTestRunIds();

    void deleteById(int testRunId);

    void reviewTestRun(int testRunId);

    List<Integer> getAll(String searchCriteriaType, int searchCriteriaId);

    String getTestRunStatus(int testSuiteId);

    String createTestRunAndReturnCiRunId(int testSuiteId, int jobId);

    void createAIAnalysis(int testRunId);
}
