package ca.mcgill.ecse.cheecsemanager.features;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CommonStepDefinitions {
  /**
   * Method used to delete the current CheECSEManager system instance before the next test. This is
   * effective for all scenarios in all feature files
   */
  @Before
  public void setup() {
    CheECSEManagerApplication.getCheecseManager().delete();
    // reset the autounique ids for each class
    ShelfLocation.resetId();
    CheeseWheel.resetId();
    Transaction.resetId();
  }

  @After
  public void tearDown() {
    CheECSEManagerApplication.getCheecseManager().delete();
    // reset the autounique ids for each class
    ShelfLocation.resetId();
    CheeseWheel.resetId();
    Transaction.resetId();
  }
}
