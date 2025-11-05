/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling
 * language!*/

package ca.mcgill.ecse.cheecsemanager.controller;

// line 3 "../../../../../../LogEntryTransferObject.ump"
// line 10 "../../../../../../LogEntryTransferObject.ump"
public class TOLogEntry {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  // TOLogEntry Attributes
  private String description;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOLogEntry(String aDescription) { description = aDescription; }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDescription(String aDescription) {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public String getDescription() { return description; }

  public void delete() {}

  public String toString() {
    return super.toString() + "["
        + "description"
        + ":" + getDescription() + "]";
  }
}
