package ru.mifi.testing.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ArticlePage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By articleTitle = By.id("org.wikipedia.alpha:id/view_page_title_text");
    private final By navigateUpButton = AppiumBy.accessibilityId("Navigate up");
    private final By articleContent = By.id("org.wikipedia.alpha:id/page_contents_container");
    private final By closeButton = By.id("org.wikipedia.alpha:id/closeButton");

    public ArticlePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void closePopupIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(closeButton))
                    .click();
        } catch (Exception e) {
            // Попап отсутствует
        }
    }

    public String getArticleTitle() {
        closePopupIfPresent();
        return wait.until(ExpectedConditions
                        .visibilityOfElementLocated(articleTitle))
                .getText();
    }

    public boolean isArticleOpened() {
        try {
            closePopupIfPresent();
            return wait.until(ExpectedConditions
                            .presenceOfElementLocated(articleTitle))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void navigateBack() {
        wait.until(ExpectedConditions
                        .elementToBeClickable(navigateUpButton))
                .click();
    }

    public void scrollDown() {
        WebElement content = wait.until(ExpectedConditions
                .presenceOfElementLocated(articleContent));

        int centerX = content.getRect().x + (content.getSize().width / 2);
        int startY = content.getRect().y + (content.getSize().height * 3 / 4);
        int endY = content.getRect().y + (content.getSize().height / 4);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);

        scroll.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600),
                PointerInput.Origin.viewport(), centerX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(scroll));
    }
}