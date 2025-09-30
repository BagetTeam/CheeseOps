Feature: Assign and Remove Cheese Wheel to/from Shelf Location (p7)
As the facility manager, I want to assign cheese wheels to shelf locations and remove them when needed.

  Background:
    Given the following farmer exists in the system (p7)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Given the following shelf exists in the system (p7)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      2 |
    Given all locations are created for shelf "A12" (p7)
    Given the following purchase exists in the system (p7)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-04-04 |              5 | Six        | farmer@cheecse.fr |
    Given all cheese wheels from purchase 1 are created (p7)

  Scenario: Successfully assign a cheese wheel to an empty shelf location
    When the facility manager attempts to assign cheese wheel 1 to shelf location with column 2 and row 1 of shelf "A12" (p7)
    Then the cheese wheel 1 shall be at shelf location with column 2 and row 1 of shelf "A12" (p7)
    Then the number of cheese wheels on shelf "A12" shall be 1 (p7)

  Scenario: Successfully assign a cheese wheel to a new empty shelf location on the same shelf
    Given cheese wheel 1 is at shelf location with column 2 and row 1 of shelf "A12" (p7)
    When the facility manager attempts to assign cheese wheel 1 to shelf location with column 1 and row 1 of shelf "A12" (p7)
    Then the cheese wheel 1 shall be at shelf location with column 1 and row 1 of shelf "A12" (p7)
    Then the number of cheese wheels on shelf "A12" shall be 1 (p7)

  Scenario: Successfully assign a cheese wheel to a new empty shelf location on a different shelf
    Given the following shelf exists in the system (p7)
      | id  | nrColumns | nrRows |
      | B11 |         1 |      1 |
    Given all locations are created for shelf "B11" (p7)
    Given cheese wheel 1 is at shelf location with column 2 and row 1 of shelf "A12" (p7)
    When the facility manager attempts to assign cheese wheel 1 to shelf location with column 1 and row 1 of shelf "B11" (p7)
    Then the cheese wheel 1 shall be at shelf location with column 1 and row 1 of shelf "B11" (p7)
    Then the number of cheese wheels on shelf "B11" shall be 1 (p7)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p7)

  Scenario: Unsuccessfully assign a cheese wheel that does not exist
    When the facility manager attempts to assign cheese wheel 10 to shelf location with column 2 and row 1 of shelf "A12" (p7)
    Then the error "The cheese wheel with id 10 does not exist." shall be raised (p7)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p7)

  Scenario: Unsuccessfully assign a spoiled cheese wheel to a shelf location
    Given cheese wheel 2 is spoiled (p7)
    When the facility manager attempts to assign cheese wheel 2 to shelf location with column 2 and row 1 of shelf "A12" (p7)
    Then the error "Cannot place a spoiled cheese wheel on a shelf." shall be raised (p7)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p7)

  Scenario: Unsuccessfully assign a cheese wheel to a non-existent shelf location
    When the facility manager attempts to assign cheese wheel 1 to shelf location with column 6 and row 1 of shelf "A12" (p7)
    Then the error "The shelf location does not exist." shall be raised (p7)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p7)

  Scenario: Unsuccessfully assign a cheese wheel to an occupied shelf location
    Given cheese wheel 1 is at shelf location with column 2 and row 1 of shelf "A12" (p7)
    When the facility manager attempts to assign cheese wheel 3 to shelf location with column 2 and row 1 of shelf "A12" (p7)
    Then the error "The shelf location is already occupied." shall be raised (p7)
    Then the cheese wheel 1 shall be at shelf location with column 2 and row 1 of shelf "A12" (p7)
    Then the number of cheese wheels on shelf "A12" shall be 1 (p7)

  Scenario: Successfully remove a cheese wheel from a shelf location
    Given cheese wheel 1 is at shelf location with column 2 and row 1 of shelf "A12" (p7)
    When the facility manager attempts to remove cheese wheel 1 from its shelf location (p7)
    Then cheese wheel 1 shall not be on any shelf (p7)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p7)

  Scenario: Unsuccessfully remove a cheese wheel that does not exist
    When the facility manager attempts to remove cheese wheel 10 from its shelf location (p7)
    Then the error "The cheese wheel with id 10 does not exist." shall be raised (p7)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p7)

  Scenario: Unsuccessfully remove a cheese wheel that is not on a shelf
    When the facility manager attempts to remove cheese wheel 3 from its shelf location (p7)
    Then the error "The cheese wheel is not on any shelf." shall be raised (p7)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p7)
