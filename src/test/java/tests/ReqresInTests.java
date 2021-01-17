package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {

  @BeforeEach
  void beforeEach() {
    RestAssured.filters(new AllureRestAssured(), new RequestLoggingFilter(), new ResponseLoggingFilter());
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
  @DisplayName("Single User Not Found")
  void userNotFoundTest() {
    given().when().get("/users/23").then().statusCode(404).log();
  }

  @Test
  @DisplayName("Successful Single User Delete")
  void deleteUserTest() {
    given().when().delete("/users/2").then().statusCode(204).log();
  }

  @Test
  @DisplayName("List Resources Json Body Matches Json Schema")
  void getSilosTest() {
    given()
        .when()
        .get("/unknown")
        .then()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("list-resources.json"))
        .log();
  }

  @Test
  @DisplayName("Successful registration")
  void successfulRegistrationTest() {
    String data =
        "{\n" + "    \"email\": \"eve.holt@reqres.in\",\n"
              + "    \"password\": \"pistol\"\n"
              + "}";

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .post("/register")
        .then()
        .statusCode(200)
        .log()
        .body()
        .body("token", notNullValue());
  }

  @Test
  @DisplayName("Successful Login")
  void successLoginTest() {
    String data =
        "{\n"
            + "    \"email\": \"eve.holt@reqres.in\",\n"
            + "    \"password\": \"cityslicka\"\n"
            + "}";

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .when()
        .post("/login")
        .then()
        .statusCode(200)
        .log()
        .body()
        .body("token", is(notNullValue()));
  }

  @Test
  @DisplayName("Unsuccessful Login")
  void unsuccessfulLoginTest() {
    String data = "{\n"
            + "    \"email\": \"peter@klaven\""
            + "}";

    given()
        .contentType(ContentType.JSON)
        .body(data)
        .when()
        .post("/login")
        .then()
        .statusCode(400)
        .body("error", is("Missing password"));
  }
}
