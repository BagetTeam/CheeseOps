/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 30 "model.ump"
// line 127 "model.ump"
public class ShelfSlot
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ShelfSlot Attributes
  private int row;
  private int column;

  //ShelfSlot Associations
  private Cheese cheese;
  private Shelf shelf;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ShelfSlot(int aRow, int aColumn, Cheese aCheese, Shelf aShelf)
  {
    row = aRow;
    column = aColumn;
    boolean didAddCheese = setCheese(aCheese);
    if (!didAddCheese)
    {
      throw new RuntimeException("Unable to create slot due to cheese. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddShelf = setShelf(aShelf);
    if (!didAddShelf)
    {
      throw new RuntimeException("Unable to create slot due to shelf. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRow(int aRow)
  {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setColumn(int aColumn)
  {
    boolean wasSet = false;
    column = aColumn;
    wasSet = true;
    return wasSet;
  }

  public int getRow()
  {
    return row;
  }

  public int getColumn()
  {
    return column;
  }
  /* Code from template association_GetOne */
  public Cheese getCheese()
  {
    return cheese;
  }
  /* Code from template association_GetOne */
  public Shelf getShelf()
  {
    return shelf;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCheese(Cheese aNewCheese)
  {
    boolean wasSet = false;
    if (aNewCheese == null)
    {
      //Unable to setCheese to null, as slot must always be associated to a cheese
      return wasSet;
    }
    
    ShelfSlot existingSlot = aNewCheese.getSlot();
    if (existingSlot != null && !equals(existingSlot))
    {
      //Unable to setCheese, the current cheese already has a slot, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Cheese anOldCheese = cheese;
    cheese = aNewCheese;
    cheese.setSlot(this);

    if (anOldCheese != null)
    {
      anOldCheese.setSlot(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setShelf(Shelf aShelf)
  {
    boolean wasSet = false;
    if (aShelf == null)
    {
      return wasSet;
    }

    Shelf existingShelf = shelf;
    shelf = aShelf;
    if (existingShelf != null && !existingShelf.equals(aShelf))
    {
      existingShelf.removeSlot(this);
    }
    shelf.addSlot(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Cheese existingCheese = cheese;
    cheese = null;
    if (existingCheese != null)
    {
      existingCheese.setSlot(null);
    }
    Shelf placeholderShelf = shelf;
    this.shelf = null;
    if(placeholderShelf != null)
    {
      placeholderShelf.removeSlot(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "row" + ":" + getRow()+ "," +
            "column" + ":" + getColumn()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cheese = "+(getCheese()!=null?Integer.toHexString(System.identityHashCode(getCheese())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "shelf = "+(getShelf()!=null?Integer.toHexString(System.identityHashCode(getShelf())):"null");
  }
}