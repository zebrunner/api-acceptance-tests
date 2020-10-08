package com.qaprosoft.zafira.service.impl;

import com.jayway.restassured.path.json.JsonPath;
import com.qaprosoft.zafira.api.testController.PostFinishTestMethod;
import com.qaprosoft.zafira.api.testController.PostRetrieveTestBySearchCriteriaMethod;
import com.qaprosoft.zafira.api.testController.PostStartTestMethod;
import com.qaprosoft.zafira.api.testRunController.GetTestByTestRunIdMethod;
import com.qaprosoft.zafira.api.testRunController.v1.PostStartTestsInTestRunV1Method;
import com.qaprosoft.zafira.constant.JSONConstant;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.service.TestService;
import com.qaprosoft.zafira.service.TestServiceAPIV1;
import org.apache.log4j.Logger;

import java.util.List;

public class TestServiceAPIV1Impl implements TestServiceAPIV1 {
    private static final Logger LOGGER = Logger.getLogger(TestServiceAPIV1Impl.class);
    private ExecutionServiceImpl apiExecutor = new ExecutionServiceImpl();

    public ExecutionServiceImpl getExecutor() {
        return apiExecutor;
    }

    @Override
    public int createTest(int testRunId) {
        PostStartTestsInTestRunV1Method postStartTestsInTestRunV1Method = new PostStartTestsInTestRunV1Method(testRunId);
        apiExecutor.expectStatus(postStartTestsInTestRunV1Method, HTTPStatusCodeType.OK);
        String response = apiExecutor.callApiMethod(postStartTestsInTestRunV1Method);
        return JsonPath.from(response).getInt(JSONConstant.ID_KEY);
    }

}