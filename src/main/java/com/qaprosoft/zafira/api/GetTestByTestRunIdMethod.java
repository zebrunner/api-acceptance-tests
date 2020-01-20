package com.qaprosoft.zafira.api;

import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class GetTestByTestRunIdMethod extends ZafiraBaseApiMethodWithAuth {

    public GetTestByTestRunIdMethod(int testRunId) {
        super(null, "api/test_run/_get/rs_for_getTest.json", (Properties) null);
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("id", String.valueOf(testRunId));
    }
}
