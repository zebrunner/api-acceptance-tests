package com.qaprosoft.zafira.service;


public interface TestServiceAPIV1 {

    int createTest(int testRunId);

    String finishTestAsResult(int testRunId, int testId, String result);

    int createTestHeadless(int testRunId);

    void deleteTest(int testRunId, int id);
}
