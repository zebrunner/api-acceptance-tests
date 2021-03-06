package com.qaprosoft.zafira.api.launcher;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class DeleteLauncherMethod extends ZafiraBaseApiMethodWithAuth {

    public DeleteLauncherMethod(int launcherId) {
        super(null, null, (Properties) null);
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("id", String.valueOf(launcherId));
    }

}
