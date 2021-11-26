package StepDefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class vegetableMarketJsonSteps {

    @Дано("В проекте создан файл с расширением json")
    public void jsonWasCreated() {
        return;
    }

    @Когда("Создан тест-запрос на созданием поля {string} со значением {string}")
    public void newUserCreated(String fieldName, String vegetableName) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(utils.Configuration.getConfigurationValue("pathToJsonDirectory")));
        Map<String, Object> myJson1 = new HashMap<>();
        myJson1.put(fieldName, vegetableName);
        ObjectMapper mapper1 = new ObjectMapper();
        writer.write(mapper1.writeValueAsString(myJson1));
        writer.close();
    }

    @И("body передан в запрос из ранее созданного файла с заменой значения в поле {string} на {string} и добавлением нового поля {string} со значением {string}")
    public void userNameChange(String fieldName1, String vegetableName, String fieldName2, String jobPlace) throws IOException {

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(utils.Configuration.getConfigurationValue("pathToJsonDirectory")));
        Map<String, Object> myJson1 = new HashMap<>();
        myJson1.put(fieldName1, vegetableName);
        myJson1.put(fieldName2, jobPlace);
        ObjectMapper mapper1 = new ObjectMapper();
        writer.write(mapper1.writeValueAsString(myJson1));
        writer.close();
    }

    @Тогда("Получено ответ 201 и проверены значения имени {string} и места работы {string}")
    public void getRightAnswer(String name, String jobPlace) throws IOException {
        JSONObject myJson = new JSONObject(new String(Files.readAllBytes(Paths.get(utils.
                Configuration.getConfigurationValue("pathToJsonDirectory")))));

        given()
                .baseUri(utils.Configuration.getConfigurationValue("urlForSecondTask"))
                .contentType("application/json;charset=UTF-8")
                .body(myJson.toString())
                .when()
                .post("/users/")
                .then()
                .assertThat()
                .statusCode(201)
                .assertThat()
                .body("name", equalTo(name))
                .body("job", equalTo(jobPlace))
                .log().all()
                .extract()
                .response();
    }

}

