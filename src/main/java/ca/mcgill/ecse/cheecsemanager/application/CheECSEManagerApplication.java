package ca.mcgill.ecse.cheecsemanager.application;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;

public class CheECSEManagerApplication {
  private static CheECSEManager cheecsemanager;

  public static void main(String[] args) {
    // TODO Start the application user interface here
  }

  public static CheECSEManager getCheecseManager() {
    if (cheecsemanager == null) {
      // these attributes are default, you should set them later with the setters
      cheecsemanager = new CheECSEManager();
    }
    return cheecsemanager;
  }
}
