package ca.mcgill.ecse.cheecsemanager.features;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)

@CucumberOptions(plugin = "pretty",
                 features =
                     "src/test/resources/",
                 glue = "ca.mcgill.ecse.cheecsemanager.features")
public class CucumberFeaturesTestRunner {}
