package notes;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import notes.config.ApplicationConfig;
import notes.config.HibernateConfig;
import notes.config.Routs;
import notes.dtos.TokenDTO;
import io.restassured.RestAssured;
import io.restassured.http.Header;

import jakarta.persistence.EntityManagerFactory;
import static io.restassured.RestAssured.*;

/*
 * Tests run individualy work but as a group they brake
 */
public class EndpointTest {

  private static ApplicationConfig appConfig;
  private static final String BASE_URL = "http://localhost:7777/api";
  private static EntityManagerFactory emfTest;

  @BeforeAll
  public static void beforeAll() {
    RestAssured.baseURI = BASE_URL;
    HibernateConfig.setTestMode(true);

    // Setup test database using docker testcontainers
    emfTest = HibernateConfig.getEntityManagerFactoryForTest();

    // Start server
    appConfig = ApplicationConfig.getInstance(emfTest)
        .initiateServer()
        .setExceptionHandling()
        .checkSecurityRoles()
        .setRoute(Routs.getSecurityRoutes(emfTest))
        .setRoute(Routs.securedRoutes(emfTest))
        .startServer(7777);
  }

  @BeforeEach
  public void setUpEach() {
    // Setup test database for each test
    new TestUtils().createNotesAndUsers(emfTest);
  }

  @AfterAll
  static void afterAll() {
    HibernateConfig.setTestMode(false);
    appConfig.stopServer();
  }

  public TokenDTO getUserToken() {
    TokenDTO token = RestAssured
        .given()
        .contentType("application/json")
        .body("{\"email\":\"user\",\"password\":\"user\"}")
        .when()
        .post("/auth/login")
        .then()
        .extract()
        .as(TokenDTO.class);
    return token;
  }

  public TokenDTO getAdminToken() {
    TokenDTO token = RestAssured
    .given()
    .contentType("application/json")
    .body("{\"email\":\"admin\",\"password\":\"admin\"}")
    .when()
    .post("/auth/login")
    .then()
    .extract()
    .as(TokenDTO.class);
    return token;
}


  @Test
  void getAllnotes() {
    TokenDTO token = getUserToken();
    Header header = new Header("Authorization", "Bearer " + token.getToken());
    given().contentType("application/json").header(header).when().get("/notes").then().assertThat()
        .statusCode(200);
  }

  @Test
  void createNote() {
    TokenDTO token = getUserToken();
    Header header = new Header("Authorization", "Bearer " + token.getToken());
    given().contentType("application/json").header(header)
        .body("{\"title\":\"test\",\"content\":\"test\",\"category\":\"NOTE\"}")
        .when().post("/user/note/create").peek().then().assertThat().statusCode(201);
  }

  @Test
  void updateNote() {
    TokenDTO token = getUserToken();
    Header header = new Header("Authorization", "Bearer " + token.getToken());
    given().contentType("application/json").header(header)
        .body("{\"title\":\"test\",\"content\":\"test\"}").when()
        .put("/user/note/update/1")
        .peek().then().assertThat().statusCode(200);
  }

  @Test
  void deleteNote() {
    TokenDTO token = getUserToken();
    Header header = new Header("Authorization", "Bearer " + token.getToken());
    given().contentType("application/json").header(header).when().delete("/user/note/delete/1").peek().then().assertThat()
        .statusCode(204);
  }

  @Test
  void getNoteById() {
    TokenDTO token = getUserToken();
    Header header = new Header("Authorization", "Bearer " + token.getToken());
    given().contentType("application/json").header(header).when().get("/user/note/search/1").peek().then().assertThat()
        .statusCode(200);
  }

  @Test
  void getNoteByTitle() {
    TokenDTO token = getUserToken();
    Header header = new Header("Authorization", "Bearer " + token.getToken());
    given().contentType("application/json").header(header).when().get("/notes/search/title").peek().then().assertThat()
        .statusCode(200);
  }

  @Test
  void getAllUsers() {
    TokenDTO token = getAdminToken();
    Header header = new Header("Authorization", "Bearer " + token.getToken());
    given().contentType("application/json").header(header).when().get("/users").peek().then().assertThat()
        .statusCode(200);
  }


}
