import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"StepDefinitions", "Hooks"},
        plugin = {"pretty", "html:target/cucumber-html-reports",
                "io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm",
                "json:target/cucumber.json"},
        tags = "@rickAndMorty or @vegetableMarket"
)

public class RunnerTest {
    @BeforeClass
    public static void before() {
        RestAssured.filters(new AllureRestAssured());
    }

}




