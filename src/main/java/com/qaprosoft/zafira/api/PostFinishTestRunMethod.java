package com.qaprosoft.zafira.api;

import com.qaprosoft.zafira.manager.APIContextManager;

public class PostFinishTestRunMethod extends ZafiraBaseApiMethodWithAuth {

    public PostFinishTestRunMethod(int testRunId) {
        super(null, "api/test_run/_post/rs_to_finish.json", "api/test_run.properties");
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("id", String.valueOf(testRunId));
    }
}
