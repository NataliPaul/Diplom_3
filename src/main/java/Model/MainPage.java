package Model;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {

    private final WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    //метод открытия страницы браузера
    public void open(String url) {
        driver.get(url);
    }

    //метод клик по кнопке
    public void clickButton(By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.findElement(locator).click();
    }

    // Метод для проверки кликабельности элемента
    public boolean isClickable(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    //метод заполнение полей
    public WebElement fillingOutFormFields(By locator, String inputOption) {
        driver.findElement(locator).click();
        WebElement inputElement = driver.findElement(locator);
        inputElement.sendKeys(inputOption);
        return inputElement;
    }

    //проверка видимости элемента
    public boolean isElementVisible(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    //Получение текста элемента по локатору
    public String getElementText(By locator) {
        WebElement element = driver.findElement(locator);
        return element.getText();
    }

    //Получение текста элемента по атрибуту "value"
    public String getElementValue(By locator) {
        WebElement inputElement = driver.findElement(locator);
        // Получаем значение атрибута 'value'
        return inputElement.getAttribute("value");
    }

    private static int counter = 1;
    public boolean isElementInViewport(WebElement element) {
    return new WebDriverWait(driver, Duration.ofSeconds(1))
                        .until(
                                driver -> {
                                    Rectangle rect = element.getRect();
                                    Dimension windowSize = driver.manage().window().getSize();

                                    System.out.println("проверка номер: " + counter++);
                                    System.out.println("rect.getX(): " + rect.getX());
                                    System.out.println("rect.getY(): " + rect.getY());
                                    System.out.println("rect.getWidth(): " + rect.getWidth());
                                    System.out.println("rect.getHeight(): " + rect.getHeight());
                                    System.out.println("windowSize.getWidth(): " + windowSize.getWidth());
                                    System.out.println("windowSize.getHeight(): " + windowSize.getHeight() + "\n");

                                    // условие, которое проверяем внутри явного ожидания
                                    return rect.getX() >= 0
                                            && rect.getY() >= 0
                                            && rect.getX() + rect.getWidth() <= windowSize.getWidth()
                                            && rect.getY() + rect.getHeight() <= windowSize.getHeight();
                                });

    }
}


