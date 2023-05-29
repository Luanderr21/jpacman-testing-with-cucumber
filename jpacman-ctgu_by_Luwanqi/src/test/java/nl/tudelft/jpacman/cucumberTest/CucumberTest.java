package nl.tudelft.jpacman.cucumberTest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:build/cucumber-reports/html-report.html", "json:build/cucumber-reports/json-report.json"},
    features = {"./src/test/resources/cucumberTest"},
    glue = {"nl.tudelft.jpacman.cucumberTest"})
public class CucumberTest {

}
