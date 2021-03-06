package com.qaprosoft.zafira.api;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class ZafiraBaseApiMethodWithAuth extends AbstractApiMethodV2 {

    private static String cacheAccessToken = "";

    public ZafiraBaseApiMethodWithAuth(String rqPath, String rsPath, String propertiesPath) {
        super(rqPath, rsPath, propertiesPath);
        setAuthHeaders();
    }

    public ZafiraBaseApiMethodWithAuth(String rqPath, String rsPath, String propertiesPath,String token) {
        super(rqPath, rsPath, propertiesPath);
        setNewAuthTokenHeaders(token);
    }

    public ZafiraBaseApiMethodWithAuth(String rqPath, String rsPath, Properties properties) {
        super(rqPath, rsPath, properties);
        setAuthHeaders();
    }

    public ZafiraBaseApiMethodWithAuth(String rqPath, String rsPath) {
        super(rqPath, rsPath);
        setAuthHeaders();
    }

    public ZafiraBaseApiMethodWithAuth() {
        setAuthHeaders();
    }

    public ZafiraBaseApiMethodWithAuth(String token) {
        setNewAuthTokenHeaders(token);
    }

    protected void setAuthHeaders() {
        if (cacheAccessToken.isEmpty()) {
            cacheAccessToken = new APIContextManager().getAccessToken();
        }
        setHeaders("Authorization=Bearer " + cacheAccessToken);
    }

    protected void setNewAuthTokenHeaders(String token) {
        setHeaders("Authorization=Bearer " + token);
    }
}
