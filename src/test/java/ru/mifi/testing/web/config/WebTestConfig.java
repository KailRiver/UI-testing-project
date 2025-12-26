package ru.mifi.testing.web.config;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public final class WebTestConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = WebTestConfig.class
                .getClassLoader()
                .getResourceAsStream("web-test.properties")) {

            if (input == null) {
                throw new RuntimeException("Файл web-test.properties не найден");
            }

            properties.load(input);
            System.out.println("[INFO] web-test.properties загружен");

        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки web-test.properties", e);
        }
    }

    private WebTestConfig() {}

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public static Duration getTimeout() {
        int seconds = Integer.parseInt(properties.getProperty("timeout.seconds", "10"));
        return Duration.ofSeconds(seconds);
    }
}