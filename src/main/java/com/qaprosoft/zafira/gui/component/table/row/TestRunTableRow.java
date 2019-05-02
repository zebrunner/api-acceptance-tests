package com.qaprosoft.zafira.gui.component.table.row;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class TestRunTableRow extends AbstractUIObject {

    @FindBy(name = "testRunCheckbox")
    private ExtendedWebElement selectedCheckbox;

    @FindBy(className = "test-run-card__title")
    private ExtendedWebElement testSuiteNameLabel;

    @FindBy(css = ".test-run-card__job-name a")
    private ExtendedWebElement jobNameLabel;

    @FindBy(className = "test-run-card__app-name")
    private ExtendedWebElement appNameLabel;

    @FindBy(css = "._env .label")
    private ExtendedWebElement environmentLabel;

    @FindBy(className = "platform-icon")
    private ExtendedWebElement browserIcon;

    @FindBy(css = "._statistics .label-success")
    private ExtendedWebElement passedLabel;

    @FindBy(css = "._statistics .label-danger")
    private ExtendedWebElement failedLabel;

    @FindBy(css = "._statistics .label-warning")
    private ExtendedWebElement skippedLabel;

    @FindBy(css = "._statistics .label-aborted-bg")
    private ExtendedWebElement abortedLabel;

    @FindBy(css = "._statistics .label-info")
    private ExtendedWebElement inProgressLabel;

    @FindBy(className = "time")
    private ExtendedWebElement startedAtLabel;

    @FindBy(css = "._menu button")
    private ExtendedWebElement menuButton;

    public TestRunTableRow(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public ExtendedWebElement getSelectedCheckbox() {
        return selectedCheckbox;
    }

    public void clickSelectedCheckbox() {
        selectedCheckbox.click();
    }

    public ExtendedWebElement getTestSuiteNameLabel() {
        return testSuiteNameLabel;
    }

    public String getTestSuiteNameLableText() {
        return testSuiteNameLabel.getText();
    }

    public ExtendedWebElement getJobNameLabel() {
        return jobNameLabel;
    }

    public String getJobNameLabelText() {
        return jobNameLabel.getText();
    }

    public ExtendedWebElement getAppNameLabel() {
        return appNameLabel;
    }

    public String getAppNameLabelText() {
        return appNameLabel.getText();
    }

    public ExtendedWebElement getEnvironmentLabel() {
        return environmentLabel;
    }

    public String getEnvironmentLabelText() {
        return environmentLabel.getText();
    }

    public ExtendedWebElement getBrowserIcon() {
        return browserIcon;
    }

    public ExtendedWebElement getPassedLabel() {
        return passedLabel;
    }

    public String getPassedLabelText() {
        return passedLabel.getText();
    }

    public ExtendedWebElement getFailedLabel() {
        return failedLabel;
    }

    public String getFailedLabelText() {
        return failedLabel.getText();
    }

    public ExtendedWebElement getSkippedLabel() {
        return skippedLabel;
    }

    public String getSkippedLabelText() {
        return skippedLabel.getText();
    }

    public ExtendedWebElement getAbortedLabel() {
        return abortedLabel;
    }

    public String getAbortedLabelText() {
        return abortedLabel.getText();
    }

    public ExtendedWebElement getInProgressLabel() {
        return inProgressLabel;
    }

    public String getInProgressLabelText() {
        return inProgressLabel.getText();
    }

    public ExtendedWebElement getStartedAtLabel() {
        return startedAtLabel;
    }

    public String getStartedAtLabelText() {
        return startedAtLabel.getText();
    }

    public ExtendedWebElement getMenuButton() {
        return menuButton;
    }

    public void clickMenuButton() {
        menuButton.click();
    }

}
