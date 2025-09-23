package ca.mcgill.ecse.cheecsemanager.model;
// %% NEW FILE User BEGINS HERE %%

/* PLEASE DO NOT EDIT THIS CODE */

/*
 * This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling
 * language!
 */

import java.util.*;

// line 78 "model.ump"
// line 156 "model.ump"
public abstract class User {

    // ------------------------
    // STATIC VARIABLES
    // ------------------------

    private static Map<String, User> usersByEmail = new HashMap<String, User>();

    // ------------------------
    // MEMBER VARIABLES
    // ------------------------

    // User Attributes
    private String email;
    private String password;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public User(String aEmail, String aPassword) {
        password = aPassword;
        if (!setEmail(aEmail)) {
            throw new RuntimeException(
                    "Cannot create due to duplicate email. See https://manual.umple.org?RE003ViolationofUniqueness.html");
        }
    }

    // ------------------------
    // INTERFACE
    // ------------------------

    public boolean setEmail(String aEmail) {
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

    public boolean setPassword(String aPassword) {
        boolean wasSet = false;
        password = aPassword;
        wasSet = true;
        return wasSet;
    }

    public String getEmail() {
        return email;
    }

    /* Code from template attribute_GetUnique */
    public static User getWithEmail(String aEmail) {
        return usersByEmail.get(aEmail);
    }

    /* Code from template attribute_HasUnique */
    public static boolean hasWithEmail(String aEmail) {
        return getWithEmail(aEmail) != null;
    }

    public String getPassword() {
        return password;
    }

    public void delete() {
        usersByEmail.remove(getEmail());
    }

    public String toString() {
        return super.toString() + "[" +
                "email" + ":" + getEmail() + "," +
                "password" + ":" + getPassword() + "]";
    }
}
