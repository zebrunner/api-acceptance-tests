package com.qaprosoft.zafira.api.testRunController.v1;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;
import java.util.UUID;


public class PostStartTestRunV1Method extends ZafiraBaseApiMethodWithAuth {
        public PostStartTestRunV1Method() {
        super("api/test_run/v1/_post/rq.json", "api/test_run/v1/_post/rs.json", "api/test_run.properties");
        replaceUrlPlaceholder("base_api_url", APIContextManager.BASE_URL);
        replaceUrlPlaceholder("Project", APIContextManager.PROJECT_KEY);
        addProperty("uuid", UUID.randomUUID());
    }
}
