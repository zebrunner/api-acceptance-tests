package com.qaprosoft.zafira.api.testRunController.v1;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;


public class PutFinishTestRunV1Method extends ZafiraBaseApiMethodWithAuth {
    public PutFinishTestRunV1Method(int testRunId, String date) {
        super("api/test_run/v1/_put/rq_for_finish_test_run.json",
                "api/test_run/v1/_put/rs_for_finish_test_run.json", "api/test_run.properties");
        replaceUrlPlaceholder("base_api_url", APIContextManager.BASE_URL);
        replaceUrlPlaceholder("testRunId", String.valueOf(testRunId));
        addProperty("endedAt", date);
    }
}
