package com.qaprosoft.zafira.manager;

import com.jayway.restassured.path.json.JsonPath;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.api.RefreshTokenMethod;
import com.qaprosoft.zafira.enums.HTTPStatusCodeType;
import com.qaprosoft.zafira.service.impl.ExecutionServiceImpl;
import com.qaprosoft.zafira.constant.ConfigConstant;
import com.qaprosoft.zafira.constant.JSONConstant;
import org.apache.log4j.Logger;

public class APIContextManager {
    private static final Logger LOGGER = Logger.getLogger(APIContextManager.class);


    private String accessToken;

    public static String ENV_VALUE = R.CONFIG.get(ConfigConstant.ENV_KEY);
    public static final String BASE_URL = R.CONFIG.get(String.format(ConfigConstant.BASE_URL_KEY, ENV_VALUE));
    public static final String API_URL = R.CONFIG.get(String.format(ConfigConstant.BASE_API_URL_KEY, ENV_VALUE));
    public static final int AUTHOMATION_SERVER_ID_VALUE = R.TESTDATA.getInt(ConfigConstant.AUTHOMATION_SERVER_KEY);
    public static final int SCM_ACCOUNT_TYPE_ID_VALUE = R.TESTDATA.getInt(ConfigConstant.SCM_ACCOUNT_TYPE_KEY);


    public APIContextManager() {

        LOGGER.info("");
        LOGGER.info("The following env will be used: ".concat(ENV_VALUE));
        LOGGER.info("Base API URL: ".concat(API_URL));
        setAccessToken();
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken() {
        ExecutionServiceImpl executor = new ExecutionServiceImpl();
        String rsString = executor.callApiMethod(new RefreshTokenMethod(), HTTPStatusCodeType.OK, false, null);
        accessToken = JsonPath.from(rsString).get(JSONConstant.ACCESS_TOKEN_KEY);
        LOGGER.info("Zafira Access Token: ".concat(accessToken));
    }
}