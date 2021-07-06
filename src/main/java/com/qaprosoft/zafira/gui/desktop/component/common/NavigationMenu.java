package com.qaprosoft.zafira.gui.desktop.component.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;
import com.qaprosoft.zafira.constant.WebConstant;
import com.qaprosoft.zafira.gui.desktop.page.tenant.MainDashboardsPage;
import com.qaprosoft.zafira.gui.desktop.page.tenant.MembersPage;
import com.qaprosoft.zafira.gui.desktop.page.tenant.TestRunsPage;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

//id = "nav-container"
public class NavigationMenu extends AbstractUIObject {
    @FindBy(xpath = ".//li[contains(@class,'nav-item dashboards')]")
    private ExtendedWebElement dashboardButton;

    @FindBy(xpath = ".//li[contains(@class,'nav-item tests')]")
    private ExtendedWebElement testRunsButton;

    @FindBy(xpath = ".//li[contains(@class,'nav-item integrations')]")
    private ExtendedWebElement integrationsButton;

    @FindBy(xpath = ".//li[contains(@class,'nav-item users')]")
    private ExtendedWebElement membersButton;

    @FindBy(xpath = ".//div[@class='project__selected ng-binding']")
    private ExtendedWebElement projectKey;

    @FindBy(xpath = "//.div[@class='main-nav__button-icon-wrapper']")
    private ExtendedWebElement extendSideBarButton;

    @FindBy(xpath = ".//span[contains(@class,'profile-photo')]")
    private ExtendedWebElement profilePhoto;

    public NavigationMenu(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public MainDashboardsPage toMainDashboardPage() {
        dashboardButton.click();
        return new MainDashboardsPage(getDriver());
    }

    public TestRunsPage toTestRunsPage() {
        waitUntil(ExpectedConditions.elementToBeClickable(testRunsButton.getElement()), 3);
        testRunsButton.click();
        return new TestRunsPage(getDriver());
    }

    public void toIntegrationPage() {
        integrationsButton.click();
    }

    public MembersPage toMembersPage() {
        membersButton.click();
        return new MembersPage(driver);
    }

    public String getProjectKey() {
        return projectKey.getText().trim();
    }

    public boolean isProjectPhotoPresent() {
        return profilePhoto.isVisible();
    }

    public boolean waitUntilProjectKeyToBE(String key) {
        return waitUntil(ExpectedConditions.textToBePresentInElement(projectKey.getElement(), key), WebConstant.TIME_TO_LOAD_PAGE);
    }

}
