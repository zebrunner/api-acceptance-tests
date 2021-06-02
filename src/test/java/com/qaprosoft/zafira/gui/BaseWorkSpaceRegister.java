package com.qaprosoft.zafira.gui;

import com.qaprosoft.carina.core.foundation.AbstractTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.zafira.constant.WebConstant;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.BeforeSuite;

public class BaseWorkSpaceRegister extends AbstractTest {

    protected String orgName;
    protected String ownerName;
    protected String tenantEmail;

    @BeforeSuite
    public void generateNames(){
        StringBuilder tenantBuilder = new StringBuilder(R.TESTDATA.get(WebConstant.TENANT_EMAIL));
        tenantBuilder.insert(tenantBuilder.indexOf("@"), "+" + RandomStringUtils.randomNumeric(1));
        tenantEmail = tenantBuilder.toString();
        orgName = RandomStringUtils.randomAlphabetic(7);
        ownerName = RandomStringUtils.randomAlphabetic(8);
    }
}
