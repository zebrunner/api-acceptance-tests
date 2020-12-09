package com.qaprosoft.zafira.api.scmAccountController;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class GetAllRepositoriesMethod extends ZafiraBaseApiMethodWithAuth {
    public GetAllRepositoriesMethod() {
        super(null, "api/scmAccountController/_get/rs_for_all_repositories.json", new Properties());
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("scmId", String.valueOf(APIContextManager.EXISTING_SCM_ID));
    }
}