package ca.mcgill.ecse.cheecsemanager.model;
// %% NEW FILE FacilityManager BEGINS HERE %%

/* PLEASE DO NOT EDIT THIS CODE */

/*
 * This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling
 * language!
 */

import java.util.*;
import java.sql.Date;

// line 10 "model.ump"
// line 98 "model.ump"
public class FacilityManager extends User {

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // FacilityManager Associations
    private List<Farmer> farmers;
    private List<Company> companies;
    private List<Shelf> shelfs;
    private List<Order> orders;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public FacilityManager(String aEmail, String aPassword) {
        super(aEmail, aPassword);
        farmers = new ArrayList<Farmer>();
        companies = new ArrayList<Company>();
        shelfs = new ArrayList<Shelf>();
        orders = new ArrayList<Order>();
    }

    // ------------------------
    // INTERFACE
    // ------------------------
    /* Code from template association_GetMany */
    public Farmer getFarmer(int index) {
        Farmer aFarmer = farmers.get(index);
        return aFarmer;
    }

    public List<Farmer> getFarmers() {
        List<Farmer> newFarmers = Collections.unmodifiableList(farmers);
        return newFarmers;
    }

    public int numberOfFarmers() {
        int number = farmers.size();
        return number;
    }

    public boolean hasFarmers() {
        boolean has = farmers.size() > 0;
        return has;
    }

    public int indexOfFarmer(Farmer aFarmer) {
        int index = farmers.indexOf(aFarmer);
        return index;
    }

    /* Code from template association_GetMany */
    public Company getCompany(int index) {
        Company aCompany = companies.get(index);
        return aCompany;
    }

    public List<Company> getCompanies() {
        List<Company> newCompanies = Collections.unmodifiableList(companies);
        return newCompanies;
    }

    public int numberOfCompanies() {
        int number = companies.size();
        return number;
    }

    public boolean hasCompanies() {
        boolean has = companies.size() > 0;
        return has;
    }

    public int indexOfCompany(Company aCompany) {
        int index = companies.indexOf(aCompany);
        return index;
    }

    /* Code from template association_GetMany */
    public Shelf getShelf(int index) {
        Shelf aShelf = shelfs.get(index);
        return aShelf;
    }

    public List<Shelf> getShelfs() {
        List<Shelf> newShelfs = Collections.unmodifiableList(shelfs);
        return newShelfs;
    }

    public int numberOfShelfs() {
        int number = shelfs.size();
        return number;
    }

    public boolean hasShelfs() {
        boolean has = shelfs.size() > 0;
        return has;
    }

    public int indexOfShelf(Shelf aShelf) {
        int index = shelfs.indexOf(aShelf);
        return index;
    }

    /* Code from template association_GetMany */
    public Order getOrder(int index) {
        Order aOrder = orders.get(index);
        return aOrder;
    }

    public List<Order> getOrders() {
        List<Order> newOrders = Collections.unmodifiableList(orders);
        return newOrders;
    }

    public int numberOfOrders() {
        int number = orders.size();
        return number;
    }

    public boolean hasOrders() {
        boolean has = orders.size() > 0;
        return has;
    }

    public int indexOfOrder(Order aOrder) {
        int index = orders.indexOf(aOrder);
        return index;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfFarmers() {
        return 0;
    }

    /* Code from template association_AddUnidirectionalMany */
    public boolean addFarmer(Farmer aFarmer) {
        boolean wasAdded = false;
        if (farmers.contains(aFarmer)) {
            return false;
        }
        farmers.add(aFarmer);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeFarmer(Farmer aFarmer) {
        boolean wasRemoved = false;
        if (farmers.contains(aFarmer)) {
            farmers.remove(aFarmer);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addFarmerAt(Farmer aFarmer, int index) {
        boolean wasAdded = false;
        if (addFarmer(aFarmer)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfFarmers()) {
                index = numberOfFarmers() - 1;
            }
            farmers.remove(aFarmer);
            farmers.add(index, aFarmer);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveFarmerAt(Farmer aFarmer, int index) {
        boolean wasAdded = false;
        if (farmers.contains(aFarmer)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfFarmers()) {
                index = numberOfFarmers() - 1;
            }
            farmers.remove(aFarmer);
            farmers.add(index, aFarmer);
            wasAdded = true;
        } else {
            wasAdded = addFarmerAt(aFarmer, index);
        }
        return wasAdded;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfCompanies() {
        return 0;
    }

    /* Code from template association_AddUnidirectionalMany */
    public boolean addCompany(Company aCompany) {
        boolean wasAdded = false;
        if (companies.contains(aCompany)) {
            return false;
        }
        companies.add(aCompany);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeCompany(Company aCompany) {
        boolean wasRemoved = false;
        if (companies.contains(aCompany)) {
            companies.remove(aCompany);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addCompanyAt(Company aCompany, int index) {
        boolean wasAdded = false;
        if (addCompany(aCompany)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfCompanies()) {
                index = numberOfCompanies() - 1;
            }
            companies.remove(aCompany);
            companies.add(index, aCompany);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveCompanyAt(Company aCompany, int index) {
        boolean wasAdded = false;
        if (companies.contains(aCompany)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfCompanies()) {
                index = numberOfCompanies() - 1;
            }
            companies.remove(aCompany);
            companies.add(index, aCompany);
            wasAdded = true;
        } else {
            wasAdded = addCompanyAt(aCompany, index);
        }
        return wasAdded;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfShelfs() {
        return 0;
    }

    /* Code from template association_AddManyToOne */
    public Shelf addShelf(String aId) {
        return new Shelf(aId, this);
    }

    public boolean addShelf(Shelf aShelf) {
        boolean wasAdded = false;
        if (shelfs.contains(aShelf)) {
            return false;
        }
        FacilityManager existingFacilityManager = aShelf.getFacilityManager();
        boolean isNewFacilityManager = existingFacilityManager != null && !this.equals(existingFacilityManager);
        if (isNewFacilityManager) {
            aShelf.setFacilityManager(this);
        } else {
            shelfs.add(aShelf);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeShelf(Shelf aShelf) {
        boolean wasRemoved = false;
        // Unable to remove aShelf, as it must always have a facilityManager
        if (!this.equals(aShelf.getFacilityManager())) {
            shelfs.remove(aShelf);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addShelfAt(Shelf aShelf, int index) {
        boolean wasAdded = false;
        if (addShelf(aShelf)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfShelfs()) {
                index = numberOfShelfs() - 1;
            }
            shelfs.remove(aShelf);
            shelfs.add(index, aShelf);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveShelfAt(Shelf aShelf, int index) {
        boolean wasAdded = false;
        if (shelfs.contains(aShelf)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfShelfs()) {
                index = numberOfShelfs() - 1;
            }
            shelfs.remove(aShelf);
            shelfs.add(index, aShelf);
            wasAdded = true;
        } else {
            wasAdded = addShelfAt(aShelf, index);
        }
        return wasAdded;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfOrders() {
        return 0;
    }

    /* Code from template association_AddUnidirectionalMany */
    public boolean addOrder(Order aOrder) {
        boolean wasAdded = false;
        if (orders.contains(aOrder)) {
            return false;
        }
        orders.add(aOrder);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeOrder(Order aOrder) {
        boolean wasRemoved = false;
        if (orders.contains(aOrder)) {
            orders.remove(aOrder);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addOrderAt(Order aOrder, int index) {
        boolean wasAdded = false;
        if (addOrder(aOrder)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfOrders()) {
                index = numberOfOrders() - 1;
            }
            orders.remove(aOrder);
            orders.add(index, aOrder);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveOrderAt(Order aOrder, int index) {
        boolean wasAdded = false;
        if (orders.contains(aOrder)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfOrders()) {
                index = numberOfOrders() - 1;
            }
            orders.remove(aOrder);
            orders.add(index, aOrder);
            wasAdded = true;
        } else {
            wasAdded = addOrderAt(aOrder, index);
        }
        return wasAdded;
    }

    public void delete() {
        farmers.clear();
        companies.clear();
        for (int i = shelfs.size(); i > 0; i--) {
            Shelf aShelf = shelfs.get(i - 1);
            aShelf.delete();
        }
        orders.clear();
        super.delete();
    }

}
