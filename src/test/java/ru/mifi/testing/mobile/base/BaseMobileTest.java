package ru.mifi.testing.mobile.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ru.mifi.testing.mobile.config.MobileTestConfig;

import java.net.URL;
import java.time.Duration;

public abstract class BaseMobileTest {
    protected AndroidDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("[INFO] Настройка мобильного теста");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(MobileTestConfig.getPlatformName())
                .setAutomationName(MobileTestConfig.getAutomationName())
                .setDeviceName(MobileTestConfig.getDeviceName())
                .setPlatformVersion(MobileTestConfig.getPlatformVersion())
                .setAppPackage(MobileTestConfig.getAppPackage())
                .setAppActivity(MobileTestConfig.getAppActivity())
                .setNoReset(MobileTestConfig.isNoReset())
                .setAutoGrantPermissions(MobileTestConfig.isAutoGrantPermissions())
                .setNewCommandTimeout(Duration.ofSeconds(MobileTestConfig.getNewCommandTimeout()));

        System.out.println("[INFO] Подключение к Appium: " + MobileTestConfig.getAppiumServerUrl());

        driver = new AndroidDriver(
                new URL(MobileTestConfig.getAppiumServerUrl()),
                options
        );

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        System.out.println("[INFO] Сессия Appium запущена");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        System.out.println("[INFO] Завершение мобильного теста");
        if (driver != null) {
            driver.quit();
        }
    }
}