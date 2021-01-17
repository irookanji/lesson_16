package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {

  @BeforeEach
  void beforeEach() {
    RestAssured.filters(new AllureRestAssured());
    RestAssured.baseURI = "https://reqres.in/api";
  }

  @Test
  @DisplayName("Successful Single User GET Response")
  void successSingleUserTest() {
    given()
        .when()
        .get("/users/2")
        .then()
        .statusCode(200)
        .log()
        .body()
        .body("data.email", is("janet.weaver@reqres.in"))
        .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"));
  }

  @Test
  @DisplayName("Successful Login")
  void successLoginTest() {
      String data = "{\n" +
              "    \"email\": \"eve.holt@reqres.in\",\n" +
              "    \"password\": \"cityslicka\"\n" +
              "}";

      given()
              .contentType(ContentType.JSON)
              .body(data)
              .when()
              .post("/login")
              .then()
              .statusCode(200)
              .log().body()
              .body("token", is(notNullValue()));
  }
}
