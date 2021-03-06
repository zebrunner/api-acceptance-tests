package com.qaprosoft.zafira.service.impl;

import com.qaprosoft.zafira.api.user.DeleteUserFromGroupMethod;
import com.qaprosoft.zafira.api.user.v1.*;
import com.qaprosoft.zafira.constant.JSONConstant;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.service.UserV1ServiceAPI;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;

public class UserV1ServiceAPIImpl implements UserV1ServiceAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ExecutionServiceImpl apiExecutor = new ExecutionServiceImpl();

    @Override
    public int getUserId(String username) {
        GetUserByUsernameV1Method getUserByUsernameV1Method = new GetUserByUsernameV1Method(username);
        apiExecutor.expectStatus(getUserByUsernameV1Method, HTTPStatusCodeType.OK);
        String response = apiExecutor.callApiMethod(getUserByUsernameV1Method);
        return JsonPath.from(response).getInt(JSONConstant.ID_KEY);
    }

    @Override
    public String create(String username, String password, String email) {
        PostUserMethodV1 postUserMethodV1 = new PostUserMethodV1(username, password, email);
        apiExecutor.expectStatus(postUserMethodV1, HTTPStatusCodeType.CREATED);
        String response = apiExecutor.callApiMethod(postUserMethodV1);
        return JsonPath.from(response).getString(JSONConstant.USERNAME_KEY);
    }

    @Override
    public int createForProject() {
        String username = "TEST_".concat(RandomStringUtils.randomAlphabetic(10));
        String password = "TEST_".concat(RandomStringUtils.randomAlphabetic(10));
        String email = "TEST_".concat(RandomStringUtils.randomAlphabetic(15)).concat("@gmail.com");
        PostUserMethodV1 postUserMethodV1 = new PostUserMethodV1(username, password, email);
        apiExecutor.expectStatus(postUserMethodV1, HTTPStatusCodeType.CREATED);
        String response = apiExecutor.callApiMethod(postUserMethodV1);
        return JsonPath.from(response).getInt(JSONConstant.ID_KEY);
    }

    @Override
    public int createAndGetId(String username, String password, String email) {
        PostUserMethodV1 postUserMethodV1 = new PostUserMethodV1(username, password, email);
        apiExecutor.expectStatus(postUserMethodV1, HTTPStatusCodeType.CREATED);
        String response = apiExecutor.callApiMethod(postUserMethodV1);
        return JsonPath.from(response).getInt(JSONConstant.ID_KEY);
    }

    @Override
    public void deleteUserFromGroup(int groupId, int userId) {
        DeleteUserFromGroupMethod deleteUserFromGroupMethod = new DeleteUserFromGroupMethod(groupId, userId);
        apiExecutor.expectStatus(deleteUserFromGroupMethod, HTTPStatusCodeType.OK);
        apiExecutor.callApiMethod(deleteUserFromGroupMethod);
    }

    @Override
    public List<Integer> getAllUserGroupIds(int userId) {
        GetUserByIdV1Method getUserByIdV1Method = new GetUserByIdV1Method(userId);
        apiExecutor.expectStatus(getUserByIdV1Method, HTTPStatusCodeType.OK);
        String response = apiExecutor.callApiMethod(getUserByIdV1Method);
        return JsonPath.from(response).getList(JSONConstant.ALL_USERS_GROUPS_ID);
    }

    @Override
    public String getEmail(String username) {
        GetUserByUsernameV1Method getUserByUsernameV1Method = new GetUserByUsernameV1Method(username);
        apiExecutor.expectStatus(getUserByUsernameV1Method, HTTPStatusCodeType.OK);
        String response = apiExecutor.callApiMethod(getUserByUsernameV1Method);
        return JsonPath.from(response).getString(JSONConstant.EMAIL_KEY);
    }

    @Override
    public String getStatus(String username) {
        GetUserByUsernameV1Method getUserByUsernameV1Method = new GetUserByUsernameV1Method(username);
        apiExecutor.expectStatus(getUserByUsernameV1Method, HTTPStatusCodeType.OK);
        String response = apiExecutor.callApiMethod(getUserByUsernameV1Method);
        return JsonPath.from(response).getString(JSONConstant.STATUS_KEY);
    }

    @Override
    public void deleteUserById(int userId) {
        DeleteUserByIdV1Method getUserByCriteriaV1Method = new DeleteUserByIdV1Method(userId);
        apiExecutor.callApiMethod(getUserByCriteriaV1Method);
    }

    @Override
    public String getUserById(int userId) {
        GetUserByIdV1Method getUserByIdV1Method = new GetUserByIdV1Method(userId);
        apiExecutor.expectStatus(getUserByIdV1Method, HTTPStatusCodeType.OK);
        String response = apiExecutor.callApiMethod(getUserByIdV1Method);
        return response;
    }

    @Override
    public List<Integer> getAllUserIds() {
        GetUserByCriteriaV1Method getUserByCriteriaV1Method = new GetUserByCriteriaV1Method("", "");
        apiExecutor.expectStatus(getUserByCriteriaV1Method, HTTPStatusCodeType.OK);
        String rs = apiExecutor.callApiMethod(getUserByCriteriaV1Method);
        List<Integer> userIds = JsonPath.from(rs).getList("results.id");
        userIds.sort(Comparator.naturalOrder());
        LOGGER.info("Existing userIds: " + userIds);
        return userIds;
    }

}
