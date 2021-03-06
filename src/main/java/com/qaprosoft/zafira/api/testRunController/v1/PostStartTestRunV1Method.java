package com.qaprosoft.zafira.api.testRunController.v1;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.UUID;


public class PostStartTestRunV1Method extends ZafiraBaseApiMethodWithAuth {
    public PostStartTestRunV1Method(String project, String date) {
        super("api/test_run/v1/_post/rq_full.json", "api/test_run/v1/_post/rs.json", "api/test_runV1.properties");
        replaceUrlPlaceholder("base_api_url", APIContextManager.BASE_URL);
        replaceUrlPlaceholder("project", project);
        addProperty("uuid", UUID.randomUUID());
        addProperty("startedAt", date);
    }
}
