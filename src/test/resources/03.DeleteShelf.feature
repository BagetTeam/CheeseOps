Feature: Delete Shelf (p12)
As the facility manager, I want to delete a shelf from the system.

  Background:
    Given the following shelf exists in the system (p12)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      1 |
      | B23 |         6 |      2 |
    Given all locations are created for shelf "A12" (p12)
    Given all locations are created for shelf "B23" (p12)
    Given the following farmer exists in the system (p12)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Given the following purchase exists in the system (p12)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-09-01 |              2 | Six        | farmer@cheecse.fr |
    Given all cheese wheels from purchase 1 are created (p12)

  Scenario: Successfully delete a shelf from the system
    When the facility manager attempts to delete from the system the shelf with id "A12" (p12)
    Then the number of shelves in the system shall be 1 (p12)
    Then the following shelves shall exist in the system (p12)
      | id  | nrColumns | nrRows |
      | B23 |         6 |      2 |
    Then the number of locations associated with the shelf "A12" shall be 0 (p12)

  Scenario Outline: Unsuccessfully delete a shelf that does not exist in the system
    When the facility manager attempts to delete from the system the shelf with id "<id>" (p12)
    Then the number of shelves in the system shall be 2 (p12)
    Then the following shelves shall exist in the system (p12)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      1 |
      | B23 |         6 |      2 |
    Then the number of locations associated with the shelf "<id>" shall be 0 (p12)
    Then the error "The shelf <id> does not exist." shall be raised (p12)

    Examples:
      | id  |
      | C45 |
      | Z99 |

  Scenario: Unsuccessfully delete a shelf that has cheese wheels stored on it
    Given the following cheese wheel exists on shelf "B23" (p12)
      | column | row | id |
      |      1 |   1 |  1 |
    When the facility manager attempts to delete from the system the shelf with id "B23" (p12)
    Then the number of shelves in the system shall be 2 (p12)
    Then the following shelves shall exist in the system (p12)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      1 |
      | B23 |         6 |      2 |
    Then the number of locations associated with the shelf "B23" shall be 12 (p12)
    Then the following cheese wheels shall exist in the system (p12)
      | id | monthsAged | isSpoiled | purchaseId | shelfID | column | row |
      |  1 | Six        | false     |          1 | B23     |      1 |   1 |
      |  2 | Six        | false     |          1 |         |     -1 |  -1 |
    Then the error "Cannot delete a shelf that contains cheese wheels." shall be raised (p12)
