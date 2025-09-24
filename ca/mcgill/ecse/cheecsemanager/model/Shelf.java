/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 16 "model.ump"
// line 112 "model.ump"
public class Shelf
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Shelf> shelfsById = new HashMap<String, Shelf>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Shelf Attributes
  private String id;

  //Shelf Associations
  private List<ShelfSlot> slots;
  private FacilityManager manager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Shelf(String aId, FacilityManager aManager)
  {
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    slots = new ArrayList<ShelfSlot>();
    boolean didAddManager = setManager(aManager);
    if (!didAddManager)
    {
      throw new RuntimeException("Unable to create shelve due to manager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(String aId)
  {
    boolean wasSet = false;
    String anOldId = getId();
    if (anOldId != null && anOldId.equals(aId)) {
      return true;
    }
    if (hasWithId(aId)) {
      return wasSet;
    }
    id = aId;
    wasSet = true;
    if (anOldId != null) {
      shelfsById.remove(anOldId);
    }
    shelfsById.put(aId, this);
    return wasSet;
  }

  public String getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Shelf getWithId(String aId)
  {
    return shelfsById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(String aId)
  {
    return getWithId(aId) != null;
  }
  /* Code from template association_GetMany */
  public ShelfSlot getSlot(int index)
  {
    ShelfSlot aSlot = slots.get(index);
    return aSlot;
  }

  public List<ShelfSlot> getSlots()
  {
    List<ShelfSlot> newSlots = Collections.unmodifiableList(slots);
    return newSlots;
  }

  public int numberOfSlots()
  {
    int number = slots.size();
    return number;
  }

  public boolean hasSlots()
  {
    boolean has = slots.size() > 0;
    return has;
  }

  public int indexOfSlot(ShelfSlot aSlot)
  {
    int index = slots.indexOf(aSlot);
    return index;
  }
  /* Code from template association_GetOne */
  public FacilityManager getManager()
  {
    return manager;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSlots()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShelfSlot addSlot(int aRow, int aColumn, Cheese aCheese)
  {
    return new ShelfSlot(aRow, aColumn, aCheese, this);
  }

  public boolean addSlot(ShelfSlot aSlot)
  {
    boolean wasAdded = false;
    if (slots.contains(aSlot)) { return false; }
    Shelf existingShelf = aSlot.getShelf();
    boolean isNewShelf = existingShelf != null && !this.equals(existingShelf);
    if (isNewShelf)
    {
      aSlot.setShelf(this);
    }
    else
    {
      slots.add(aSlot);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSlot(ShelfSlot aSlot)
  {
    boolean wasRemoved = false;
    //Unable to remove aSlot, as it must always have a shelf
    if (!this.equals(aSlot.getShelf()))
    {
      slots.remove(aSlot);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSlotAt(ShelfSlot aSlot, int index)
  {  
    boolean wasAdded = false;
    if(addSlot(aSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSlots()) { index = numberOfSlots() - 1; }
      slots.remove(aSlot);
      slots.add(index, aSlot);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSlotAt(ShelfSlot aSlot, int index)
  {
    boolean wasAdded = false;
    if(slots.contains(aSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSlots()) { index = numberOfSlots() - 1; }
      slots.remove(aSlot);
      slots.add(index, aSlot);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSlotAt(aSlot, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setManager(FacilityManager aManager)
  {
    boolean wasSet = false;
    if (aManager == null)
    {
      return wasSet;
    }

    FacilityManager existingManager = manager;
    manager = aManager;
    if (existingManager != null && !existingManager.equals(aManager))
    {
      existingManager.removeShelve(this);
    }
    manager.addShelve(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    shelfsById.remove(getId());
    while (slots.size() > 0)
    {
      ShelfSlot aSlot = slots.get(slots.size() - 1);
      aSlot.delete();
      slots.remove(aSlot);
    }
    
    FacilityManager placeholderManager = manager;
    this.manager = null;
    if(placeholderManager != null)
    {
      placeholderManager.removeShelve(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "manager = "+(getManager()!=null?Integer.toHexString(System.identityHashCode(getManager())):"null");
  }
}