package com.qaprosoft.zafira.api.testRunController;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;

public class PostAbortTestRunMethod extends ZafiraBaseApiMethodWithAuth {
    public PostAbortTestRunMethod(int testRunId, String ciRunId) {
        super("api/test_run/_post/rq_to_abort.json", "api/test_run/_post/rs_to_abort.json", "api/test_run.properties");
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("id", String.valueOf(testRunId));
        replaceUrlPlaceholder("ciRunId", ciRunId);
    }
}
