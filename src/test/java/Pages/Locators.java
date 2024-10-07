package Pages;

import org.openqa.selenium.By;

public class Locators {

    // Base URL
    static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    // Локаторы главной страницы
    //кнопка Входа в личный кабинет
    static final By PERSONAL_ACCOUNT_BUTTON = By.xpath(".//p[text()='Личный Кабинет']");
    //кнопка Входа в "Войти в аккаунт"
    static final By LOG_IN_TO_ACCOUNT_BUTTON = By.xpath(".//button[text()='Войти в аккаунт']");
    //кнопка Конструктор
    static final By CONSTRUCTOR_BUTTON = By.xpath(".//p[text()='Конструктор']");


    // Локаторы входа и регистрации
    //кнопка 'Зарегистрироваться' при входе до ввода данных
    static final By REGISTER_BUTTON_BEFORE_INPUT = By.xpath(".//a[text()='Зарегистрироваться']");
    //кнопка 'Зарегистрироваться' после ввода данных
    static final By REGISTER_BUTTON_AFTER_INPUT = By.xpath(".//button[text()='Зарегистрироваться']");
    //Кнопка Войти через "Войти в аккаунт" и Личный кабинет
    static final By LOGIN_BUTTON_ACCOUNT = By.xpath(".//button[@class ='button_button__33qZ0 button_button_type_primary__1O7Bx button_button_size_medium__3zxIa']");
    //Кнопка Войти в форме регистрации и восстановления пароля
    static final By LOGIN_BUTTON_REGISTER = By.xpath(".//a[@class = 'Auth_link__1fOlj']");

     // Формы
    //для проверки видимости формы для входа
    public static final By LOGIN_FORM
            = By.xpath(".//h2[text()='Вход']/following::form[@class='Auth_form__3qKeq mb-20']");
    //для проверки видимости формы регистрация
    public static final By REGISTER_FORM
            = By.xpath(".//h2[text()='Регистрация']/following::form[@class='Auth_form__3qKeq mb-20']");

    // Локаторы полей ввода
    //поле ввода имя
    static final By LOCATOR_NAME_USER = By.xpath(".//label[text()='Имя']/following-sibling::input");
    //поле ввода email
    static final By LOCATOR_EMAIL_USER = By.xpath(".//label[text()='Email']/following-sibling::input");
    //поле ввода Пароль
    static final By LOCATOR_PASSWORD_USER = By.xpath(".//input[@type='password']");

     // Локаторы для сообщений и валидации
    //для проверки формы Личный кабинет до Авторизации, надпись "Вы - новый пользователь?"
    public static final By YOU_NEW_USER
            = By.xpath(".//p[@class='undefined text text_type_main-default text_color_inactive mb-4']");
    //для проверки формы Личный кабинет до Авторизации, надпись - "Забыли пароль?"
    public static final By FORGOT_PASSWORD
            = By.xpath(".//p[text()='Забыли пароль?']");
    //для проверки формы Личный кабинет после Авторизации, надпись - "В этом разделе вы можете изменить свои персональные данные"
    public static final By CHANGE_PERSONAL_DATA
            = By.xpath(".//p[text()='В этом разделе вы можете изменить свои персональные данные']");

    // Локаторы для проверки полей форм Регистрации и авторизации
    //поле ввода имя для проверки
    static final By TEST_LOCATOR_NAME_USER = By.xpath(".//label[text()='Имя']");
    //поле ввода email для проверки без авторизации
    static final By TEST_LOCATOR_EMAIL_USER = By.xpath(".//label[text()='Email']");
    //поле ввода Логин для проверки после авторизации
    static final By TEST_LOCATOR_LOGIN_AUTH_USER = By.xpath(".//label[text()='Логин']");
    //для проверки содержимого поля Логин после авторизации
    static final By TEST_LOCATOR_EMAIL_AUTH_USER = By.xpath(".//label[text()='Логин']/following-sibling::input");
    //поле ввода Пароль для проверки
    static final By TEST_LOCATOR_PASSWORD_USER = By.xpath(".//label[text()='Пароль']");
    //кнопка Восстановить пароль
    static final By RESTORE_PASSWORD_BUTTON = By.xpath(".//a[text()='Восстановить пароль']");


    // Локаторы валидации
    //ошибка при вводе некорректного пароля
    static final By ERROR_MESSAGE_LOCATOR_PASSWORD =
            By.xpath(".//p[text()='Некорректный пароль']");

    // Локаторы надписей и логотипа
    //для проверки регистрации
    public static final By AVAILABILITY_OF_HEADER_INPUT
            = By.xpath(".//h2[text()='Вход']");
    //для проверки входа
    public static final By BANNER_COLLECT_BURGER
            = By.xpath(".//h1[text()='Соберите бургер']");

    //Кнопки логотип и вкладки
    public static final By LOGO_BUTTON = By.xpath(".//div[@class = 'AppHeader_header__logo__2D0X2']");
    public static final By TEXT_PULL_THE_BUN_UP = By.xpath(".//span[text()='Перетяните булочку сюда (верх)']");
    public static final By TEXT_DRAG_THE_BUN_DOWN = By.xpath(".//span[text()='Перетяните булочку сюда (низ)']");
    public static final By TEXT_BUNS = By.xpath(".//span[text()='Булки']");
    public static final By TEXT_SAUCES = By.xpath(".//span[text()='Соусы']");
    public static final By TEXT_FILLINGS = By.xpath(".//span[text()='Начинки']");

    //кнопка Выйти в Личном кабинете
    static final By ENTRY_BUTTON = By.xpath(".//button[@class='Account_button__14Yp3 text text_type_main-medium text_color_inactive']");

    //кнопки раздела Конструктор
    //вкладка Начинки
    static final By FILLINGS = By.xpath(".//span[text() = 'Начинки']/parent::div");
    //начинка Мясо бессмертных моллюсков Protostomia
    static final By MEAT_OF_IMMORTAL_MOLLUSKS = By.xpath(".//img[@alt='Мясо бессмертных моллюсков Protostomia']");
    //начинка Говяжий метеорит (отбивная)
    static final By BEEF_METEORITE = By.xpath(".//img[@alt='Говяжий метеорит (отбивная)']");

    //вкладка Соусы
    static final By SAUCES = By.xpath(".//span[text() = 'Соусы']/parent::div");
    //соус Spicy-X
    static final By SPICY = By.xpath(".//img[@alt='Соус Spicy-X']");
    //соус с шипами Антарианского плоскоходца
    static final By WITH_SPIKES = By.xpath(".//img[@alt='Соус с шипами Антарианского плоскоходца']");

    //вкладка Булки
    static final By BUNS = By.xpath(".//span[text() = 'Булки']/parent::div");
    //флюорецентная булка R-D3
    static final By FLUORESCENT_BUN = By.xpath(".//img[@alt='Флюоресцентная булка R2-D3']");
    //Краторная булка N-200i
    static final By KRATORY_BUN = By.xpath(".//img[@alt='Краторная булка N-200i']");
}
