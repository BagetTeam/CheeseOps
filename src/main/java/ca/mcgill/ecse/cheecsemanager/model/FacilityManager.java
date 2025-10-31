/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import java.util.*;

// line 21 "../../../../../../CheECSEManager.ump"
// line 116 "../../../../../../CheECSEManager.ump"
public class FacilityManager extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //FacilityManager Associations
  private CheECSEManager cheECSEManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public FacilityManager(String aEmail, String aPassword, CheECSEManager aCheECSEManager)
  {
    super(aEmail, aPassword);
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager)
    {
      throw new RuntimeException("Unable to create manager due to cheECSEManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager()
  {
    return cheECSEManager;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCheECSEManager(CheECSEManager aNewCheECSEManager)
  {
    boolean wasSet = false;
    if (aNewCheECSEManager == null)
    {
      //Unable to setCheECSEManager to null, as manager must always be associated to a cheECSEManager
      return wasSet;
    }
    
    FacilityManager existingManager = aNewCheECSEManager.getManager();
    if (existingManager != null && !equals(existingManager))
    {
      //Unable to setCheECSEManager, the current cheECSEManager already has a manager, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    CheECSEManager anOldCheECSEManager = cheECSEManager;
    cheECSEManager = aNewCheECSEManager;
    cheECSEManager.setManager(this);

    if (anOldCheECSEManager != null)
    {
      anOldCheECSEManager.setManager(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    CheECSEManager existingCheECSEManager = cheECSEManager;
    cheECSEManager = null;
    if (existingCheECSEManager != null)
    {
      existingCheECSEManager.setManager(null);
    }
    super.delete();
  }

}