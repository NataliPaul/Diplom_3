package Pages;

import Model.MainPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
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

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UserRegistrationWithIncorrectPasswordTest {
    private WebDriver driver;
    MainPage mainPage;

    private final String email;
    private final String password;
    private final String name;
    private final String browserType;

    public UserRegistrationWithIncorrectPasswordTest(String email, String password, String name, String browserType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.browserType = browserType;
    }

    @Parameterized.Parameters(name = "{index}: регистрация с : EMAIL={0}, PASSWORD={1}, NAME={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"test-qa3221@yandex.ru", "pass", "Users3221", "chrome"},
                {"test-qa3221@mail.ru", "3221", "Users3221", "chrome"},
                {"test-qa3221@mail.ru", "32213", "Users3221", "chrome"},
                {"test-qa3221@gmail.com", " ", "Users3221", "chrome"},
                {"test-qa3221@yandex.ru", "pass", "Users3221", "yandex"},
                {"test-qa3221@mail.ru", "3221", "Users3221", "yandex"},
                {"test-qa3221@mail.ru", "32213", "Users3221", "yandex"},
                {"test-qa3221@gmail.com", " ", "Users3221", "yandex"},
        });
    }

    //запуск браузера
    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (browserType.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C://CDriver/chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browserType.equals("yandex")) {
            System.setProperty("webdriver.chrome.driver", "C://CDriver/yandexdriver.exe");
            driver = new ChromeDriver(options); // Запускаем ChromeDriver с параметрами Яндекс
        }
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
        //заполнение поле формы Имя, email, пароль
        mainPage.fillingOutFormFields(Locators.LOCATOR_NAME_USER, name);
        mainPage.fillingOutFormFields(Locators.LOCATOR_EMAIL_USER, email);
        mainPage.fillingOutFormFields(Locators.LOCATOR_PASSWORD_USER, password);
        Allure.step("Заполнение формы: email = " + email + ", пароль = " + password + ", имя = " + name);
        //кликнуть по кнопке Зарегистрироваться
        clickIfVisibleAndClickable(Locators.REGISTER_BUTTON_AFTER_INPUT);

        //проверка регистрации
        boolean errorDisplayed = mainPage.isElementVisible(Locators.ERROR_MESSAGE_LOCATOR_PASSWORD);
        assertTrue("При некорректном пароле должно появиться сообщение 'Некорректный пароль'", errorDisplayed);

    }

    //удаление пользователя и закрытие браузера
    @After
    @Step("Удаление пользователя после теста по token: {accessToken}")
    public void after() {
        if (driver != null) {
                driver.quit();
            }
    }
}
