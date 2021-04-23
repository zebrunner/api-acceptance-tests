package com.qaprosoft.zafira.service;

import java.util.List;

public interface ProjectV1TestRunService {


    void deleteProjectTestRun(int testRunId);

    List<Integer> getAllProjectTestRunIds(int projectId);

    String getProjectTestRunById(int testRunId);
}
