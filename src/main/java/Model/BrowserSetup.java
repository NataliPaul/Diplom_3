package Model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class BrowserSetup {
    private WebDriver driver;

    public WebDriver initializeBrowser(String browserType) {
        ChromeOptions options = new ChromeOptions();
        if (browserType.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C://CDriver/chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browserType.equals("yandex")) {
            System.setProperty("webdriver.chrome.driver", "C://CDriver/yandexdriver.exe");
            driver = new ChromeDriver(options); // Запускаем ChromeDriver с параметрами Яндекс
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }
}
