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
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class IngredientSectionSwitchingTest {
    private final StellarBurgersClient model = new StellarBurgersClient();
    private final BrowserSetup browserSetup = new BrowserSetup();
    private WebDriver driver;
    static MainPage mainPage;
    private String accessToken;

    private final String browserType;

    public IngredientSectionSwitchingTest(String browserType) {
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

    @Step("Проверка, что элементы находятся в видимой области окна браузера")
    public void verifyElementsInViewport(WebElement... elements) {
        for (WebElement element : elements) {
            boolean isInViewport = mainPage.isElementInViewport(element);
            assertTrue("Элемент должен находиться в видимой области окна: " + element, isInViewport);
        }
    }

    @Test
    @Step("Проверяем наличие элементов во вкладке Начинка")
    public void filling() {

        // Кликаем по вкладке "Начинки"
        Allure.step("Переходим во вкладку Начинка", () ->
                clickIfVisibleAndClickable(Locators.FILLINGS));
        // Проверяем видимость нескольких элементов
        WebElement meatOfImmortalMollusks = driver.findElement(Locators.MEAT_OF_IMMORTAL_MOLLUSKS);
        WebElement beefMeteorite = driver.findElement(Locators.BEEF_METEORITE);

        verifyElementsInViewport(meatOfImmortalMollusks, beefMeteorite);
    }

    @Test
    @Step("Проверяем наличие элементов во вкладке Соусы")
    public void souse() {

        Allure.step("Переходим во вкладку Соусы", () ->
                clickIfVisibleAndClickable(Locators.SAUCES));
        // Проверяем видимость нескольких элементов
        WebElement spicy = driver.findElement(Locators.SPICY);
        WebElement withSpiker = driver.findElement(Locators.WITH_SPIKES);

        verifyElementsInViewport(spicy, withSpiker);
    }

    @Test
    @Step("Проверяем наличие элементов во вкладке Булки")
    public void buns() {
        // Проверяем видимость нескольких элементов
        WebElement bun = driver.findElement(Locators.BUNS);
        WebElement fluorescentBun = driver.findElement(Locators.FLUORESCENT_BUN);
        WebElement kratoryBun = driver.findElement(Locators.KRATORY_BUN);

        verifyElementsInViewport(bun, fluorescentBun, kratoryBun);
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
