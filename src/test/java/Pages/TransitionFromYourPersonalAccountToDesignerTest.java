package Pages;

import Model.MainPage;
import Model.StellarBurgersClient;
import Model.Users;
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
public class TransitionFromYourPersonalAccountToDesignerTest {
    private final StellarBurgersClient model = new StellarBurgersClient();
    private WebDriver driver;
    static MainPage mainPage;
    private String accessToken;

    private final String browserType;

     public TransitionFromYourPersonalAccountToDesignerTest(String browserType) {
        this.browserType = browserType;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"chrome"},
                {"yandex"},
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
    public void transitionFromYourPersonalAccountToTheDesigner() {
        Allure.step("Переходим в Личный кабинет", () ->
                clickIfVisibleAndClickable(Locators.PERSONAL_ACCOUNT_BUTTON));
        Allure.step("Переходим в Конструктор", () ->
                clickIfVisibleAndClickable(Locators.CONSTRUCTOR_BUTTON));

        Allure.step("Проверяем наличие логотипа", () ->
                checkElementVisibilityAndContent(Locators.BANNER_COLLECT_BURGER, Constants.TEXT_BANNER_COLLECT_BURGER));
        Allure.step("Проверяем наличие вкладки Булки", () ->
                checkElementVisibilityAndContent(Locators.TEXT_BUNS, Constants.TEXT_BUNS));
        Allure.step("Проверяем наличие вкладки Соусы", () ->
                checkElementVisibilityAndContent(Locators.TEXT_SAUCES, Constants.TEXT_SAUCES));
        Allure.step("Проверяем наличие вкладки Начинки", () ->
                checkElementVisibilityAndContent(Locators.TEXT_FILLINGS, Constants.TEXT_FILLINGS));
        Allure.step("Проверяем наличие кнопки Перетяните булочку сюда (верх)", () ->
                checkElementVisibilityAndContent(Locators.TEXT_PULL_THE_BUN_UP, Constants.PULL_THE_BUN_UP));
        Allure.step("Проверяем наличие кнопки Перетяните булочку сюда (низ)", () ->
                checkElementVisibilityAndContent(Locators.TEXT_DRAG_THE_BUN_DOWN, Constants.DRAG_THE_BUN_DOWN));
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

