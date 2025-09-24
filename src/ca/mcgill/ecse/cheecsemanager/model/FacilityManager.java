/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 15 "model.ump"
// line 101 "model.ump"
public class FacilityManager extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //FacilityManager Associations
  private List<Company> companies;
  private List<Shelf> shelves;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public FacilityManager(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
    companies = new ArrayList<Company>();
    shelves = new ArrayList<Shelf>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Company getCompany(int index)
  {
    Company aCompany = companies.get(index);
    return aCompany;
  }

  public List<Company> getCompanies()
  {
    List<Company> newCompanies = Collections.unmodifiableList(companies);
    return newCompanies;
  }

  public int numberOfCompanies()
  {
    int number = companies.size();
    return number;
  }

  public boolean hasCompanies()
  {
    boolean has = companies.size() > 0;
    return has;
  }

  public int indexOfCompany(Company aCompany)
  {
    int index = companies.indexOf(aCompany);
    return index;
  }
  /* Code from template association_GetMany */
  public Shelf getShelve(int index)
  {
    Shelf aShelve = shelves.get(index);
    return aShelve;
  }

  public List<Shelf> getShelves()
  {
    List<Shelf> newShelves = Collections.unmodifiableList(shelves);
    return newShelves;
  }

  public int numberOfShelves()
  {
    int number = shelves.size();
    return number;
  }

  public boolean hasShelves()
  {
    boolean has = shelves.size() > 0;
    return has;
  }

  public int indexOfShelve(Shelf aShelve)
  {
    int index = shelves.indexOf(aShelve);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCompanies()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCompany(Company aCompany)
  {
    boolean wasAdded = false;
    if (companies.contains(aCompany)) { return false; }
    companies.add(aCompany);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCompany(Company aCompany)
  {
    boolean wasRemoved = false;
    if (companies.contains(aCompany))
    {
      companies.remove(aCompany);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCompanyAt(Company aCompany, int index)
  {  
    boolean wasAdded = false;
    if(addCompany(aCompany))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCompanies()) { index = numberOfCompanies() - 1; }
      companies.remove(aCompany);
      companies.add(index, aCompany);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCompanyAt(Company aCompany, int index)
  {
    boolean wasAdded = false;
    if(companies.contains(aCompany))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCompanies()) { index = numberOfCompanies() - 1; }
      companies.remove(aCompany);
      companies.add(index, aCompany);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCompanyAt(aCompany, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShelves()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Shelf addShelve(String aId)
  {
    return new Shelf(aId, this);
  }

  public boolean addShelve(Shelf aShelve)
  {
    boolean wasAdded = false;
    if (shelves.contains(aShelve)) { return false; }
    FacilityManager existingManager = aShelve.getManager();
    boolean isNewManager = existingManager != null && !this.equals(existingManager);
    if (isNewManager)
    {
      aShelve.setManager(this);
    }
    else
    {
      shelves.add(aShelve);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShelve(Shelf aShelve)
  {
    boolean wasRemoved = false;
    //Unable to remove aShelve, as it must always have a manager
    if (!this.equals(aShelve.getManager()))
    {
      shelves.remove(aShelve);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShelveAt(Shelf aShelve, int index)
  {  
    boolean wasAdded = false;
    if(addShelve(aShelve))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelves()) { index = numberOfShelves() - 1; }
      shelves.remove(aShelve);
      shelves.add(index, aShelve);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShelveAt(Shelf aShelve, int index)
  {
    boolean wasAdded = false;
    if(shelves.contains(aShelve))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelves()) { index = numberOfShelves() - 1; }
      shelves.remove(aShelve);
      shelves.add(index, aShelve);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShelveAt(aShelve, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    companies.clear();
    for(int i=shelves.size(); i > 0; i--)
    {
      Shelf aShelve = shelves.get(i - 1);
      aShelve.delete();
    }
    super.delete();
  }

}