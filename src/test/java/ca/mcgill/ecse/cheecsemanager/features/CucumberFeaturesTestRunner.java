package ca.mcgill.ecse.cheecsemanager.features;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
<<<<<<< Updated upstream
@CucumberOptions(plugin = "pretty", features = "src/test/resources/02.AddShelf.feature",
=======
@CucumberOptions(plugin = "pretty",
                 features =
                     "src/test/resources/10.AssignAndRemoveCheeseWheel.feature",
>>>>>>> Stashed changes
                 glue = "ca.mcgill.ecse.cheecsemanager.features")
public class CucumberFeaturesTestRunner {}
