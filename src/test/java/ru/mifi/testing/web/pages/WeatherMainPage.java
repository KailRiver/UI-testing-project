package ru.mifi.testing.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;  // Добавляем этот импорт
import java.util.regex.Matcher;  // Добавляем этот импорт

public class WeatherMainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Актуальные локаторы на основе отладки
    private final By body = By.tagName("body");
    private final By searchInput = By.cssSelector("input[type='search']");
    private final By searchSuggestions = By.cssSelector("ul.Suggest_list__2n_Ua li");
    private final By temperatureElement = By.xpath("//div[contains(@class, 'temp fact__temp')]//span[contains(@class, 'temp__value')]");
    private final By weatherDescription = By.cssSelector("div.fact__condition");
    private final By cityTitle = By.cssSelector("h1.title");
    private final By forecastItems = By.cssSelector("div.forecast-briefly");

    public WeatherMainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void openPage(String url) {
        System.out.println("[PAGE] Opening: " + url);
        driver.get(url);
        waitForPageLoad();
    }

    private void waitForPageLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(body));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getPageUrl() {
        return driver.getCurrentUrl();
    }

    public String getCityFromTitle() {
        String title = getPageTitle();
        if (title.contains("Погода в")) {
            return title.split("Погода в")[1].split("—")[0].trim();
        }
        return "Неизвестный город";
    }

    public String getTemperature() {
        try {
            // Способ 1: Ищем по новым локаторам
            List<WebElement> elements = driver.findElements(By.cssSelector("span.temp__value, div.temp, [class*='temp']"));

            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    String text = element.getText().trim();
                    // Ищем текст с числом и градусом
                    if (text.matches(".*[+-]?\\d+.*°.*") ||
                            text.matches(".*°.*[+-]?\\d+.*") ||
                            (text.contains("°") && text.length() < 10)) {
                        System.out.println("[DEBUG] Found temperature: " + text);
                        return text;
                    }
                }
            }

            // Способ 2: Ищем через XPath
            elements = driver.findElements(By.xpath("//*[contains(text(),'°C') or contains(text(),'°')]"));
            for (WebElement element : elements) {
                String text = element.getText().trim();
                if (!text.isEmpty() && text.length() < 15) {
                    System.out.println("[DEBUG] Found via XPath: " + text);
                    return text;
                }
            }

            // Способ 3: Ищем в заголовке или метаданных
            String pageSource = driver.getPageSource();
            if (pageSource.contains("°C") || pageSource.contains("°")) {
                // Пытаемся извлечь температуру из исходного кода
                Pattern pattern = Pattern.compile("[+-]?\\d+\\s*°");
                Matcher matcher = pattern.matcher(pageSource);
                if (matcher.find()) {
                    return matcher.group();
                }
            }

            return "Температура: данные недоступны";
        } catch (Exception e) {
            return "Ошибка получения температуры: " + e.getMessage();
        }
    }

    public String getWeatherDescriptionText() {
        try {
            List<WebElement> elements = driver.findElements(weatherDescription);
            if (!elements.isEmpty()) {
                return elements.get(0).getText().trim();
            }

            // Ищем альтернативные описания
            elements = driver.findElements(By.cssSelector("div[class*='condition'], div[class*='description']"));
            for (WebElement element : elements) {
                String text = element.getText().trim();
                if (!text.isEmpty() && text.length() < 50) {
                    return text;
                }
            }

            return "Описание не доступно";
        } catch (Exception e) {
            return "Ошибка получения описания";
        }
    }

    public boolean hasSearchInput() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(searchInput)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void searchForCityWithSuggest(String city) {
        try {
            WebElement search = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
            search.clear();
            search.sendKeys(city);

            // Ждем появления подсказок
            Thread.sleep(1000);

            // Выбираем первую подсказку
            List<WebElement> suggestions = driver.findElements(searchSuggestions);
            if (!suggestions.isEmpty()) {
                suggestions.get(0).click();
                waitForPageLoad();
            } else {
                // Если нет подсказок, просто отправляем форму
                search.submit();
                waitForPageLoad();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("[ERROR] Search failed: " + e.getMessage());
        }
    }

    public void searchForCityDirect(String city) {
        // Прямой переход на страницу города
        String cityUrl = getCityUrl(city);
        openPage(cityUrl);
    }

    private String getCityUrl(String city) {
        switch (city.toLowerCase()) {
            case "москва":
            case "moscow":
                return "https://yandex.ru/pogoda/moscow";
            case "санкт-петербург":
            case "saint petersburg":
            case "петербург":
                return "https://yandex.ru/pogoda/saint-petersburg";
            case "казань":
            case "kazan":
                return "https://yandex.ru/pogoda/kazan";
            default:
                return "https://yandex.ru/pogoda";
        }
    }

    public String getCityFromUrl() {
        String url = getPageUrl();
        if (url.contains("/moscow")) return "Москва";
        if (url.contains("/saint-petersburg")) return "Санкт-Петербург";
        if (url.contains("/kazan")) return "Казань";
        return getCityFromTitle();
    }

    public int getForecastItemsCount() {
        try {
            List<WebElement> items = driver.findElements(forecastItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getTemperatureWithRetry() {
        // Пробуем несколько способов получить температуру
        String[] temperatureSelectors = {
                "//div[contains(@class, 'temp')]//span[contains(@class, 'value')]",
                "//span[contains(@class, 'temp__value')]",
                "//div[contains(@class, 'fact__temp')]",
                "//*[contains(@class, 'temp') and contains(text(), '+')]",
                "//*[contains(@class, 'temp') and contains(text(), '-')]",
                "//*[contains(text(), '°C')]",
                "//*[contains(text(), '°') and string-length(text()) < 10]"
        };

        for (String selector : temperatureSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(selector));
                for (WebElement element : elements) {
                    if (element.isDisplayed()) {
                        String text = element.getText().trim();
                        if (!text.isEmpty() && (text.contains("°") || text.matches(".*[0-9].*"))) {
                            System.out.println("[DEBUG] Temperature found with selector '" + selector + "': " + text);
                            return text;
                        }
                    }
                }
            } catch (Exception e) {
                // Продолжаем пробовать другие селекторы
            }
        }

        // Если не нашли, возвращаем сообщение
        return "Температурные данные обновляются";
    }

    // Утилитарный метод для безопасного ожидания
    public void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}