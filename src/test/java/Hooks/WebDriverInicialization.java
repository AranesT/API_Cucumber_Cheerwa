package Hooks;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class WebDriverInicialization {

    public static WebDriver driver;

    @Before
    public void setDriverProps() {
        String webDriverLocation = utils.Configuration.getConfigurationValue("webdriverlocationLOCAL");

        if (webDriverLocation != null) {
            System.setProperty("webdrriver.chrome.driver", webDriverLocation);
            System.setProperty("selenide.browser", "Chrome");
            Configuration.startMaximized = true;
        }
    }

    @Before
    public void allureSubThreadParallel() {
        String listenerName = "AllureSelenide";

        if (!(SelenideLogger.hasListener(listenerName)))
            SelenideLogger.addListener(listenerName,
                    new AllureSelenide().
                            screenshots(true).
                            savePageSource(false));
    }

    @After
    public void tearDown(Scenario scenario) {

        try {
            String screenshotName = scenario.getName().replaceAll(" ", "_");
            if (scenario.isFailed()) {
                scenario.log("Error");
                TakesScreenshot screenshotMaker = (TakesScreenshot) driver;
                byte[] sceenshot = screenshotMaker.getScreenshotAs(OutputType.BYTES);
                scenario.attach(sceenshot, "*/image/png", screenshotName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @After
    public void closer() {
        WebDriverRunner.closeWebDriver();
        SelenideLogger.removeListener("AllureSelenide");
    }
}

