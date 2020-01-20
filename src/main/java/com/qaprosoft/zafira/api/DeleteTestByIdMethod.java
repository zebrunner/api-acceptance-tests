package com.qaprosoft.zafira.api;

import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class DeleteTestByIdMethod extends ZafiraBaseApiMethodWithAuth {

    public DeleteTestByIdMethod(int testId) {
        super(null, null, (Properties) null);
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("id", String.valueOf(testId));
    }
}
