package com.qaprosoft.zafira.api.artifactsController;

import com.qaprosoft.zafira.api.ZafiraBaseApiMethodWithAuth;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.io.File;

public class PostTestArtifactMethod extends ZafiraBaseApiMethodWithAuth {
    public PostTestArtifactMethod(int testRunId, int testId, File uploadFile) {
        super(null, "api/artifacts_controller/_get/artifacts/rs.json", "api/file_util.properties");
        replaceUrlPlaceholder("base_api_url", APIContextManager.BASE_URL);
        replaceUrlPlaceholder("testRunId", String.valueOf(testRunId));
        replaceUrlPlaceholder("testId", String.valueOf(testId));
        setHeaders("Content-Type=multipart/form-data");
        request.multiPart("file", uploadFile, "image/jpeg");
    }
}

