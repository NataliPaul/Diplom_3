package Pages;

import Model.BrowserSetup;
import Model.MainPage;
import Model.StellarBurgersClient;
import Model.Users;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UserLoginViaPasswordRecoveryFormTest {
    private final StellarBurgersClient model = new StellarBurgersClient();
    private WebDriver driver;
    static MainPage mainPage;
    private final boolean isValid;
    private String accessToken;
    private final BrowserSetup browserSetup = new BrowserSetup();

    private final String email;
    private final String password;
    private final String browserType;

    public UserLoginViaPasswordRecoveryFormTest(String email, String password, boolean isValid, String browserType) {
        this.email = email;
        this.password = password;
        this.isValid = isValid;
        this.browserType = browserType;
    }

    @Parameterized.Parameters(name = "{index}: авторизация с : EMAIL={0}, PASSWORD={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"test-qa99@yandex.ru", "password12399", true, "chrome"},
                {"test-qa991@yandex.ru", "password12399", false, "chrome"},
                {"test-qa99@yandex.ru", "password123991", false, "chrome"},
                {"", "", false, "chrome"},
                {" ", "pass", false, "chrome"},
                {"test-qa99@yandex.ru", "password12399", true, "yandex"},
                {"test-qa991@yandex.ru", "password12399", false, "yandex"},
                {"test-qa99@yandex.ru", "password123991", false, "yandex"},
                {"", "", false, "yandex"},
                {" ", "pass", false, "yandex"},
        });
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

    @Test
    @Step("Проверка входа через 'Войти в аккаунт'")
    public void testLoginViaAccountButton() {
        loginUser(Locators.LOG_IN_TO_ACCOUNT_BUTTON); // Используем метод логина
        checkLoginResult(); // Проверяем результат
    }

    @Test
    @Step("Проверка входа через 'Личный кабинет'")
    public void testLoginViaPersonalCabinetButton() {
        loginUser(Locators.PERSONAL_ACCOUNT_BUTTON); // Используем тот же метод для логина через другую кнопку
        checkLoginResult(); // Проверяем результат
    }

    @Step("Нажатие на кнопку после проверки видимости и кликабельности")
    public void clickIfVisibleAndClickable(By locator) {
        assertTrue("Элемент должен быть видимым: " + locator, mainPage.isElementVisible(locator));
        assertTrue("Элемент должен быть кликабельным: " + locator, mainPage.isClickable(locator));
        mainPage.clickButton(locator);
    }

    @Step("Вход пользователя")
    public void loginUser(By loginButtonLocator) {
        // Нажимаем кнопку для входа
        clickIfVisibleAndClickable(loginButtonLocator);
        // Нажимаем кнопку 'Восстановить пароль'
        clickIfVisibleAndClickable(Locators.RESTORE_PASSWORD_BUTTON);
        // Нажимаем кнопку Войти
        clickIfVisibleAndClickable(Locators.LOGIN_BUTTON_REGISTER);
        // Заполняем поля email и пароль
        assertTrue("Форма авторизации должна быть видимой", mainPage.isElementVisible(Locators.LOGIN_FORM));
        mainPage.fillingOutFormFields(Locators.LOCATOR_EMAIL_USER, email);
        mainPage.fillingOutFormFields(Locators.LOCATOR_PASSWORD_USER, password);
        Allure.step("Заполнение формы: email = " + email + ", пароль = " + password);
        // Нажимаем кнопку Войти
        clickIfVisibleAndClickable(Locators.LOGIN_BUTTON_ACCOUNT);
    }

    @Step("Проверка результата авторизации")
    public void checkLoginResult() {
        boolean isBannerDisplayed = mainPage.isElementVisible(Locators.BANNER_COLLECT_BURGER);
        if (isValid) {
            assertTrue("При удачной авторизации должен появиться заголовок 'Соберите бургер'", isBannerDisplayed);
            Allure.step("Авторизация прошла успешно для пользователя с email: " + email + " и паролем: " + password);
        } else {
            assertFalse("При неудачной авторизации нет заголовка 'Соберите бургер'", isBannerDisplayed);
            Allure.step("Авторизация не удалась для пользователя с email: " + email + " и паролем: " + password);
        }
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
