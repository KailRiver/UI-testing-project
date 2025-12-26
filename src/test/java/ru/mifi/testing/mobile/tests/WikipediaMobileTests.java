package ru.mifi.testing.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.mifi.testing.mobile.base.BaseMobileTest;
import ru.mifi.testing.mobile.pages.ArticlePage;
import ru.mifi.testing.mobile.pages.MainPage;
import ru.mifi.testing.mobile.pages.SearchPage;

import java.util.List;

public class WikipediaMobileTests extends BaseMobileTest {

    @Test(priority = 1, description = "Запуск приложения и проверка главного экрана")
    public void testAppLaunch() {
        MainPage mainPage = new MainPage(driver);
        mainPage.skipOnboarding();

        Assert.assertTrue(mainPage.isMainPageOpened(),
                "Главный экран должен отображаться после запуска");
    }

    @Test(priority = 2, description = "Поиск статьи")
    public void testSearchArticle() {
        MainPage mainPage = new MainPage(driver);
        SearchPage searchPage = new SearchPage(driver);

        mainPage.skipOnboarding();
        mainPage.openSearch();
        searchPage.searchFor("Java");

        List results = searchPage.getSearchResults();
        Assert.assertTrue(searchPage.hasResults(),
                "Должны отображаться результаты поиска");
        Assert.assertTrue(results.size() > 0,
                "Количество результатов должно быть больше 0");
    }

    @Test(priority = 3, description = "Открытие статьи из результатов поиска")
    public void testOpenArticleFromSearch() {
        MainPage mainPage = new MainPage(driver);
        SearchPage searchPage = new SearchPage(driver);
        ArticlePage articlePage = new ArticlePage(driver);

        mainPage.skipOnboarding();
        mainPage.openSearch();
        searchPage.searchFor("Appium");
        searchPage.openFirstResult();

        Assert.assertTrue(articlePage.isArticleOpened(),
                "Статья должна открыться");

        String title = articlePage.getArticleTitle();
        Assert.assertNotNull(title, "Заголовок статьи должен отображаться");
        Assert.assertFalse(title.isEmpty(), "Заголовок не должен быть пустым");
    }

    @Test(priority = 4, description = "Прокрутка статьи")
    public void testScrollArticle() {
        MainPage mainPage = new MainPage(driver);
        SearchPage searchPage = new SearchPage(driver);
        ArticlePage articlePage = new ArticlePage(driver);

        mainPage.skipOnboarding();
        mainPage.openSearch();
        searchPage.searchFor("Software testing");
        searchPage.openFirstResult();

        // Запоминаем заголовок перед прокруткой
        String initialTitle = articlePage.getArticleTitle();

        // Прокручиваем
        articlePage.scrollDown();

        // Проверяем, что статья все еще открыта
        Assert.assertTrue(articlePage.isArticleOpened(),
                "Статья должна оставаться открытой после прокрутки");

        // Навигация назад
        articlePage.navigateBack();
    }

    @Test(priority = 5, description = "Очистка поиска")
    public void testClearSearch() {
        MainPage mainPage = new MainPage(driver);
        SearchPage searchPage = new SearchPage(driver);

        mainPage.skipOnboarding();
        mainPage.openSearch();
        searchPage.searchFor("Test automation");

        Assert.assertTrue(searchPage.hasResults(),
                "Должны быть результаты поиска");

        searchPage.clearSearch();

        // После очистки результаты должны исчезнуть
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List results = searchPage.getSearchResults();
        Assert.assertTrue(results.isEmpty() || results.size() == 0,
                "Результаты поиска должны очиститься");
    }
}