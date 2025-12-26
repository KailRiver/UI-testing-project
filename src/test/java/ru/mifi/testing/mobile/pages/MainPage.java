package ru.mifi.testing.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By searchContainer = By.id("org.wikipedia.alpha:id/search_container");
    private final By skipButton = By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button");

    public MainPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void skipOnboarding() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(skipButton))
                    .click();
            System.out.println("[INFO] Онбординг пропущен");
        } catch (Exception e) {
            System.out.println("[INFO] Онбординг не отображается");
        }
    }

    public boolean isMainPageOpened() {
        try {
            return wait.until(ExpectedConditions
                            .presenceOfElementLocated(searchContainer))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void openSearch() {
        wait.until(ExpectedConditions
                        .elementToBeClickable(searchContainer))
                .click();
    }
}