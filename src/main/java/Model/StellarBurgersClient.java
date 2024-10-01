package Model;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class StellarBurgersClient {

    private static final String BASE_URL ="https://stellarburgers.nomoreparties.site";

    //Регистрация пользователя
    public ValidatableResponse registerUsers(Users user) {
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .post("/api/auth/register")
                .then()
                .log()
                .all();
    }

    //Авторизация пользователя
    public ValidatableResponse loginUsers(Users user) {
        return given()
            .baseUri(BASE_URL)
            .header("Content-Type", "application/json")
            .body(user)
            .post("/api/auth/login")
            .then()
            .log().all()
            .statusCode(200);
    }

    public ValidatableResponse deleteUser(String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", token) // Добавляем токен в заголовок
                .delete("/api/auth/user")
                .then();
    }
}
