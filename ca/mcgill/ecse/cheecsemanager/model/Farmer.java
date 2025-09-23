package ca.mcgill.ecse.cheecsemanager.model;
// %% NEW FILE Farmer BEGINS HERE %%

/* PLEASE DO NOT EDIT THIS CODE */

/*
 * This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling
 * language!
 */

import java.util.*;
import java.sql.Date;

// line 2 "model.ump"
// line 90 "model.ump"
public class Farmer extends User {

    // ------------------------
    // STATIC VARIABLES
    // ------------------------

    private static Map<String, Farmer> farmersByName = new HashMap<String, Farmer>();

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // Farmer Attributes
    private String postalCode;
    private String name;

    // Farmer Associations
    private List<Cheese> cheeses;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public Farmer(String aEmail, String aPassword, String aPostalCode, String aName) {
        super(aEmail, aPassword);
        postalCode = aPostalCode;
        if (!setName(aName)) {
            throw new RuntimeException(
                    "Cannot create due to duplicate name. See https://manual.umple.org?RE003ViolationofUniqueness.html");
        }
        cheeses = new ArrayList<Cheese>();
    }

    // ------------------------
    // INTERFACE
    // ------------------------

    public boolean setPostalCode(String aPostalCode) {
        boolean wasSet = false;
        postalCode = aPostalCode;
        wasSet = true;
        return wasSet;
    }

    public boolean setName(String aName) {
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

    public String getPostalCode() {
        return postalCode;
    }

    public String getName() {
        return name;
    }

    /* Code from template attribute_GetUnique */
    public static Farmer getWithName(String aName) {
        return farmersByName.get(aName);
    }

    /* Code from template attribute_HasUnique */
    public static boolean hasWithName(String aName) {
        return getWithName(aName) != null;
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

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfCheeses() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public Cheese addCheese(Date aPurchaseDate, int aAge, int aMonthsToAge, boolean aIsSpoiled) {
        return new Cheese(aPurchaseDate, aAge, aMonthsToAge, aIsSpoiled, this);
    }

    public boolean addCheese(Cheese aCheese) {
        boolean wasAdded = false;
        if (cheeses.contains(aCheese)) {
            return false;
        }
        Farmer existingFarmer = aCheese.getFarmer();
        boolean isNewFarmer = existingFarmer != null && !this.equals(existingFarmer);
        if (isNewFarmer) {
            aCheese.setFarmer(this);
        } else {
            cheeses.add(aCheese);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeCheese(Cheese aCheese) {
        boolean wasRemoved = false;
        // Unable to remove aCheese, as it must always have a farmer
        if (!this.equals(aCheese.getFarmer())) {
            cheeses.remove(aCheese);
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

    public void delete() {
        farmersByName.remove(getName());
        for (int i = cheeses.size(); i > 0; i--) {
            Cheese aCheese = cheeses.get(i - 1);
            aCheese.delete();
        }
        super.delete();
    }

    public String toString() {
        return super.toString() + "[" +
                "postalCode" + ":" + getPostalCode() + "," +
                "name" + ":" + getName() + "]";
    }
}
