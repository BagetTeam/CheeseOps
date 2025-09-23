package ca.mcgill.ecse.cheecsemanager.model;
//%% NEW FILE Order BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.util.*;

// line 40 "model.ump"
// line 126 "model.ump"
public class Order
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private int kind;
  private int numberOfMissingCheese;
  private Date purchaseDate;
  private Date deliveryDate;

  //Order Associations
  private List<Cheese> cheeses;
  private Company company;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(int aKind, int aNumberOfMissingCheese, Date aPurchaseDate, Date aDeliveryDate, Company aCompany)
  {
    kind = aKind;
    numberOfMissingCheese = aNumberOfMissingCheese;
    purchaseDate = aPurchaseDate;
    deliveryDate = aDeliveryDate;
    cheeses = new ArrayList<Cheese>();
    boolean didAddCompany = setCompany(aCompany);
    if (!didAddCompany)
    {
      throw new RuntimeException("Unable to create order due to company. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setKind(int aKind)
  {
    boolean wasSet = false;
    kind = aKind;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumberOfMissingCheese(int aNumberOfMissingCheese)
  {
    boolean wasSet = false;
    numberOfMissingCheese = aNumberOfMissingCheese;
    wasSet = true;
    return wasSet;
  }

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeliveryDate(Date aDeliveryDate)
  {
    boolean wasSet = false;
    deliveryDate = aDeliveryDate;
    wasSet = true;
    return wasSet;
  }

  public int getKind()
  {
    return kind;
  }

  public int getNumberOfMissingCheese()
  {
    return numberOfMissingCheese;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public Date getDeliveryDate()
  {
    return deliveryDate;
  }
  /* Code from template association_GetMany */
  public Cheese getCheese(int index)
  {
    Cheese aCheese = cheeses.get(index);
    return aCheese;
  }

  public List<Cheese> getCheeses()
  {
    List<Cheese> newCheeses = Collections.unmodifiableList(cheeses);
    return newCheeses;
  }

  public int numberOfCheeses()
  {
    int number = cheeses.size();
    return number;
  }

  public boolean hasCheeses()
  {
    boolean has = cheeses.size() > 0;
    return has;
  }

  public int indexOfCheese(Cheese aCheese)
  {
    int index = cheeses.indexOf(aCheese);
    return index;
  }
  /* Code from template association_GetOne */
  public Company getCompany()
  {
    return company;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCheeses()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addCheese(Cheese aCheese)
  {
    boolean wasAdded = false;
    if (cheeses.contains(aCheese)) { return false; }
    Order existingOrder = aCheese.getOrder();
    if (existingOrder == null)
    {
      aCheese.setOrder(this);
    }
    else if (!this.equals(existingOrder))
    {
      existingOrder.removeCheese(aCheese);
      addCheese(aCheese);
    }
    else
    {
      cheeses.add(aCheese);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCheese(Cheese aCheese)
  {
    boolean wasRemoved = false;
    if (cheeses.contains(aCheese))
    {
      cheeses.remove(aCheese);
      aCheese.setOrder(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCheeseAt(Cheese aCheese, int index)
  {
    boolean wasAdded = false;
    if(addCheese(aCheese))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCheeses()) { index = numberOfCheeses() - 1; }
      cheeses.remove(aCheese);
      cheeses.add(index, aCheese);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCheeseAt(Cheese aCheese, int index)
  {
    boolean wasAdded = false;
    if(cheeses.contains(aCheese))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCheeses()) { index = numberOfCheeses() - 1; }
      cheeses.remove(aCheese);
      cheeses.add(index, aCheese);
      wasAdded = true;
    }
    else
    {
      wasAdded = addCheeseAt(aCheese, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCompany(Company aCompany)
  {
    boolean wasSet = false;
    if (aCompany == null)
    {
      return wasSet;
    }

    Company existingCompany = company;
    company = aCompany;
    if (existingCompany != null && !existingCompany.equals(aCompany))
    {
      existingCompany.removeOrder(this);
    }
    company.addOrder(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    while( !cheeses.isEmpty() )
    {
      cheeses.get(0).setOrder(null);
    }
    Company placeholderCompany = company;
    this.company = null;
    if(placeholderCompany != null)
    {
      placeholderCompany.removeOrder(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "kind" + ":" + getKind()+ "," +
            "numberOfMissingCheese" + ":" + getNumberOfMissingCheese()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deliveryDate" + "=" + (getDeliveryDate() != null ? !getDeliveryDate().equals(this)  ? getDeliveryDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "company = "+(getCompany()!=null?Integer.toHexString(System.identityHashCode(getCompany())):"null");
  }
}



//%% NEW FILE Company BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;
import java.sql.Date;

// line 70 "model.ump"
// line 150 "model.ump"
public class Company
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Company> companysByName = new HashMap<String, Company>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Company Attributes
  private String address;
  private String name;

  //Company Associations
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Company(String aAddress, String aName)
  {
    address = aAddress;
    if (!setName(aName))
    {
      throw new RuntimeException("Cannot create due to duplicate name. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    orders = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    String anOldName = getName();
    if (anOldName != null && anOldName.equals(aName)) {
      return true;
    }
    if (hasWithName(aName)) {
      return wasSet;
    }
    name = aName;
    wasSet = true;
    if (anOldName != null) {
      companysByName.remove(anOldName);
    }
    companysByName.put(aName, this);
    return wasSet;
  }

  public String getAddress()
  {
    return address;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template attribute_GetUnique */
  public static Company getWithName(String aName)
  {
    return companysByName.get(aName);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithName(String aName)
  {
    return getWithName(aName) != null;
  }
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = orders.get(index);
    return aOrder;
  }

  public List<Order> getOrders()
  {
    List<Order> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrder(int aKind, int aNumberOfMissingCheese, Date aPurchaseDate, Date aDeliveryDate)
  {
    return new Order(aKind, aNumberOfMissingCheese, aPurchaseDate, aDeliveryDate, this);
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    Company existingCompany = aOrder.getCompany();
    boolean isNewCompany = existingCompany != null && !this.equals(existingCompany);
    if (isNewCompany)
    {
      aOrder.setCompany(this);
    }
    else
    {
      orders.add(aOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrder, as it must always have a company
    if (!this.equals(aOrder.getCompany()))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    else
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    companysByName.remove(getName());
    while (orders.size() > 0)
    {
      Order aOrder = orders.get(orders.size() - 1);
      aOrder.delete();
      orders.remove(aOrder);
    }

  }


  public String toString()
  {
    return super.toString() + "["+
            "address" + ":" + getAddress()+ "," +
            "name" + ":" + getName()+ "]";
  }
}



//%% NEW FILE User BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 78 "model.ump"
// line 156 "model.ump"
public abstract class User
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, User> usersByEmail = new HashMap<String, User>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private String email;
  private String password;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(String aEmail, String aPassword)
  {
    password = aPassword;
    if (!setEmail(aEmail))
    {
      throw new RuntimeException("Cannot create due to duplicate email. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    String anOldEmail = getEmail();
    if (anOldEmail != null && anOldEmail.equals(aEmail)) {
      return true;
    }
    if (hasWithEmail(aEmail)) {
      return wasSet;
    }
    email = aEmail;
    wasSet = true;
    if (anOldEmail != null) {
      usersByEmail.remove(anOldEmail);
    }
    usersByEmail.put(aEmail, this);
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }
  /* Code from template attribute_GetUnique */
  public static User getWithEmail(String aEmail)
  {
    return usersByEmail.get(aEmail);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithEmail(String aEmail)
  {
    return getWithEmail(aEmail) != null;
  }

  public String getPassword()
  {
    return password;
  }

  public void delete()
  {
    usersByEmail.remove(getEmail());
  }


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "]";
  }
}



//%% NEW FILE RobotActionLog BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 57 "model.ump"
// line 141 "model.ump"
public class RobotActionLog
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RobotActionLog Attributes
  private String severity;
  private String title;
  private String details;

  //RobotActionLog Associations
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RobotActionLog(String aSeverity, String aTitle, String aDetails, Robot aRobot)
  {
    severity = aSeverity;
    title = aTitle;
    details = aDetails;
    boolean didAddRobot = setRobot(aRobot);
    if (!didAddRobot)
    {
      throw new RuntimeException("Unable to create robotActionLog due to robot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSeverity(String aSeverity)
  {
    boolean wasSet = false;
    severity = aSeverity;
    wasSet = true;
    return wasSet;
  }

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
    wasSet = true;
    return wasSet;
  }

  public boolean setDetails(String aDetails)
  {
    boolean wasSet = false;
    details = aDetails;
    wasSet = true;
    return wasSet;
  }

  public String getSeverity()
  {
    return severity;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDetails()
  {
    return details;
  }
  /* Code from template association_GetOne */
  public Robot getRobot()
  {
    return robot;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRobot(Robot aRobot)
  {
    boolean wasSet = false;
    if (aRobot == null)
    {
      return wasSet;
    }

    Robot existingRobot = robot;
    robot = aRobot;
    if (existingRobot != null && !existingRobot.equals(aRobot))
    {
      existingRobot.removeRobotActionLog(this);
    }
    robot.addRobotActionLog(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Robot placeholderRobot = robot;
    this.robot = null;
    if(placeholderRobot != null)
    {
      placeholderRobot.removeRobotActionLog(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "severity" + ":" + getSeverity()+ "," +
            "title" + ":" + getTitle()+ "," +
            "details" + ":" + getDetails()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "robot = "+(getRobot()!=null?Integer.toHexString(System.identityHashCode(getRobot())):"null");
  }
}



//%% NEW FILE CheeseManager BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 85 "model.ump"
// line 161 "model.ump"
public class CheeseManager
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CheeseManager Associations
  private List<User> users;
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CheeseManager(Robot aRobot)
  {
    users = new ArrayList<User>();
    if (aRobot == null || aRobot.getCheeseManager() != null)
    {
      throw new RuntimeException("Unable to create CheeseManager due to aRobot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    robot = aRobot;
  }

  public CheeseManager(int aDirectionForRobot, ShelfSlot aShelfSlotForRobot)
  {
    users = new ArrayList<User>();
    robot = new Robot(aDirectionForRobot, this, aShelfSlotForRobot);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public User getUser(int index)
  {
    User aUser = users.get(index);
    return aUser;
  }

  public List<User> getUsers()
  {
    List<User> newUsers = Collections.unmodifiableList(users);
    return newUsers;
  }

  public int numberOfUsers()
  {
    int number = users.size();
    return number;
  }

  public boolean hasUsers()
  {
    boolean has = users.size() > 0;
    return has;
  }

  public int indexOfUser(User aUser)
  {
    int index = users.indexOf(aUser);
    return index;
  }
  /* Code from template association_GetOne */
  public Robot getRobot()
  {
    return robot;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfUsers()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addUser(User aUser)
  {
    boolean wasAdded = false;
    if (users.contains(aUser)) { return false; }
    users.add(aUser);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeUser(User aUser)
  {
    boolean wasRemoved = false;
    if (users.contains(aUser))
    {
      users.remove(aUser);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addUserAt(User aUser, int index)
  {
    boolean wasAdded = false;
    if(addUser(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveUserAt(User aUser, int index)
  {
    boolean wasAdded = false;
    if(users.contains(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    }
    else
    {
      wasAdded = addUserAt(aUser, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    users.clear();
    Robot existingRobot = robot;
    robot = null;
    if (existingRobot != null)
    {
      existingRobot.delete();
    }
  }

}



//%% NEW FILE FacilityManager BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;
import java.sql.Date;

// line 10 "model.ump"
// line 98 "model.ump"
public class FacilityManager extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //FacilityManager Associations
  private List<Farmer> farmers;
  private List<Company> companies;
  private List<Shelf> shelfs;
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public FacilityManager(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
    farmers = new ArrayList<Farmer>();
    companies = new ArrayList<Company>();
    shelfs = new ArrayList<Shelf>();
    orders = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Farmer getFarmer(int index)
  {
    Farmer aFarmer = farmers.get(index);
    return aFarmer;
  }

  public List<Farmer> getFarmers()
  {
    List<Farmer> newFarmers = Collections.unmodifiableList(farmers);
    return newFarmers;
  }

  public int numberOfFarmers()
  {
    int number = farmers.size();
    return number;
  }

  public boolean hasFarmers()
  {
    boolean has = farmers.size() > 0;
    return has;
  }

  public int indexOfFarmer(Farmer aFarmer)
  {
    int index = farmers.indexOf(aFarmer);
    return index;
  }
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
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = orders.get(index);
    return aOrder;
  }

  public List<Order> getOrders()
  {
    List<Order> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfFarmers()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addFarmer(Farmer aFarmer)
  {
    boolean wasAdded = false;
    if (farmers.contains(aFarmer)) { return false; }
    farmers.add(aFarmer);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeFarmer(Farmer aFarmer)
  {
    boolean wasRemoved = false;
    if (farmers.contains(aFarmer))
    {
      farmers.remove(aFarmer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addFarmerAt(Farmer aFarmer, int index)
  {
    boolean wasAdded = false;
    if(addFarmer(aFarmer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFarmers()) { index = numberOfFarmers() - 1; }
      farmers.remove(aFarmer);
      farmers.add(index, aFarmer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveFarmerAt(Farmer aFarmer, int index)
  {
    boolean wasAdded = false;
    if(farmers.contains(aFarmer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFarmers()) { index = numberOfFarmers() - 1; }
      farmers.remove(aFarmer);
      farmers.add(index, aFarmer);
      wasAdded = true;
    }
    else
    {
      wasAdded = addFarmerAt(aFarmer, index);
    }
    return wasAdded;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    orders.add(aOrder);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    if (orders.contains(aOrder))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    else
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    farmers.clear();
    companies.clear();
    for(int i=shelfs.size(); i > 0; i--)
    {
      Shelf aShelf = shelfs.get(i - 1);
      aShelf.delete();
    }
    orders.clear();
    super.delete();
  }

}



//%% NEW FILE ShelfSlot BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 32 "model.ump"
// line 119 "model.ump"
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
      throw new RuntimeException("Unable to create shelfSlot due to cheese. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddShelf = setShelf(aShelf);
    if (!didAddShelf)
    {
      throw new RuntimeException("Unable to create shelfSlot due to shelf. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
      //Unable to setCheese to null, as shelfSlot must always be associated to a cheese
      return wasSet;
    }

    ShelfSlot existingShelfSlot = aNewCheese.getShelfSlot();
    if (existingShelfSlot != null && !equals(existingShelfSlot))
    {
      //Unable to setCheese, the current cheese already has a shelfSlot, which would be orphaned if it were re-assigned
      return wasSet;
    }

    Cheese anOldCheese = cheese;
    cheese = aNewCheese;
    cheese.setShelfSlot(this);

    if (anOldCheese != null)
    {
      anOldCheese.setShelfSlot(null);
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
      existingShelf.removeShelfSlot(this);
    }
    shelf.addShelfSlot(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Cheese existingCheese = cheese;
    cheese = null;
    if (existingCheese != null)
    {
      existingCheese.setShelfSlot(null);
    }
    Shelf placeholderShelf = shelf;
    this.shelf = null;
    if(placeholderShelf != null)
    {
      placeholderShelf.removeShelfSlot(this);
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



//%% NEW FILE Shelf BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 17 "model.ump"
// line 106 "model.ump"
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
  private List<ShelfSlot> shelfSlots;
  private FacilityManager facilityManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Shelf(String aId, FacilityManager aFacilityManager)
  {
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    shelfSlots = new ArrayList<ShelfSlot>();
    boolean didAddFacilityManager = setFacilityManager(aFacilityManager);
    if (!didAddFacilityManager)
    {
      throw new RuntimeException("Unable to create shelf due to facilityManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
  public ShelfSlot getShelfSlot(int index)
  {
    ShelfSlot aShelfSlot = shelfSlots.get(index);
    return aShelfSlot;
  }

  public List<ShelfSlot> getShelfSlots()
  {
    List<ShelfSlot> newShelfSlots = Collections.unmodifiableList(shelfSlots);
    return newShelfSlots;
  }

  public int numberOfShelfSlots()
  {
    int number = shelfSlots.size();
    return number;
  }

  public boolean hasShelfSlots()
  {
    boolean has = shelfSlots.size() > 0;
    return has;
  }

  public int indexOfShelfSlot(ShelfSlot aShelfSlot)
  {
    int index = shelfSlots.indexOf(aShelfSlot);
    return index;
  }
  /* Code from template association_GetOne */
  public FacilityManager getFacilityManager()
  {
    return facilityManager;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShelfSlots()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShelfSlot addShelfSlot(int aRow, int aColumn, Cheese aCheese)
  {
    return new ShelfSlot(aRow, aColumn, aCheese, this);
  }

  public boolean addShelfSlot(ShelfSlot aShelfSlot)
  {
    boolean wasAdded = false;
    if (shelfSlots.contains(aShelfSlot)) { return false; }
    Shelf existingShelf = aShelfSlot.getShelf();
    boolean isNewShelf = existingShelf != null && !this.equals(existingShelf);
    if (isNewShelf)
    {
      aShelfSlot.setShelf(this);
    }
    else
    {
      shelfSlots.add(aShelfSlot);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShelfSlot(ShelfSlot aShelfSlot)
  {
    boolean wasRemoved = false;
    //Unable to remove aShelfSlot, as it must always have a shelf
    if (!this.equals(aShelfSlot.getShelf()))
    {
      shelfSlots.remove(aShelfSlot);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShelfSlotAt(ShelfSlot aShelfSlot, int index)
  {
    boolean wasAdded = false;
    if(addShelfSlot(aShelfSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelfSlots()) { index = numberOfShelfSlots() - 1; }
      shelfSlots.remove(aShelfSlot);
      shelfSlots.add(index, aShelfSlot);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShelfSlotAt(ShelfSlot aShelfSlot, int index)
  {
    boolean wasAdded = false;
    if(shelfSlots.contains(aShelfSlot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelfSlots()) { index = numberOfShelfSlots() - 1; }
      shelfSlots.remove(aShelfSlot);
      shelfSlots.add(index, aShelfSlot);
      wasAdded = true;
    }
    else
    {
      wasAdded = addShelfSlotAt(aShelfSlot, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setFacilityManager(FacilityManager aFacilityManager)
  {
    boolean wasSet = false;
    if (aFacilityManager == null)
    {
      return wasSet;
    }

    FacilityManager existingFacilityManager = facilityManager;
    facilityManager = aFacilityManager;
    if (existingFacilityManager != null && !existingFacilityManager.equals(aFacilityManager))
    {
      existingFacilityManager.removeShelf(this);
    }
    facilityManager.addShelf(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    shelfsById.remove(getId());
    while (shelfSlots.size() > 0)
    {
      ShelfSlot aShelfSlot = shelfSlots.get(shelfSlots.size() - 1);
      aShelfSlot.delete();
      shelfSlots.remove(aShelfSlot);
    }

    FacilityManager placeholderFacilityManager = facilityManager;
    this.facilityManager = null;
    if(placeholderFacilityManager != null)
    {
      placeholderFacilityManager.removeShelf(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "facilityManager = "+(getFacilityManager()!=null?Integer.toHexString(System.identityHashCode(getFacilityManager())):"null");
  }
}



//%% NEW FILE Cheese BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;

// line 24 "model.ump"
// line 113 "model.ump"
public class Cheese
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Cheese Attributes
  private Date purchaseDate;
  private int age;
  private int monthsToAge;
  private boolean isSpoiled;

  //Cheese Associations
  private Farmer farmer;
  private ShelfSlot shelfSlot;
  private Order order;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Cheese(Date aPurchaseDate, int aAge, int aMonthsToAge, boolean aIsSpoiled, Farmer aFarmer)
  {
    purchaseDate = aPurchaseDate;
    age = aAge;
    monthsToAge = aMonthsToAge;
    isSpoiled = aIsSpoiled;
    boolean didAddFarmer = setFarmer(aFarmer);
    if (!didAddFarmer)
    {
      throw new RuntimeException("Unable to create cheese due to farmer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setAge(int aAge)
  {
    boolean wasSet = false;
    age = aAge;
    wasSet = true;
    return wasSet;
  }

  public boolean setMonthsToAge(int aMonthsToAge)
  {
    boolean wasSet = false;
    monthsToAge = aMonthsToAge;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsSpoiled(boolean aIsSpoiled)
  {
    boolean wasSet = false;
    isSpoiled = aIsSpoiled;
    wasSet = true;
    return wasSet;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public int getAge()
  {
    return age;
  }

  public int getMonthsToAge()
  {
    return monthsToAge;
  }

  public boolean getIsSpoiled()
  {
    return isSpoiled;
  }
  /* Code from template association_GetOne */
  public Farmer getFarmer()
  {
    return farmer;
  }
  /* Code from template association_GetOne */
  public ShelfSlot getShelfSlot()
  {
    return shelfSlot;
  }

  public boolean hasShelfSlot()
  {
    boolean has = shelfSlot != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Order getOrder()
  {
    return order;
  }

  public boolean hasOrder()
  {
    boolean has = order != null;
    return has;
  }
  /* Code from template association_SetOneToMany */
  public boolean setFarmer(Farmer aFarmer)
  {
    boolean wasSet = false;
    if (aFarmer == null)
    {
      return wasSet;
    }

    Farmer existingFarmer = farmer;
    farmer = aFarmer;
    if (existingFarmer != null && !existingFarmer.equals(aFarmer))
    {
      existingFarmer.removeCheese(this);
    }
    farmer.addCheese(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setShelfSlot(ShelfSlot aNewShelfSlot)
  {
    boolean wasSet = false;
    if (shelfSlot != null && !shelfSlot.equals(aNewShelfSlot) && equals(shelfSlot.getCheese()))
    {
      //Unable to setShelfSlot, as existing shelfSlot would become an orphan
      return wasSet;
    }

    shelfSlot = aNewShelfSlot;
    Cheese anOldCheese = aNewShelfSlot != null ? aNewShelfSlot.getCheese() : null;

    if (!this.equals(anOldCheese))
    {
      if (anOldCheese != null)
      {
        anOldCheese.shelfSlot = null;
      }
      if (shelfSlot != null)
      {
        shelfSlot.setCheese(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrder(Order aOrder)
  {
    boolean wasSet = false;
    Order existingOrder = order;
    order = aOrder;
    if (existingOrder != null && !existingOrder.equals(aOrder))
    {
      existingOrder.removeCheese(this);
    }
    if (aOrder != null)
    {
      aOrder.addCheese(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Farmer placeholderFarmer = farmer;
    this.farmer = null;
    if(placeholderFarmer != null)
    {
      placeholderFarmer.removeCheese(this);
    }
    ShelfSlot existingShelfSlot = shelfSlot;
    shelfSlot = null;
    if (existingShelfSlot != null)
    {
      existingShelfSlot.delete();
    }
    if (order != null)
    {
      Order placeholderOrder = order;
      this.order = null;
      placeholderOrder.removeCheese(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "age" + ":" + getAge()+ "," +
            "monthsToAge" + ":" + getMonthsToAge()+ "," +
            "isSpoiled" + ":" + getIsSpoiled()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "farmer = "+(getFarmer()!=null?Integer.toHexString(System.identityHashCode(getFarmer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "shelfSlot = "+(getShelfSlot()!=null?Integer.toHexString(System.identityHashCode(getShelfSlot())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null");
  }
}



//%% NEW FILE Farmer BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;
import java.sql.Date;

// line 2 "model.ump"
// line 90 "model.ump"
public class Farmer extends User
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Farmer> farmersByName = new HashMap<String, Farmer>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Farmer Attributes
  private String postalCode;
  private String name;

  //Farmer Associations
  private List<Cheese> cheeses;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Farmer(String aEmail, String aPassword, String aPostalCode, String aName)
  {
    super(aEmail, aPassword);
    postalCode = aPostalCode;
    if (!setName(aName))
    {
      throw new RuntimeException("Cannot create due to duplicate name. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    cheeses = new ArrayList<Cheese>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPostalCode(String aPostalCode)
  {
    boolean wasSet = false;
    postalCode = aPostalCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    String anOldName = getName();
    if (anOldName != null && anOldName.equals(aName)) {
      return true;
    }
    if (hasWithName(aName)) {
      return wasSet;
    }
    name = aName;
    wasSet = true;
    if (anOldName != null) {
      farmersByName.remove(anOldName);
    }
    farmersByName.put(aName, this);
    return wasSet;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template attribute_GetUnique */
  public static Farmer getWithName(String aName)
  {
    return farmersByName.get(aName);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithName(String aName)
  {
    return getWithName(aName) != null;
  }
  /* Code from template association_GetMany */
  public Cheese getCheese(int index)
  {
    Cheese aCheese = cheeses.get(index);
    return aCheese;
  }

  public List<Cheese> getCheeses()
  {
    List<Cheese> newCheeses = Collections.unmodifiableList(cheeses);
    return newCheeses;
  }

  public int numberOfCheeses()
  {
    int number = cheeses.size();
    return number;
  }

  public boolean hasCheeses()
  {
    boolean has = cheeses.size() > 0;
    return has;
  }

  public int indexOfCheese(Cheese aCheese)
  {
    int index = cheeses.indexOf(aCheese);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCheeses()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Cheese addCheese(Date aPurchaseDate, int aAge, int aMonthsToAge, boolean aIsSpoiled)
  {
    return new Cheese(aPurchaseDate, aAge, aMonthsToAge, aIsSpoiled, this);
  }

  public boolean addCheese(Cheese aCheese)
  {
    boolean wasAdded = false;
    if (cheeses.contains(aCheese)) { return false; }
    Farmer existingFarmer = aCheese.getFarmer();
    boolean isNewFarmer = existingFarmer != null && !this.equals(existingFarmer);
    if (isNewFarmer)
    {
      aCheese.setFarmer(this);
    }
    else
    {
      cheeses.add(aCheese);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCheese(Cheese aCheese)
  {
    boolean wasRemoved = false;
    //Unable to remove aCheese, as it must always have a farmer
    if (!this.equals(aCheese.getFarmer()))
    {
      cheeses.remove(aCheese);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCheeseAt(Cheese aCheese, int index)
  {
    boolean wasAdded = false;
    if(addCheese(aCheese))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCheeses()) { index = numberOfCheeses() - 1; }
      cheeses.remove(aCheese);
      cheeses.add(index, aCheese);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCheeseAt(Cheese aCheese, int index)
  {
    boolean wasAdded = false;
    if(cheeses.contains(aCheese))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCheeses()) { index = numberOfCheeses() - 1; }
      cheeses.remove(aCheese);
      cheeses.add(index, aCheese);
      wasAdded = true;
    }
    else
    {
      wasAdded = addCheeseAt(aCheese, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    farmersByName.remove(getName());
    for(int i=cheeses.size(); i > 0; i--)
    {
      Cheese aCheese = cheeses.get(i - 1);
      aCheese.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "postalCode" + ":" + getPostalCode()+ "," +
            "name" + ":" + getName()+ "]";
  }
}



//%% NEW FILE Robot BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 50 "model.ump"
// line 133 "model.ump"
public class Robot
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Robot Attributes
  private int direction;

  //Robot Associations
  private CheeseManager cheeseManager;
  private ShelfSlot shelfSlot;
  private List<RobotActionLog> robotActionLogs;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Robot(int aDirection, CheeseManager aCheeseManager, ShelfSlot aShelfSlot)
  {
    direction = aDirection;
    if (aCheeseManager == null || aCheeseManager.getRobot() != null)
    {
      throw new RuntimeException("Unable to create Robot due to aCheeseManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    cheeseManager = aCheeseManager;
    if (!setShelfSlot(aShelfSlot))
    {
      throw new RuntimeException("Unable to create Robot due to aShelfSlot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    robotActionLogs = new ArrayList<RobotActionLog>();
  }

  public Robot(int aDirection, ShelfSlot aShelfSlot)
  {
    direction = aDirection;
    cheeseManager = new CheeseManager(this);
    shelfSlot = new ShelfSlot(null);
    robotActionLogs = new ArrayList<RobotActionLog>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDirection(int aDirection)
  {
    boolean wasSet = false;
    direction = aDirection;
    wasSet = true;
    return wasSet;
  }

  public int getDirection()
  {
    return direction;
  }
  /* Code from template association_GetOne */
  public CheeseManager getCheeseManager()
  {
    return cheeseManager;
  }
  /* Code from template association_GetOne */
  public ShelfSlot getShelfSlot()
  {
    return shelfSlot;
  }
  /* Code from template association_GetMany */
  public RobotActionLog getRobotActionLog(int index)
  {
    RobotActionLog aRobotActionLog = robotActionLogs.get(index);
    return aRobotActionLog;
  }

  public List<RobotActionLog> getRobotActionLogs()
  {
    List<RobotActionLog> newRobotActionLogs = Collections.unmodifiableList(robotActionLogs);
    return newRobotActionLogs;
  }

  public int numberOfRobotActionLogs()
  {
    int number = robotActionLogs.size();
    return number;
  }

  public boolean hasRobotActionLogs()
  {
    boolean has = robotActionLogs.size() > 0;
    return has;
  }

  public int indexOfRobotActionLog(RobotActionLog aRobotActionLog)
  {
    int index = robotActionLogs.indexOf(aRobotActionLog);
    return index;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setShelfSlot(ShelfSlot aNewShelfSlot)
  {
    boolean wasSet = false;
    if (aNewShelfSlot != null)
    {
      shelfSlot = aNewShelfSlot;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRobotActionLogs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RobotActionLog addRobotActionLog(String aSeverity, String aTitle, String aDetails)
  {
    return new RobotActionLog(aSeverity, aTitle, aDetails, this);
  }

  public boolean addRobotActionLog(RobotActionLog aRobotActionLog)
  {
    boolean wasAdded = false;
    if (robotActionLogs.contains(aRobotActionLog)) { return false; }
    Robot existingRobot = aRobotActionLog.getRobot();
    boolean isNewRobot = existingRobot != null && !this.equals(existingRobot);
    if (isNewRobot)
    {
      aRobotActionLog.setRobot(this);
    }
    else
    {
      robotActionLogs.add(aRobotActionLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRobotActionLog(RobotActionLog aRobotActionLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aRobotActionLog, as it must always have a robot
    if (!this.equals(aRobotActionLog.getRobot()))
    {
      robotActionLogs.remove(aRobotActionLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRobotActionLogAt(RobotActionLog aRobotActionLog, int index)
  {
    boolean wasAdded = false;
    if(addRobotActionLog(aRobotActionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRobotActionLogs()) { index = numberOfRobotActionLogs() - 1; }
      robotActionLogs.remove(aRobotActionLog);
      robotActionLogs.add(index, aRobotActionLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRobotActionLogAt(RobotActionLog aRobotActionLog, int index)
  {
    boolean wasAdded = false;
    if(robotActionLogs.contains(aRobotActionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRobotActionLogs()) { index = numberOfRobotActionLogs() - 1; }
      robotActionLogs.remove(aRobotActionLog);
      robotActionLogs.add(index, aRobotActionLog);
      wasAdded = true;
    }
    else
    {
      wasAdded = addRobotActionLogAt(aRobotActionLog, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    CheeseManager existingCheeseManager = cheeseManager;
    cheeseManager = null;
    if (existingCheeseManager != null)
    {
      existingCheeseManager.delete();
    }
    shelfSlot = null;
    while (robotActionLogs.size() > 0)
    {
      RobotActionLog aRobotActionLog = robotActionLogs.get(robotActionLogs.size() - 1);
      aRobotActionLog.delete();
      robotActionLogs.remove(aRobotActionLog);
    }

  }


  public String toString()
  {
    return super.toString() + "["+
            "direction" + ":" + getDirection()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cheeseManager = "+(getCheeseManager()!=null?Integer.toHexString(System.identityHashCode(getCheeseManager())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "shelfSlot = "+(getShelfSlot()!=null?Integer.toHexString(System.identityHashCode(getShelfSlot())):"null");
  }
}
