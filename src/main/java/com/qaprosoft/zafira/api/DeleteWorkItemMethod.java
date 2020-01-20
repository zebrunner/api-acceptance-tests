package com.qaprosoft.zafira.api;

import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class DeleteWorkItemMethod extends ZafiraBaseApiMethodWithAuth {
    public DeleteWorkItemMethod(int testId, int workItemId) {
        super(null, null, (Properties) null);
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("testId", String.valueOf(testId));
        replaceUrlPlaceholder("workItemId", String.valueOf(workItemId));
    }
}
