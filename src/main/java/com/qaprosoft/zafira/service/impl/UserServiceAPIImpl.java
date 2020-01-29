package com.qaprosoft.zafira.service.impl;

import com.qaprosoft.zafira.api.UserMethods.PostSearchUserByCriteriaMethod;
import com.qaprosoft.zafira.api.UserMethods.PutCreateUserMethod;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.service.UserServiceAPI;

public class UserServiceAPIImpl implements UserServiceAPI {
    private ExecutionServiceImpl apiExecutor = new ExecutionServiceImpl();

    @Override
    public String getUserByCriteria(String query) {
        PostSearchUserByCriteriaMethod postSearchUserByCriteriaMethod = new PostSearchUserByCriteriaMethod(query);
        apiExecutor.expectStatus(postSearchUserByCriteriaMethod, HTTPStatusCodeType.OK);
        return apiExecutor.callApiMethod(postSearchUserByCriteriaMethod);
    }

    @Override
    public String getUserId(String username) {
        PutCreateUserMethod putCreateUserMethod = new PutCreateUserMethod(username);
        apiExecutor.expectStatus(putCreateUserMethod, HTTPStatusCodeType.OK);
        return apiExecutor.callApiMethod(putCreateUserMethod);
    }
}
