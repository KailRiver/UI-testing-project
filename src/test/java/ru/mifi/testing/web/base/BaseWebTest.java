package ru.mifi.testing.web.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ru.mifi.testing.web.config.WebTestConfig;

import java.time.Duration;

public abstract class BaseWebTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;

    @BeforeMethod
    public void setUp() {
        baseUrl = WebTestConfig.getBaseUrl();
        String browser = WebTestConfig.getBrowser();

        System.out.println("[INFO] Запуск веб-теста в браузере: " + browser);

        if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }

        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, WebTestConfig.getTimeout());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}