package ca.mcgill.ecse.cheecsemanager.model;
//%% NEW FILE Order BEGINS HERE %%

/*PLEASE DO NOT EDIT THIS CODE*/

/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

import java.sql.Date;
import java.util.*;

// line 40 "model.ump"
// line 126 "model.ump"
public class Order {

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // Order Attributes
    private int kind;
    private int numberOfMissingCheese;
    private Date purchaseDate;
    private Date deliveryDate;

    // Order Associations
    private List<Cheese> cheeses;
    private Company company;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public Order(int aKind, int aNumberOfMissingCheese, Date aPurchaseDate, Date aDeliveryDate, Company aCompany) {
        kind = aKind;
        numberOfMissingCheese = aNumberOfMissingCheese;
        purchaseDate = aPurchaseDate;
        deliveryDate = aDeliveryDate;
        cheeses = new ArrayList<Cheese>();
        boolean didAddCompany = setCompany(aCompany);
        if (!didAddCompany) {
            throw new RuntimeException(
                    "Unable to create order due to company. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    // ------------------------
    // INTERFACE
    // ------------------------

    public boolean setKind(int aKind) {
        boolean wasSet = false;
        kind = aKind;
        wasSet = true;
        return wasSet;
    }

    public boolean setNumberOfMissingCheese(int aNumberOfMissingCheese) {
        boolean wasSet = false;
        numberOfMissingCheese = aNumberOfMissingCheese;
        wasSet = true;
        return wasSet;
    }

    public boolean setPurchaseDate(Date aPurchaseDate) {
        boolean wasSet = false;
        purchaseDate = aPurchaseDate;
        wasSet = true;
        return wasSet;
    }

    public boolean setDeliveryDate(Date aDeliveryDate) {
        boolean wasSet = false;
        deliveryDate = aDeliveryDate;
        wasSet = true;
        return wasSet;
    }

    public int getKind() {
        return kind;
    }

    public int getNumberOfMissingCheese() {
        return numberOfMissingCheese;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /* Code from template association_GetMany */
    public Cheese getCheese(int index) {
        Cheese aCheese = cheeses.get(index);
        return aCheese;
    }

    public List<Cheese> getCheeses() {
        List<Cheese> newCheeses = Collections.unmodifiableList(cheeses);
        return newCheeses;
    }

    public int numberOfCheeses() {
        int number = cheeses.size();
        return number;
    }

    public boolean hasCheeses() {
        boolean has = cheeses.size() > 0;
        return has;
    }

    public int indexOfCheese(Cheese aCheese) {
        int index = cheeses.indexOf(aCheese);
        return index;
    }

    /* Code from template association_GetOne */
    public Company getCompany() {
        return company;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfCheeses() {
        return 0;
    }

    /* Code from template association_AddManyToOptionalOne */
    public boolean addCheese(Cheese aCheese) {
        boolean wasAdded = false;
        if (cheeses.contains(aCheese)) {
            return false;
        }
        Order existingOrder = aCheese.getOrder();
        if (existingOrder == null) {
            aCheese.setOrder(this);
        } else if (!this.equals(existingOrder)) {
            existingOrder.removeCheese(aCheese);
            addCheese(aCheese);
        } else {
            cheeses.add(aCheese);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeCheese(Cheese aCheese) {
        boolean wasRemoved = false;
        if (cheeses.contains(aCheese)) {
            cheeses.remove(aCheese);
            aCheese.setOrder(null);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addCheeseAt(Cheese aCheese, int index) {
        boolean wasAdded = false;
        if (addCheese(aCheese)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfCheeses()) {
                index = numberOfCheeses() - 1;
            }
            cheeses.remove(aCheese);
            cheeses.add(index, aCheese);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveCheeseAt(Cheese aCheese, int index) {
        boolean wasAdded = false;
        if (cheeses.contains(aCheese)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfCheeses()) {
                index = numberOfCheeses() - 1;
            }
            cheeses.remove(aCheese);
            cheeses.add(index, aCheese);
            wasAdded = true;
        } else {
            wasAdded = addCheeseAt(aCheese, index);
        }
        return wasAdded;
    }

    /* Code from template association_SetOneToMany */
    public boolean setCompany(Company aCompany) {
        boolean wasSet = false;
        if (aCompany == null) {
            return wasSet;
        }

        Company existingCompany = company;
        company = aCompany;
        if (existingCompany != null && !existingCompany.equals(aCompany)) {
            existingCompany.removeOrder(this);
        }
        company.addOrder(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        while (!cheeses.isEmpty()) {
            cheeses.get(0).setOrder(null);
        }
        Company placeholderCompany = company;
        this.company = null;
        if (placeholderCompany != null) {
            placeholderCompany.removeOrder(this);
        }
    }

    public String toString() {
        return super.toString() + "[" +
                "kind" + ":" + getKind() + "," +
                "numberOfMissingCheese" + ":" + getNumberOfMissingCheese() + "]"
                + System.getProperties().getProperty("line.separator") +
                "  " + "purchaseDate" + "="
                + (getPurchaseDate() != null
                        ? !getPurchaseDate().equals(this) ? getPurchaseDate().toString().replaceAll("  ", "    ")
                                : "this"
                        : "null")
                + System.getProperties().getProperty("line.separator") +
                "  " + "deliveryDate" + "="
                + (getDeliveryDate() != null
                        ? !getDeliveryDate().equals(this) ? getDeliveryDate().toString().replaceAll("  ", "    ")
                                : "this"
                        : "null")
                + System.getProperties().getProperty("line.separator") +
                "  " + "company = "
                + (getCompany() != null ? Integer.toHexString(System.identityHashCode(getCompany())) : "null");
    }
}
