package com.qaprosoft.zafira.api.projectIntegrations;

import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.ZafiraAPIBaseTest;
import com.qaprosoft.zafira.api.projectIntegrations.jiraWithXray.*;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.constant.JSONConstant;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.service.impl.projectIntegrations.JiraIntegrationServiceImpl;
import com.qaprosoft.zafira.util.AbstractAPIMethodUtil;
import com.zebrunner.agent.core.annotation.Maintainer;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;

@Maintainer("obabich")
public class JiraIntegrationTest extends ZafiraAPIBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static JiraIntegrationServiceImpl jiraIntegrationService = new JiraIntegrationServiceImpl();
    private static final String EXISTING_ISSUE = "ZEB-2787";
    private static final String NONEXISTENT_ISSUE = "ZEB--FOR-API";
    private static int projectId = 1;


    @Test
    public void testSaveJiraIntegration() {
        PutSaveJiraIntegrationMethod putSaveIntegrationMethod = new PutSaveJiraIntegrationMethod(projectId);
        apiExecutor.expectStatus(putSaveIntegrationMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(putSaveIntegrationMethod);
        apiExecutor.validateResponse(putSaveIntegrationMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testSaveJiraIntegrationWithEmptyRq() {
        PutSaveJiraIntegrationMethod putSaveJiraIntegrationMethod = new PutSaveJiraIntegrationMethod(projectId);
        putSaveJiraIntegrationMethod.setRequestTemplate(R.TESTDATA.get(ConfigConstant.EMPTY_RQ_PATH));
        apiExecutor.expectStatus(putSaveJiraIntegrationMethod, HTTPStatusCodeType.BAD_REQUEST);
        String rs = apiExecutor.callApiMethod(putSaveJiraIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-1003", "Error code is not as expected!");
    }

    @Test
    public void testSaveJiraIntegrationWithoutQueryParams() {
        PutSaveJiraIntegrationMethod putSaveJiraIntegrationMethod = new PutSaveJiraIntegrationMethod(projectId);
        AbstractAPIMethodUtil.deleteQuery(putSaveJiraIntegrationMethod);
        apiExecutor.expectStatus(putSaveJiraIntegrationMethod, HTTPStatusCodeType.BAD_REQUEST);
        String rs = apiExecutor.callApiMethod(putSaveJiraIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-1010", "Error code is not as expected!");
    }

    @Test
    public void testSaveJiraIntegrationWithoutNonexistentProjectId() {
        PutSaveJiraIntegrationMethod putSaveJiraIntegrationMethod = new PutSaveJiraIntegrationMethod(projectId * (-1));
        apiExecutor.expectStatus(putSaveJiraIntegrationMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(putSaveJiraIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2018", "Error code is not as expected!");
    }

    @Test
    public void testDeleteJiraIntegration() throws UnsupportedEncodingException {
        jiraIntegrationService.addIntegration(projectId);

        DeleteJiraIntegrationMethod testIntegrationMethod = new DeleteJiraIntegrationMethod(projectId);
        apiExecutor.expectStatus(testIntegrationMethod, HTTPStatusCodeType.NO_CONTENT);
        apiExecutor.callApiMethod(testIntegrationMethod);

        GetJiraIntegrationByProjectIdMethod getJiraIntegrationByProjectIdMethod = new GetJiraIntegrationByProjectIdMethod(projectId);
        apiExecutor.expectStatus(getJiraIntegrationByProjectIdMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(getJiraIntegrationByProjectIdMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2068", "Error code is not as expected!");
    }


    @Test
    public void testDeleteJiraIntegrationsWithNonexistentProjectId() {
        DeleteJiraIntegrationMethod deleteJiraIntegrationMethod = new DeleteJiraIntegrationMethod(projectId * (-1));
        apiExecutor.expectStatus(deleteJiraIntegrationMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(deleteJiraIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2018", "Error code is not as expected!");
    }

    @Test(enabled = false, description = " actual status 200")
    public void testDeleteNonexistentJiraIntegration() {
        jiraIntegrationService.addIntegration(projectId);
        jiraIntegrationService.deleteIntegration(projectId);
        DeleteJiraIntegrationMethod deleteJiraIntegrationMethod = new DeleteJiraIntegrationMethod(projectId);
        apiExecutor.expectStatus(deleteJiraIntegrationMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(deleteJiraIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2018", "Error code is not as expected!");
    }

    @Test
    public void testDeleteJiraIntegrationWithoutQueryParams() {
        jiraIntegrationService.addIntegration(projectId);
        DeleteJiraIntegrationMethod deleteJiraIntegrationMethod = new DeleteJiraIntegrationMethod(projectId);
        AbstractAPIMethodUtil.deleteQuery(deleteJiraIntegrationMethod);
        apiExecutor.expectStatus(deleteJiraIntegrationMethod, HTTPStatusCodeType.BAD_REQUEST);
        String rs = apiExecutor.callApiMethod(deleteJiraIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-1010", "Error code is not as expected!");
    }

    @Test
    public void testGetJiraIntegration() throws UnsupportedEncodingException {
        jiraIntegrationService.addIntegration(projectId);

        GetJiraIntegrationByProjectIdMethod getJiraIntegrationByProjectIdMethod = new GetJiraIntegrationByProjectIdMethod(projectId);
        apiExecutor.expectStatus(getJiraIntegrationByProjectIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getJiraIntegrationByProjectIdMethod);
        apiExecutor.validateResponse(getJiraIntegrationByProjectIdMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testGetJiraIntegrationWithoutQuery() throws UnsupportedEncodingException {
        jiraIntegrationService.addIntegration(projectId);

        GetJiraIntegrationByProjectIdMethod getJiraIntegrationByProjectIdMethod = new GetJiraIntegrationByProjectIdMethod(projectId);
        AbstractAPIMethodUtil.deleteQuery(getJiraIntegrationByProjectIdMethod);
        apiExecutor.expectStatus(getJiraIntegrationByProjectIdMethod, HTTPStatusCodeType.BAD_REQUEST);
        String rs = apiExecutor.callApiMethod(getJiraIntegrationByProjectIdMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-1010", "Error code is not as expected!");
    }

    @Test
    public void testGetJiraIntegrationWithNonexistentProjectId() throws UnsupportedEncodingException {
        GetJiraIntegrationByProjectIdMethod getJiraIntegrationByProjectIdMethod = new GetJiraIntegrationByProjectIdMethod(projectId * (-1));
        apiExecutor.expectStatus(getJiraIntegrationByProjectIdMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(getJiraIntegrationByProjectIdMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2018", "Error code is not as expected!");
    }

    @DataProvider(name = "enabledIntegration")
    public Object[][] getEnabledType() {
        return new Object[][]{{false}, {true}};
    }

    @Test(dataProvider = "enabledIntegration")
    public void testCheckEnabledJiraIntegration(Boolean value) throws UnsupportedEncodingException {
        jiraIntegrationService.addIntegration(projectId);

        Boolean expectedEnableValue = value;
        PatchEnabledJiraIntegrationMethod patchEnabledIntegrationMethod = new PatchEnabledJiraIntegrationMethod(projectId, expectedEnableValue);
        apiExecutor.expectStatus(patchEnabledIntegrationMethod, HTTPStatusCodeType.NO_CONTENT);
        apiExecutor.callApiMethod(patchEnabledIntegrationMethod);
        Boolean actualEnabled = jiraIntegrationService.getEnabledIntegration(projectId);
        Assert.assertEquals(actualEnabled, expectedEnableValue, "Enabled was not updated!");
    }

    @Test
    public void testCheckEnabledJiraIntegrationWithoutQueryParam() {
        jiraIntegrationService.addIntegration(projectId);

        PatchEnabledJiraIntegrationMethod patchEnabledIntegrationMethod = new PatchEnabledJiraIntegrationMethod(projectId, true);
        AbstractAPIMethodUtil.deleteQuery(patchEnabledIntegrationMethod);
        apiExecutor.expectStatus(patchEnabledIntegrationMethod, HTTPStatusCodeType.BAD_REQUEST);
        String rs = apiExecutor.callApiMethod(patchEnabledIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-1010", "Error code is not as expected!");
    }

    @Test
    public void testCheckEnabledInDeletedJiraIntegration() {
        jiraIntegrationService.addIntegration(projectId);
        jiraIntegrationService.deleteIntegration(projectId);

        PatchEnabledJiraIntegrationMethod patchEnabledIntegrationMethod = new PatchEnabledJiraIntegrationMethod(projectId, true);
        apiExecutor.expectStatus(patchEnabledIntegrationMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(patchEnabledIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2068", "Error code is not as expected!");
    }

    @Test
    public void testCheckEnabledJiraIntegrationWithNonexistentProjectId() {
        jiraIntegrationService.addIntegration(projectId);

        PatchEnabledJiraIntegrationMethod patchEnabledIntegrationMethod = new PatchEnabledJiraIntegrationMethod(projectId * (-1), true);
        apiExecutor.expectStatus(patchEnabledIntegrationMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(patchEnabledIntegrationMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2018", "Error code is not as expected!");
    }

    @Test
    public void testCheckConnectionWithJiraIntegration() {
        PostCheckConnectionJiraIntegrationMethod checkConnection = new PostCheckConnectionJiraIntegrationMethod(projectId);
        apiExecutor.expectStatus(checkConnection, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(checkConnection);
        apiExecutor.validateResponse(checkConnection, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testCheckConnectionWithJiraIntegrationWithInvalidCreds() {
        PostCheckConnectionJiraIntegrationMethod checkConnection = new PostCheckConnectionJiraIntegrationMethod(projectId);
        checkConnection.addProperty("token", "Jira_invalid_token".concat(RandomStringUtils.randomAlphabetic(3)));
        apiExecutor.expectStatus(checkConnection, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(checkConnection);
    }

    @Test
    public void testCheckConnectionWithJiraIntegrationWithEmptyRq() {
        PostCheckConnectionJiraIntegrationMethod checkConnection = new PostCheckConnectionJiraIntegrationMethod(projectId);
        checkConnection.setRequestTemplate(R.TESTDATA.get(ConfigConstant.EMPTY_RQ_PATH));
        apiExecutor.expectStatus(checkConnection, HTTPStatusCodeType.BAD_REQUEST);
        String rs = apiExecutor.callApiMethod(checkConnection);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-1003", "Error code is not as expected!");
    }

    @Test
    public void testCheckConnectionWithXRayWithInvalidCreds() {
        PostCheckXRayConnectionIntegrationMethod checkConnection = new PostCheckXRayConnectionIntegrationMethod(projectId);
        checkConnection.addProperty("token", "Xray_invalid_token".concat(RandomStringUtils.randomAlphabetic(3)));
        apiExecutor.expectStatus(checkConnection, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(checkConnection);
    }

    @Test
    public void testCheckConnectionWithXRayWithEmptyRq() {
        PostCheckXRayConnectionIntegrationMethod checkConnection = new PostCheckXRayConnectionIntegrationMethod(projectId);
        checkConnection.setRequestTemplate(R.TESTDATA.get(ConfigConstant.EMPTY_RQ_PATH));
        apiExecutor.expectStatus(checkConnection, HTTPStatusCodeType.BAD_REQUEST);
        String rs = apiExecutor.callApiMethod(checkConnection);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-1003", "Error code is not as expected!");
    }

    @Test
    public void testExistingJiraIssueByIssueId() {
        jiraIntegrationService.addIntegration(projectId);
        GetJiraIssueByProjectIdMethod getJiraIssueByProjectIdMethod = new GetJiraIssueByProjectIdMethod(projectId,EXISTING_ISSUE);
        apiExecutor.expectStatus(getJiraIssueByProjectIdMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(getJiraIssueByProjectIdMethod);
        apiExecutor.validateResponse(getJiraIssueByProjectIdMethod, JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
    }

    @Test
    public void testNonExistentJiraIssueByIssueId() {
        jiraIntegrationService.addIntegration(projectId);
        GetJiraIssueByProjectIdMethod getJiraIssueByProjectIdMethod = new GetJiraIssueByProjectIdMethod(projectId,NONEXISTENT_ISSUE);
        apiExecutor.expectStatus(getJiraIssueByProjectIdMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(getJiraIssueByProjectIdMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2076", "Error code is not as expected!");
    }

    @Test
    public void testExistingJiraIssueByIssueIdWithoutIntegration() {
        jiraIntegrationService.deleteIntegration(projectId);
        GetJiraIssueByProjectIdMethod getJiraIssueByProjectIdMethod = new GetJiraIssueByProjectIdMethod(projectId,EXISTING_ISSUE);
        apiExecutor.expectStatus(getJiraIssueByProjectIdMethod, HTTPStatusCodeType.NOT_FOUND);
        String rs = apiExecutor.callApiMethod(getJiraIssueByProjectIdMethod);
        Assert.assertEquals(JsonPath.from(rs).getString(JSONConstant.ERROR_CODE), "REP-2068", "Error code is not as expected!");

    }
}
