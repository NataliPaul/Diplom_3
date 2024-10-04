package Pages;

import Model.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UserProfileNavigationBeforeAuthorizationTest {
    private final StellarBurgersClient model = new StellarBurgersClient();
    private final BrowserSetup browserSetup = new BrowserSetup();
    private WebDriver driver;
    static MainPage mainPage;
    private String accessToken;

    private final String browserType;

     public UserProfileNavigationBeforeAuthorizationTest(String browserType) {
        this.browserType = browserType;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return TestDataProvider.getBrowserTypes();
    }

    //запуск браузера
    @Before
    public void setUp() {
        driver = browserSetup.initializeBrowser(browserType);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        mainPage = new MainPage(driver);
        mainPage.open(Locators.BASE_URL);

        // Регистрация пользователя через API для получения токена и проверка ответа
        Users user = new Users(Constants.EMAIL, Constants.PASSWORD, Constants.NAME);
        ValidatableResponse response = model.registerUsers(user);
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));  // успешный запрос возвращает ok: true
        // Извлечение accessToken для последующего удаления пользователя
        accessToken = response.extract().jsonPath().getString("accessToken");
        assertTrue("Токен доступа не должен быть пустым", accessToken != null && !accessToken.isEmpty());

    }

    @Step("Нажатие на кнопку после проверки видимости и кликабельности")
    public void clickIfVisibleAndClickable(By locator) {
        assertTrue("Элемент должен быть видимым: " + locator, mainPage.isElementVisible(locator));
        assertTrue("Элемент должен быть кликабельным: " + locator, mainPage.isClickable(locator));
        mainPage.clickButton(locator);
    }

    @Step("Проверка видимости поля и соответствия содержимого")
    private void checkElementVisibilityAndContent(By locator, String expectedContent) {
        assertTrue("Элемент должен быть видимым: " + locator, mainPage.isElementVisible(locator));

        // Получаем текст элемента и проверяем его содержимое
        String actualContent = mainPage.getElementText(locator);
        Allure.step("Проверка текста в элементе: ожидается" + expectedContent, () ->
            assertEquals("Текст элемента не совпадает", expectedContent, actualContent));
    }

    @Test
    @Step("Проверка перехода в 'Личный кабинет'")
    public void goToYourPersonalAccount() {
        Allure.step("Переходим в Личный кабинет", () ->
            clickIfVisibleAndClickable(Locators.PERSONAL_ACCOUNT_BUTTON));
        Allure.step("Проверяем поле Email", () ->
            checkElementVisibilityAndContent(Locators.TEST_LOCATOR_EMAIL_USER, Constants.TEXT_EMAIL));
        Allure.step("Проверяем поле Пароль", () ->
            checkElementVisibilityAndContent(Locators.TEST_LOCATOR_PASSWORD_USER, Constants.TEXT_PASSWORD));
        Allure.step("Проверяем наличие кнопки Войти", () ->
            checkElementVisibilityAndContent(Locators.LOGIN_BUTTON_ACCOUNT, Constants.TEXT_LOGIN_BUTTON));
        Allure.step("Проверяем наличие текста 'Вы — новый пользователь? Зарегистрироваться'", () ->
            checkElementVisibilityAndContent(Locators.YOU_NEW_USER, Constants.TEXT_YOU_NEW_USER));
        Allure.step("Проверяем наличие текста 'Забыли пароль? Восстановить пароль'", () ->
            checkElementVisibilityAndContent(Locators.FORGOT_PASSWORD, Constants.TEXT_FORGOT_PASSWORD));
    }

    //закрытие браузера
    @After
    @Step("Закрытие браузера")
    public void after() {
        if (accessToken != null && !accessToken.isEmpty()) {
            ValidatableResponse deleteResponse = model.deleteUser(accessToken);
            deleteResponse.assertThat()
                    .statusCode(202); // Проверяем, что статус код 202 OK
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
