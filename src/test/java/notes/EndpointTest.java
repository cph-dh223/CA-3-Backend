package notes;
// package notes;
// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.JsonMappingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import notes.config.ApplicationConfig;
// import notes.config.HibernateConfig;
// import notes.config.Routs;
// import notes.ressources.notes;
// import notes.ressources.Room;
// import io.javalin.http.ContentType;
// import io.restassured.RestAssured;
// import static org.hamcrest.Matchers.*;
// import io.restassured.response.Response;
// import jakarta.persistence.EntityManagerFactory;
// import static io.restassured.RestAssured.*;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.Map;
// import java.util.stream.Collectors;

// /*
//  * Tests run individualy work but as a group they brake
//  */
// public class EndpointTest {

//     private static ApplicationConfig appConfig;
//     private static final String BASE_URL = "http://localhost:7777/api";
//     private static EntityManagerFactory emfTest;
//     private static ObjectMapper objectMapper = new ObjectMapper();
//     private static Map<Integer,notes> notess;
//     private static Map<Integer,Room> rooms;
//     @BeforeAll
//     public static void beforeAll(){
//         RestAssured.baseURI = BASE_URL;
//         objectMapper.findAndRegisterModules();
//         HibernateConfig.setTestMode(true);
        

//         // Setup test database using docker testcontainers
//         emfTest = HibernateConfig.getEntityManagerFactoryForTest();

//         // Start server
//         appConfig = ApplicationConfig.
//                 getInstance(emfTest)
//                 .initiateServer()
//                 .setExceptionHandling()
//                 .setRoute(Routs.getResourses(emfTest))
//                 .startServer(7777)
//         ;
//     }

//     @BeforeEach
//     public void setUpEach() {
//         // Setup test database for each test
//         new TestUtils().createnotessAndRooms(emfTest);
//         notess = new TestUtils().getnotess(emfTest);
//         rooms = new TestUtils().getRooms(emfTest);
        
//     }
    
//     @AfterAll
//     static void afterAll() {
//         HibernateConfig.setTestMode(false);
//         appConfig.stopServer();
//     }
    
//     @Test
//     public void defTest(){
        
//         given().when().get("/").peek().then().assertThat().statusCode(200);
//     }
    
//     @Test
//     public void getAllnotess() throws JsonMappingException, JsonProcessingException {
        
//         Response res = given().when().get("/notes").peek();
        
//         notes[] response = objectMapper.readValue(res.asString(), notes[].class);
//         assertEquals(notess.size(), response.length);
//     }

//     @Test
//     public void updatenotes() throws JsonProcessingException{
//         notes h = notess.get(notess.keySet().toArray()[0]);
//         h.setName("new h1");
//         String requestBody = objectMapper.writeValueAsString(h);
//         given()
//             .header("Content-type", ContentType.JSON)
//             .body(requestBody)
//             .when()
//             .post("/notes/" + h.getId())
//             .then()
//             .log().body()
//             .assertThat()
//             .statusCode(200)
//             .body("name", equalTo(h.getName()))
//             .body("address", equalTo(h.getAddress()));
//     }
    
//     @Test
//     public void getnotesById() {
//         notes h = notess.get(notess.keySet().toArray()[0]);
//         given().when().get("notes/" + h.getId()).peek().then().assertThat().statusCode(200).body("name",equalTo(h.getName()));
//     }

//     @Test
//     public void createnotes() throws JsonProcessingException{
//         notes h = new notes("h4","a4");
        
//         String requestBody = objectMapper.writeValueAsString(h);
//         given()
//             .header("Content-type", ContentType.JSON)
//             .body(requestBody)
//             .when()
//             .put("/notes/" + h.getId())
//             .then()
//             .assertThat()
//             .statusCode(201)
//             .body("name", equalTo(h.getName()))
//             .body("address", equalTo(h.getAddress()));
//     }

//     @Test
//     public void deletenotes() throws JsonProcessingException{
//         notes h = notess.get(notess.keySet().toArray()[0]);
        
//         String requestBody = objectMapper.writeValueAsString(h);
//         given()
//             .header("Content-type", ContentType.JSON)
//             .body(requestBody)
//             .when()
//             .delete("/notes/" + h.getId())
//             .then()
//             .assertThat()
//             .statusCode(200);
//     }
    

//     @Test
//     public void getnotesRooms() throws JsonProcessingException{
//         notes h = notess.get(notess.keySet().toArray()[0]);
        
//         String requestBody = objectMapper.writeValueAsString(h);

        
//         var res = given()
//             .header("Content-type", ContentType.JSON)
//             .body(requestBody)
//             .when()
//             .get("/notes/" + h.getId() + "/rooms");
        
//         Room[] response = objectMapper.readValue(res.asString(), Room[].class);
//         assertEquals(h.getRooms().size(), response.length);
        
//         for (Room room : response) {
//             assertTrue(h.getRooms().stream().filter(r -> r.getId() == room.getId()).collect(Collectors.toList()).size() == 1);
//         }
//     }

//     @Test
//     public void getAllRooms() throws JsonMappingException, JsonProcessingException {
        
//         Response res = given().when().get("/room").peek();
        
//         Room[] response = objectMapper.readValue(res.asString(), Room[].class);
//         assertEquals(rooms.size(), response.length);
//     }

//     @Test
//     public void updateRoom() throws JsonProcessingException{
//         Room r = rooms.get(rooms.keySet().toArray()[0]);
//         r.setPrice(20000f);
//         String requestBody = objectMapper.writeValueAsString(r);
//         r.getnotes();
//         given()
//             .header("Content-type", ContentType.JSON)
//             .body(requestBody)
//             .when()
//             .post("/room/" + r.getId())
//             .then()
//             .log().body()
//             .assertThat()
//             .statusCode(200)
//             .body("roomNumber", equalTo("" + r.getRoomNumber()))
//             .body("price", equalTo("" + r.getPrice()));
//     }
    
//     @Test
//     public void getRoomById() {
//         Room r = rooms.get(rooms.keySet().toArray()[0]);
//         given().when().get("room/" + r.getId()).peek().then().assertThat().statusCode(200).body("roomNumber",equalTo(r.getRoomNumber()));
//     }

//     // @Test
//     // public void createRoom() throws JsonProcessingException{
//     //     notes h = notess.get(notess.keySet().toArray()[0]);
//     //     Room r = new Room(300, h);
//     //     r.setPrice(20);
//     //     String requestBody = objectMapper.writeValueAsString(r);
//     //     given()
//     //         .header("Content-type", ContentType.JSON)
//     //         .body(requestBody)
//     //         .when()
//     //         .put("/room/" + r.getId())
//     //         .then()
//     //         .assertThat()
//     //         .statusCode(200)
//     //         .body("roomNumber", equalTo(r.getRoomNumber()))
//     //         .body("price", equalTo(r.getPrice()));
//     // }

//     @Test
//     public void deleteRoom() throws JsonProcessingException{
//         Room r = rooms.get(rooms.keySet().toArray()[0]);
        
//         String requestBody = objectMapper.writeValueAsString(r);
//         given()
//             .header("Content-type", ContentType.JSON)
//             .body(requestBody)
//             .when()
//             .delete("/room/" + r.getId())
//             .then()
//             .assertThat()
//             .statusCode(200);
//     }
// }
