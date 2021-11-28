package StepDefinitions;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;

public class rickAndMortySteps {

    private static String CharName;
    private static String CharacterId;
    private static String CharLoc;
    private static String CharRace;
    private static String nameOfLastChar;
    private static String lastCharRace;
    private static String lastCharLoc;
    private static int numberOfEpisode;
    private static int numberOfLastEpisode;
    private static int lastCharID;
    private static int lastCharIDD;

    @Дано("Найдена информация о персонаже с id {string}")
    public void findCharInfo(String Id) {

        Response getFirstCharInfo = given()
                .baseUri(utils.Configuration.getConfigurationValue("webUrl"))
                .when().get("/character/" + Id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        CharName = new JSONObject(getFirstCharInfo.getBody().asString()).get("name").toString();
        CharacterId = new JSONObject(getFirstCharInfo.getBody().asString()).get("id").toString();
        CharLoc = new JSONObject(getFirstCharInfo.getBody().asString()).getJSONObject("location").get("name").toString();
        CharRace = new JSONObject(getFirstCharInfo.getBody().asString()).get("species").toString();
    }

    @И("Выбран последний эпизод, где он появлялся")
    public void getLastEpisode() {

        Response getLastEpisode = given()
                .baseUri(utils.Configuration.getConfigurationValue("webUrl"))
                .when()
                .get("/character/" + CharacterId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        numberOfEpisode = new JSONObject(getLastEpisode.getBody().asString()).getJSONArray("episode").length() - 1;
        numberOfLastEpisode = Integer.parseInt(new JSONObject(getLastEpisode.getBody().asString())
                .getJSONArray("episode").get(numberOfEpisode).toString().replaceAll("[^\\d]", ""));
    }

    @Когда("Получен последний персонаж из этого эпизода")
    public void getEpisodeCharacter() {

        Response getLastCharackerInEpisode = given()
                .baseUri(utils.Configuration.getConfigurationValue("webUrl"))
                .when()
                .get("/episode/" + numberOfLastEpisode)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        lastCharID = (new JSONObject(getLastCharackerInEpisode.getBody().asString()).getJSONArray("characters").length() - 1);
        lastCharIDD = Integer.parseInt(new JSONObject(getLastCharackerInEpisode.getBody().asString())
                .getJSONArray("characters").get(lastCharID).toString().replaceAll("[^\\d]", ""));
        nameOfLastChar = new JSONObject(getLastCharackerInEpisode.getBody().asString()).get("name").toString();
    }

    @И("Получены данные о расе и местоположении данного персонажа")
    public void getSecondCharInfo() {

        Response getLastCharacterInfo = given()
                .baseUri(utils.Configuration.getConfigurationValue("webUrl"))
                .when()
                .get("/character/" + lastCharIDD)
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .response();

        lastCharRace = new JSONObject(getLastCharacterInfo.getBody().asString()).get("species").toString();
        lastCharLoc = new JSONObject(getLastCharacterInfo.getBody().asString()).getJSONObject("location").get("name").toString();
    }

    @Тогда("Сравнение этих данных с данными первого персонажа")
    public void comparingInfo() {

        Assertions.assertEquals(CharRace, lastCharRace, "Расы персрнажей не совпадают");
        Assertions.assertEquals(CharLoc, lastCharLoc, "Локации персрнажей не совпадают");
    }
}
