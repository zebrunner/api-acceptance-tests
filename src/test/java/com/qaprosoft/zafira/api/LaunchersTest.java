package com.qaprosoft.zafira.api;

import com.jayway.restassured.path.json.JsonPath;
import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.LauncherMethods.*;
import com.qaprosoft.zafira.api.TestRunMethods.GetBuildNumberMethod;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.constant.JSONConstant;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.manager.APIContextManager;
import com.qaprosoft.zafira.service.impl.JobServiceImpl;
import com.qaprosoft.zafira.service.impl.LauncherServiceImpl;
import com.qaprosoft.zafira.service.impl.PresetServiceImpl;
import org.apache.log4j.Logger;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.List;

public class LaunchersTest extends ZafiraAPIBaseTest {
    private static final Logger LOGGER = Logger.getLogger(LaunchersTest.class);

    @AfterTest
    private void deleteAllLaunchers() {
        LauncherServiceImpl launcherService = new LauncherServiceImpl();
        List<Integer> ids = launcherService.getAll();
        LOGGER.info(String.format("IDs to delete: %s", ids.toString()));
        ids.forEach(launcherService::deleteById);
    }

    @Test
    public void testGetAllLaunchers() {
        int autoServerId = APIContextManager.AUTHOMATION_SERVER_ID_VALUE;
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;
        new LauncherServiceImpl().create(autoServerId, accountTypeId);
        GetAllLaunchersMethod getAllLaunchersMethod = new GetAllLaunchersMethod();
        apiExecutor.expectStatus(getAllLaunchersMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getAllLaunchersMethod);
        apiExecutor.validateResponse(getAllLaunchersMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testCreateLauncher() {
        int jobId = new JobServiceImpl().create();
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;

        PostLauncherMethod postLauncherMethod = new PostLauncherMethod(jobId, accountTypeId);
        apiExecutor.expectStatus(postLauncherMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postLauncherMethod);
        apiExecutor.validateResponse(postLauncherMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testGetLauncherById() {
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;
        int jobId = new JobServiceImpl().create();
        int launcherId = new LauncherServiceImpl().create(jobId, accountTypeId);

        GetLauncherByIdMethod getLauncherByIdMethod = new GetLauncherByIdMethod(launcherId);
        apiExecutor.expectStatus(getLauncherByIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getLauncherByIdMethod);
        apiExecutor.validateResponse(getLauncherByIdMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testUpdateLauncher() {
        String expectedTypeValue = R.TESTDATA.get(ConfigConstant.TYPE_KEY);
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;
        int jobId = new JobServiceImpl().create();
        int launcherId = new LauncherServiceImpl().create(jobId, accountTypeId);

        PutLauncherMethod putLauncherMethod = new PutLauncherMethod(launcherId, expectedTypeValue);
        apiExecutor.expectStatus(putLauncherMethod, HTTPStatusCodeType.OK);
        String putLauncherRs = apiExecutor.callApiMethod(putLauncherMethod);
        apiExecutor.validateResponse(putLauncherMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());

        String type = JsonPath.from(putLauncherRs).get(JSONConstant.TYPE_RS_KEY);
        Assert.assertEquals(type, expectedTypeValue, "Launcher was not updated!");
    }

    @Test
    public void testDeleteNewLauncher() {
        LauncherServiceImpl launcherService = new LauncherServiceImpl();
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;
        int jobId = new JobServiceImpl().create();
        int launcherId = new LauncherServiceImpl().create(jobId, accountTypeId);
        launcherService.deleteById(launcherId);

        GetLauncherByIdMethod getLauncherByIdMethod = new GetLauncherByIdMethod(launcherId);
        apiExecutor.expectStatus(getLauncherByIdMethod, HTTPStatusCodeType.NOT_FOUND);
        apiExecutor.callApiMethod(getLauncherByIdMethod);
    }

    @Test(enabled = false) //TODO: enable this test when jenkins mock container will be up
    public void testBuildJobByWebhook() {
        PresetServiceImpl presetServiceImpl = new PresetServiceImpl();
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;
        int jobId = new JobServiceImpl().create();
        int launcherId = new LauncherServiceImpl().create(jobId, accountTypeId);
        int presetId = presetServiceImpl.create(launcherId);
        String ref = presetServiceImpl.getWebhookUrl(launcherId, presetId);

        PostJobByWebHookMethod postJobByWebHookMethod = new PostJobByWebHookMethod(launcherId, ref);
        apiExecutor.expectStatus(postJobByWebHookMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postJobByWebHookMethod);
    }

    @Test(enabled = false) //TODO: enable this test when jenkins mock container will be up
    public void testBuildJobByLauncher() {
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;

        PostJobByLauncherMethod postJobByLauncherMethod = new PostJobByLauncherMethod(accountTypeId);
        apiExecutor.expectStatus(postJobByLauncherMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postJobByLauncherMethod);
    }

    @Test(enabled = false) //TODO: enable this test when jenkins mock container will be up
    public void testCreateLauncherFromJenkiins() {
        PostLauncherFromJenkinsMethod postLauncherFromJenkinsMethod = new PostLauncherFromJenkinsMethod();
        apiExecutor.expectStatus(postLauncherFromJenkinsMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postLauncherFromJenkinsMethod);
        apiExecutor.validateResponse(postLauncherFromJenkinsMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test(enabled = false) //TODO: enable this test when jenkins mock container will be up
    public void testScanLauncher() {
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;

        PostScanLaucherMethod postScanLaucherMethod = new PostScanLaucherMethod(accountTypeId);
        apiExecutor.expectStatus(postScanLaucherMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(postScanLaucherMethod);
        apiExecutor.validateResponse(postScanLaucherMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());

    }

    @Test(enabled = false) //TODO: enable this test when jenkins mock container will be up
    public void testGetBuildNumber() {
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;
        String queueItemUrl = new LauncherServiceImpl().getQueueItemUrl(accountTypeId);

        GetBuildNumberMethod getBuildNumberMethod = new GetBuildNumberMethod(queueItemUrl);
        apiExecutor.expectStatus(getBuildNumberMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getBuildNumberMethod);
    }

    @Test(enabled = false) //TODO: enable this test when jenkins mock container will be up
    public void testCancelLauncherScanner() {
        LauncherServiceImpl launcherServiceImpl = new LauncherServiceImpl();
        int accountTypeId = APIContextManager.SCM_ACCOUNT_TYPE_ID_VALUE;
        String buildnumber = launcherServiceImpl.getBuildNumber(launcherServiceImpl.getQueueItemUrl(accountTypeId));


        DeleteLauncherScannerMethod deleteLauncherScannerMethod = new DeleteLauncherScannerMethod(buildnumber, accountTypeId);
        apiExecutor.expectStatus(deleteLauncherScannerMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(deleteLauncherScannerMethod);
    }
}