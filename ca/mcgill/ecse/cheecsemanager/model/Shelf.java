package ca.mcgill.ecse.cheecsemanager.model;
// %% NEW FILE Shelf BEGINS HERE %%

/* PLEASE DO NOT EDIT THIS CODE */

/*
 * This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling
 * language!
 */

import java.util.*;

// line 17 "model.ump"
// line 106 "model.ump"
public class Shelf {

    // ------------------------
    // STATIC VARIABLES
    // ------------------------

    private static Map<String, Shelf> shelfsById = new HashMap<String, Shelf>();

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // Shelf Attributes
    private String id;

    // Shelf Associations
    private List<ShelfSlot> shelfSlots;
    private FacilityManager facilityManager;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public Shelf(String aId, FacilityManager aFacilityManager) {
        if (!setId(aId)) {
            throw new RuntimeException(
                    "Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
        }
        shelfSlots = new ArrayList<ShelfSlot>();
        boolean didAddFacilityManager = setFacilityManager(aFacilityManager);
        if (!didAddFacilityManager) {
            throw new RuntimeException(
                    "Unable to create shelf due to facilityManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    // ------------------------
    // INTERFACE
    // ------------------------

    public boolean setId(String aId) {
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

    public String getId() {
        return id;
    }

    /* Code from template attribute_GetUnique */
    public static Shelf getWithId(String aId) {
        return shelfsById.get(aId);
    }

    /* Code from template attribute_HasUnique */
    public static boolean hasWithId(String aId) {
        return getWithId(aId) != null;
    }

    /* Code from template association_GetMany */
    public ShelfSlot getShelfSlot(int index) {
        ShelfSlot aShelfSlot = shelfSlots.get(index);
        return aShelfSlot;
    }

    public List<ShelfSlot> getShelfSlots() {
        List<ShelfSlot> newShelfSlots = Collections.unmodifiableList(shelfSlots);
        return newShelfSlots;
    }

    public int numberOfShelfSlots() {
        int number = shelfSlots.size();
        return number;
    }

    public boolean hasShelfSlots() {
        boolean has = shelfSlots.size() > 0;
        return has;
    }

    public int indexOfShelfSlot(ShelfSlot aShelfSlot) {
        int index = shelfSlots.indexOf(aShelfSlot);
        return index;
    }

    /* Code from template association_GetOne */
    public FacilityManager getFacilityManager() {
        return facilityManager;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfShelfSlots() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public ShelfSlot addShelfSlot(int aRow, int aColumn, Cheese aCheese) {
        return new ShelfSlot(aRow, aColumn, aCheese, this);
    }

    public boolean addShelfSlot(ShelfSlot aShelfSlot) {
        boolean wasAdded = false;
        if (shelfSlots.contains(aShelfSlot)) {
            return false;
        }
        Shelf existingShelf = aShelfSlot.getShelf();
        boolean isNewShelf = existingShelf != null && !this.equals(existingShelf);
        if (isNewShelf) {
            aShelfSlot.setShelf(this);
        } else {
            shelfSlots.add(aShelfSlot);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeShelfSlot(ShelfSlot aShelfSlot) {
        boolean wasRemoved = false;
        // Unable to remove aShelfSlot, as it must always have a shelf
        if (!this.equals(aShelfSlot.getShelf())) {
            shelfSlots.remove(aShelfSlot);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addShelfSlotAt(ShelfSlot aShelfSlot, int index) {
        boolean wasAdded = false;
        if (addShelfSlot(aShelfSlot)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfShelfSlots()) {
                index = numberOfShelfSlots() - 1;
            }
            shelfSlots.remove(aShelfSlot);
            shelfSlots.add(index, aShelfSlot);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveShelfSlotAt(ShelfSlot aShelfSlot, int index) {
        boolean wasAdded = false;
        if (shelfSlots.contains(aShelfSlot)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfShelfSlots()) {
                index = numberOfShelfSlots() - 1;
            }
            shelfSlots.remove(aShelfSlot);
            shelfSlots.add(index, aShelfSlot);
            wasAdded = true;
        } else {
            wasAdded = addShelfSlotAt(aShelfSlot, index);
        }
        return wasAdded;
    }

    /* Code from template association_SetOneToMany */
    public boolean setFacilityManager(FacilityManager aFacilityManager) {
        boolean wasSet = false;
        if (aFacilityManager == null) {
            return wasSet;
        }

        FacilityManager existingFacilityManager = facilityManager;
        facilityManager = aFacilityManager;
        if (existingFacilityManager != null && !existingFacilityManager.equals(aFacilityManager)) {
            existingFacilityManager.removeShelf(this);
        }
        facilityManager.addShelf(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        shelfsById.remove(getId());
        while (shelfSlots.size() > 0) {
            ShelfSlot aShelfSlot = shelfSlots.get(shelfSlots.size() - 1);
            aShelfSlot.delete();
            shelfSlots.remove(aShelfSlot);
        }

        FacilityManager placeholderFacilityManager = facilityManager;
        this.facilityManager = null;
        if (placeholderFacilityManager != null) {
            placeholderFacilityManager.removeShelf(this);
        }
    }

    public String toString() {
        return super.toString() + "[" +
                "id" + ":" + getId() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "facilityManager = "
                + (getFacilityManager() != null ? Integer.toHexString(System.identityHashCode(getFacilityManager()))
                        : "null");
    }
}
