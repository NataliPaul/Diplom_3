package Pages;

import Model.BrowserSetup;
import Model.MainPage;
import Model.StellarBurgersClient;
import Model.Users;
import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Step;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UserRegistrationSuccessTest {
    private final StellarBurgersClient model = new StellarBurgersClient();
    private WebDriver driver;
    MainPage mainPage;
    private String accessToken;
    private final BrowserSetup browserSetup = new BrowserSetup();

    private final String email;
    private final String password;
    private final String name;
    private final String browserType;

    public UserRegistrationSuccessTest(String email, String password, String name, String browserType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.browserType = browserType;
    }

    @Parameterized.Parameters(name = "{index}: регистрация с : EMAIL={0}, PASSWORD={1}, NAME={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"test-qa3221@yandex.ru", "password3221", "Users3221", "chrome"},
                {"test-qa3221@mail.ru", "password3221", "Users3221", "chrome"},
                {"test-qa3221@yandex.ru", "password3221", "Users3221", "yandex"},
                {"test-qa3221@mail.ru", "password3221", "Users3221", "yandex"},
        });
    }

    //запуск браузера
    @Before
    public void setUp() {
        driver = browserSetup.initializeBrowser(browserType);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        mainPage = new MainPage(driver);
        mainPage.open(Locators.BASE_URL);
    }

    @Step("Нажатие на кнопку после проверки кликабельности")
    public void clickIfVisibleAndClickable(By locator) {
        assertTrue("Элемент должен быть видимым: " + locator, mainPage.isElementVisible(locator));
        assertTrue("Элемент должен быть кликабельным: " + locator, mainPage.isClickable(locator));
        mainPage.clickButton(locator);
    }

    @Test
    @Step("Проверка создания пользователя")
    public void createUserSuccessfully() {
        //кликнуть по кнопке "Войти в аккаунт"
        clickIfVisibleAndClickable(Locators.PERSONAL_ACCOUNT_BUTTON);
        //кликнуть по кнопке Зарегистрироваться
        clickIfVisibleAndClickable(Locators.REGISTER_BUTTON_BEFORE_INPUT);
        assertTrue("Форма регистрации должна быть видимой", mainPage.isElementVisible(Locators.REGISTER_FORM));

        //заполнение поле формы Имя, email, пароль
        mainPage.fillingOutFormFields(Locators.LOCATOR_NAME_USER, name);
        mainPage.fillingOutFormFields(Locators.LOCATOR_EMAIL_USER, email);
        mainPage.fillingOutFormFields(Locators.LOCATOR_PASSWORD_USER, password);
        Allure.step("Заполнение формы: email = " + email + ", пароль = " + password + ", имя = " + name);
        //кликнуть по кнопке Зарегистрироваться
        clickIfVisibleAndClickable(Locators.REGISTER_BUTTON_AFTER_INPUT);

        // Авторизация пользователя через API для получения токена и проверка ответа
        Users user = new Users(email, password);
        ValidatableResponse response = model.loginUsers(user);
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));  //успешный запрос возвращает ok: true
        // Извлечение accessToken для последующего удаления пользователя
        accessToken = response.extract().jsonPath().getString("accessToken");
        assertTrue("Токен доступа не должен быть пустым", accessToken != null && !accessToken.isEmpty());

        //проверка регистрации
        boolean errorDisplayed = mainPage.isElementVisible(Locators.AVAILABILITY_OF_HEADER_INPUT);
        assertTrue("При удачной регистрации должен появиться заголовок 'Вход' ", errorDisplayed);
    }

    //удаление пользователя и закрытие браузера
    @After
    @Step("Удаление пользователя после теста по token: {accessToken} и закрытие браузера")
    public void after() {
         try {
            if (accessToken != null && !accessToken.isEmpty()) {
                ValidatableResponse deleteResponse = model.deleteUser(accessToken);
                deleteResponse.assertThat()
                        .statusCode(202); // Проверяем, что статус код 202 OK
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
