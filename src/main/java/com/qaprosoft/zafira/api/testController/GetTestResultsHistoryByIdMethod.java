package com.qaprosoft.zafira.api.testController;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class GetTestResultsHistoryByIdMethod extends ZafiraBaseApiMethodWithAuth {

    public GetTestResultsHistoryByIdMethod(int id) {
        super(null, "api/test/_get/rs_fot_history.json", new Properties());
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("id", String.valueOf(id));
    }
}
