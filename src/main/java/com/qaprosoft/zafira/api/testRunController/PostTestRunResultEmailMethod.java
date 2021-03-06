package com.qaprosoft.zafira.api.testRunController;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.manager.APIContextManager;

public class PostTestRunResultEmailMethod extends ZafiraBaseApiMethodWithAuth {

    public PostTestRunResultEmailMethod(int testRunId) {
        super("api/test_run/_post/rq_for_email.json", null, "api/test_run.properties");
        String testEmail = R.TESTDATA.get(ConfigConstant.TEST_EMAIL_KEY);
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        replaceUrlPlaceholder("id", String.valueOf(testRunId));
        addProperty("testEmail", testEmail);
    }
}
