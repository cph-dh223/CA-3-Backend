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
        .setRoute(Routs.securedRoutes(emfTest))
        .startServer(7777);
  }

  @BeforeEach
  public void setUpEach() {
    // Setup test database for each test
    new TestUtils().createNotes(emfTest);
    new TestUtils().createUsersAndRoles(emfTest);

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
        .body("{\"username\":\"user\",\"password\":\"user\"}")
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
    given().contentType("application/json").body("{\"title\":\"test\",\"content\":\"test\",\"category\":\"test\"}")
        .when().post("/user/note/create").peek().then().assertThat().statusCode(200);
  }

}
