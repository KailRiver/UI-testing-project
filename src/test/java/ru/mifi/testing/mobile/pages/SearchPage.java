package ru.mifi.testing.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By searchInput = By.id("org.wikipedia.alpha:id/search_src_text");
    private final By searchResults = By.id("org.wikipedia.alpha:id/page_list_item_title");
    private final By searchCloseButton = By.id("org.wikipedia.alpha:id/search_close_btn");

    public SearchPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void searchFor(String query) {
        wait.until(ExpectedConditions
                        .elementToBeClickable(searchInput))
                .sendKeys(query);
    }

    public List<WebElement> getSearchResults() {
        return wait.until(ExpectedConditions
                .presenceOfAllElementsLocatedBy(searchResults));
    }

    public void openFirstResult() {
        getSearchResults().get(0).click();
    }

    public void clearSearch() {
        wait.until(ExpectedConditions
                        .elementToBeClickable(searchCloseButton))
                .click();
    }

    public boolean hasResults() {
        return !getSearchResults().isEmpty();
    }
}