/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 10 "model.ump"
// line 102 "model.ump"
public class FacilityManager extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //FacilityManager Associations
  private List<Company> companies;
  private List<Shelf> shelfs;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public FacilityManager(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
    companies = new ArrayList<Company>();
    shelfs = new ArrayList<Shelf>();
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
  public Shelf getShelf(int index)
  {
    Shelf aShelf = shelfs.get(index);
    return aShelf;
  }

  public List<Shelf> getShelfs()
  {
    List<Shelf> newShelfs = Collections.unmodifiableList(shelfs);
    return newShelfs;
  }

  public int numberOfShelfs()
  {
    int number = shelfs.size();
    return number;
  }

  public boolean hasShelfs()
  {
    boolean has = shelfs.size() > 0;
    return has;
  }

  public int indexOfShelf(Shelf aShelf)
  {
    int index = shelfs.indexOf(aShelf);
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
  public static int minimumNumberOfShelfs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Shelf addShelf(String aId)
  {
    return new Shelf(aId, this);
  }

  public boolean addShelf(Shelf aShelf)
  {
    boolean wasAdded = false;
    if (shelfs.contains(aShelf)) { return false; }
    FacilityManager existingFacilityManager = aShelf.getFacilityManager();
    boolean isNewFacilityManager = existingFacilityManager != null && !this.equals(existingFacilityManager);
    if (isNewFacilityManager)
    {
      aShelf.setFacilityManager(this);
    }
    else
    {
      shelfs.add(aShelf);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShelf(Shelf aShelf)
  {
    boolean wasRemoved = false;
    //Unable to remove aShelf, as it must always have a facilityManager
    if (!this.equals(aShelf.getFacilityManager()))
    {
      shelfs.remove(aShelf);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShelfAt(Shelf aShelf, int index)
  {  
    boolean wasAdded = false;
    if(addShelf(aShelf))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelfs()) { index = numberOfShelfs() - 1; }
      shelfs.remove(aShelf);
      shelfs.add(index, aShelf);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShelfAt(Shelf aShelf, int index)
  {
    boolean wasAdded = false;
    if(shelfs.contains(aShelf))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelfs()) { index = numberOfShelfs() - 1; }
      shelfs.remove(aShelf);
      shelfs.add(index, aShelf);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShelfAt(aShelf, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    companies.clear();
    for(int i=shelfs.size(); i > 0; i--)
    {
      Shelf aShelf = shelfs.get(i - 1);
      aShelf.delete();
    }
    super.delete();
  }

}