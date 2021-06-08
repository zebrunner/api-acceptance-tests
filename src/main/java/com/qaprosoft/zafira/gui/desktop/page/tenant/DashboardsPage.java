package com.qaprosoft.zafira.gui.desktop.page.tenant;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.PageOpeningStrategy;
import com.qaprosoft.carina.core.gui.AbstractPage;
import com.qaprosoft.zafira.gui.desktop.component.NavigationMenu;
import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Sleeper;

import java.util.List;

public class DashboardsPage extends AbstractPage {

    @FindBy(id = "nav")
    private NavigationMenu navigationMenu;

    @FindBy(xpath = "//a[@name='dashboardName']")
    private List<ExtendedWebElement> dashboards;

    @FindBy(xpath = "//div[@class='fixed-page-header-container']//div[contains(text(),'Dashboards')]")
    private ExtendedWebElement sectionHeader;

    @FindBy(xpath = "//span[@class='button__text ng-scope']")
    private ExtendedWebElement addDashboardButton;

    @FindBy(xpath = "//form[@name='dashboard_form']")
    private ExtendedWebElement newDashboardForm;

    @FindBy(xpath = "//*[@id='input_12']")
    private ExtendedWebElement search;

    @FindBy(xpath = "//input[@name='title']")
    private ExtendedWebElement dashboardNameInput;

    @FindBy(xpath = "//button[@type='submit']")
    private ExtendedWebElement submitButton;

    public DashboardsPage(WebDriver driver) {
        super(driver);
        setPageOpeningStrategy(PageOpeningStrategy.BY_ELEMENT);
        setUiLoadedMarker(sectionHeader);
    }

    public NavigationMenu getNavigationMenu() {
        return navigationMenu;
    }

    public Dashboard addDashboard(String dashboardName) {
        addDashboardButton.click();
        dashboardNameInput.type(dashboardName);
        submitButton.click();
        return new Dashboard(getDriver());
    }

    public List<ExtendedWebElement> getAllDashboard() throws InterruptedException {
        navigationMenu.toDashboardPage().refresh();
       Thread.sleep(5000);
        return dashboards;
    }
    public boolean isSubmitButtonActive(){
        return submitButton.isClickable(5);
    }
}
