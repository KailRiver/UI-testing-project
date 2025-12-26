package ru.mifi.testing.mobile.config;

import java.io.InputStream;
import java.util.Properties;

public class MobileTestConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream is = MobileTestConfig.class
                .getClassLoader()
                .getResourceAsStream("mobile-test.properties")) {

            if (is == null) {
                throw new RuntimeException("mobile-test.properties не найден");
            }

            properties.load(is);
            System.out.println("[INFO] Конфигурация мобильных тестов загружена");

        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки mobile-test.properties", e);
        }
    }

    public static String getAppiumServerUrl() {
        return properties.getProperty("mobile.appium.server.url");
    }

    public static String getPlatformName() {
        return properties.getProperty("mobile.platformName");
    }

    public static String getPlatformVersion() {
        return properties.getProperty("mobile.platformVersion");
    }

    public static String getDeviceName() {
        return properties.getProperty("mobile.deviceName");
    }

    public static String getAutomationName() {
        return properties.getProperty("mobile.automationName");
    }

    public static String getAppPackage() {
        return properties.getProperty("mobile.appPackage");
    }

    public static String getAppActivity() {
        return properties.getProperty("mobile.appActivity");
    }

    public static boolean isNoReset() {
        return Boolean.parseBoolean(properties.getProperty("mobile.noReset", "false"));
    }

    public static boolean isAutoGrantPermissions() {
        return Boolean.parseBoolean(properties.getProperty("mobile.autoGrantPermissions", "true"));
    }

    public static int getNewCommandTimeout() {
        return Integer.parseInt(properties.getProperty("mobile.newCommandTimeout", "180"));
    }
}