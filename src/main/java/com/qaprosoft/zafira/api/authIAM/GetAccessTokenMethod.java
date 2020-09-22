package com.qaprosoft.zafira.api.authIAM;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;
import com.qaprosoft.zafira.manager.APIContextManager;

import java.util.Properties;

public class GetAccessTokenMethod extends AbstractApiMethodV2 {
    public GetAccessTokenMethod() {
        super(null, "api/authIAM/_get/rs.json", (Properties) null);
        replaceUrlPlaceholder("base_api_url", APIContextManager.API_URL);
        setHeaders("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJhZG1pbiIsInBlcm1pc3Npb25zIjpbIklOVklURV9VU0VSUyIsIklOVk9JQ0VTIiwiTU9ESUZZX0RBU0hCT0FSRFMiLCJNT0RJRllfSU5URUdSQVRJT05TIiwiTU9ESUZZX0lOVklUQVRJT05TIiwiTU9ESUZZX0xBVU5DSEVSUyIsIk1PRElGWV9QUk9KRUNUUyIsIk1PRElGWV9URVNUUyIsIk1PRElGWV9URVNUX1JVTlMiLCJNT0RJRllfVVNFUlMiLCJNT0RJRllfVVNFUl9HUk9VUFMiLCJNT0RJRllfV0lER0VUUyIsIlBBWU1FTlRfTUVUSE9EUyIsIlJFRlJFU0hfVE9LRU4iLCJURVNUX1JVTlNfQ0kiLCJVUERBVEVfU0VUVElOR1MiLCJWSUVXX0hJRERFTl9EQVNIQk9BUkRTIiwiVklFV19JTlRFR1JBVElPTlMiLCJWSUVXX0xBVU5DSEVSUyIsIlZJRVdfVVNFUlMiLCJiaWxsaW5nOmludm9pY2VzOnJlYWQiLCJiaWxsaW5nOnBheW1lbnQtbWV0aG9kczpyZWFkIiwiY29uZmlndXJhdGlvbjpzc286c2FtbCIsImlhbTpncm91cHM6ZGVsZXRlIiwiaWFtOmdyb3VwczpyZWFkIiwiaWFtOmdyb3Vwczp1cGRhdGUiLCJpYW06aW52aXRhdGlvbnM6ZGVsZXRlIiwiaWFtOmludml0YXRpb25zOnJlYWQiLCJpYW06aW52aXRhdGlvbnM6dXBkYXRlIiwiaWFtOnVzZXJzOnJlYWQiLCJpYW06dXNlcnM6dXBkYXRlIiwicmVwb3J0aW5nOmRhc2hib2FyZHM6ZGVsZXRlIiwicmVwb3J0aW5nOmRhc2hib2FyZHM6cmVhZC1oaWRkZW4iLCJyZXBvcnRpbmc6ZGFzaGJvYXJkczp1cGRhdGUiLCJyZXBvcnRpbmc6aW50ZWdyYXRpb25zOnJlYWQiLCJyZXBvcnRpbmc6aW50ZWdyYXRpb25zOnVwZGF0ZSIsInJlcG9ydGluZzpsYXVuY2hlcnM6ZGVsZXRlIiwicmVwb3J0aW5nOmxhdW5jaGVyczpyZWFkIiwicmVwb3J0aW5nOmxhdW5jaGVyczp1cGRhdGUiLCJyZXBvcnRpbmc6cHJvamVjdHM6ZGVsZXRlIiwicmVwb3J0aW5nOnByb2plY3RzOnVwZGF0ZSIsInJlcG9ydGluZzpzZXR0aW5nczp1cGRhdGUiLCJyZXBvcnRpbmc6dGVzdC1ydW5zOmRlbGV0ZSIsInJlcG9ydGluZzp0ZXN0LXJ1bnM6cmVhZCIsInJlcG9ydGluZzp0ZXN0LXJ1bnM6dXBkYXRlIiwicmVwb3J0aW5nOnRlc3Qtc2Vzc2lvbnM6dG9rZW46cmVmcmVzaCIsInJlcG9ydGluZzp0ZXN0czp1cGRhdGUiLCJyZXBvcnRpbmc6d2lkZ2V0czpkZWxldGUiLCJyZXBvcnRpbmc6d2lkZ2V0czp1cGRhdGUiXSwiZmlyc3RfbG9naW4iOmZhbHNlLCJ0ZW5hbnQiOiJhdXRvbWF0aW9uIiwiZXhwIjoxNjAwODI0MjU0fQ.Z2sMUeYpq_UOu-dJ0Bv3Y4xrT6fbf5BZWyCucYyxpD5RSD2VBuRzs5vPKsxu4XtYUIEYTBacz0mvAkmSSjoYYw");

    }
}
