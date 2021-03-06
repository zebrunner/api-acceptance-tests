package com.qaprosoft.zafira.api.testRunController;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class GetBuildNumberMethod extends ZafiraBaseApiMethodWithAuth {

    public GetBuildNumberMethod(String queueItemUrl) {
        super(null, null, new Properties());
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("queueItemUrl", queueItemUrl);
        replaceUrlPlaceholder("automationServerId", String.valueOf(APIContextManager.AUTHOMATION_SERVER_ID_VALUE));
    }
}
