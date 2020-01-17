package com.qaprosoft.zafira.gui;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import com.qaprosoft.zafira.domain.Route;
import com.qaprosoft.zafira.gui.component.Header;
import com.qaprosoft.zafira.gui.component.Sidebar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public abstract class BasePage extends AbstractPage {

    @FindBy(id = "nav-container")
    private Sidebar sidebar;

    @FindBy(id = "header")
    private Header header;

    @FindBy(css = ".message-toast._success .md-toast-text")
    private ExtendedWebElement successAlert;

    @FindBy(css = ".message-toast._error")
    private ExtendedWebElement errorAlert;

    @FindBy(className = "ajs-warning")
    private ExtendedWebElement warningAlert;

    @FindBy(id = "loading-bar-spinner")
    private ExtendedWebElement loaderSpinner;

    @FindBy(xpath = "//md-progress-linear")
    private ExtendedWebElement progressLinear;

    private final Route route;

    public BasePage(WebDriver driver, Route route, Object... routeSlices) {
        super(driver);
        this.route = route;
        setPageURL(route.getRelativePath(routeSlices));
    }

    public void waitUntilPageIsLoaded() {
        header.getBrandImage().isElementPresent(EXPLICIT_TIMEOUT);
    }

    public Sidebar getSidebar() {
        return sidebar;
    }

    public Header getHeader() {
        return header;
    }

    public ExtendedWebElement getSuccessAlert() {
        return successAlert;
    }

    public String getSuccessAlertText() {
        String result = null;
        boolean alertIsPresent = getSuccessAlert().isElementPresent(EXPLICIT_TIMEOUT);
        if (alertIsPresent) {
            waitForNotEmptyElement(getSuccessAlert(), 0.2, 2);
            result = getSuccessAlert().getText();
        }
        return result;
    }

    private void waitForNotEmptyElement(ExtendedWebElement webElement, double timeout, int attemps) {
        if (webElement.getText().trim().isEmpty() && attemps != 0) {
            pause(timeout);
            attemps --;
            waitForNotEmptyElement(webElement, timeout, attemps);
        }
    }

    public ExtendedWebElement getErrorAlert() {
        return errorAlert;
    }

    public String getErrorAlertText() {
        return errorAlert.getText();
    }

    public ExtendedWebElement getWarningAlert() {
        return warningAlert;
    }

    public String getWarningAlertText() {
        return warningAlert.getText();
    }

    public ExtendedWebElement getLoaderSpinner() {
        return loaderSpinner;
    }

    public ExtendedWebElement getProgressLinear() {
        return progressLinear;
    }

    public Route getRoute() {
        return route;
    }

    public boolean waitProgressLinear() {
        return getProgressLinear().isPresent(1) && getProgressLinear().waitUntilElementDisappear(15);
    }

}
