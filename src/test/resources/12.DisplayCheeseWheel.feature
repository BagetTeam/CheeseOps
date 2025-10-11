Feature: Display Cheese Wheel (p10)
As the facility manager, I want to display cheese wheels from the system.

  Background:
    Given the following farmer exists in the system (p10)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Given the following purchase exists in the system (p10)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-04-04 |              5 | Six        | farmer@cheecse.fr |
    Given all cheese wheels from purchase 1 are created (p10)
    Given cheese wheel 1 is spoiled (p10)

  Scenario: Successfully display a cheese wheel from the system
    When the facility manager attempts to display cheese wheel 1 (p10)
    Then the following cheese wheels shall be presented (p10)
      | id | monthsAged | isSpoiled | purchaseDate | shelfId | column | row | isOrdered |
      |  1 | Six        | true      |   2025-04-04 |         |     -1 |  -1 | false     |
    Then the number of cheese wheels in the system shall be 5 (p10)

  Scenario: Successfully display a cheese wheel from a system that is on a shelf and part of an order
    Given the following shelf exists in the system (p10)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      2 |
    Given all locations are created for shelf "A12" (p10)
    Given the cheese wheel with id 2 is located at shelf "A12" at column 2 and row 1 (p10)
    Given the following order exists in the system (p10)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | companyName  |
      |      2025-04-04 |              5 | Six        |   2026-04-04 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 1 are part of order 2 (p10)
    When the facility manager attempts to display cheese wheel 2 (p10)
    Then the following cheese wheels shall be presented (p10)
      | id | monthsAged | isSpoiled | purchaseDate | shelfId | column | row | isOrdered |
      |  2 | Six        | false     |   2025-04-04 | A12     |      2 |   1 | true      |
    Then the number of cheese wheels in the system shall be 5 (p10)

  Scenario: Successfully display all cheese wheels in the system
    Given the following shelf exists in the system (p10)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      2 |
    Given all locations are created for shelf "A12" (p10)
    Given the cheese wheel with id 2 is located at shelf "A12" at column 2 and row 1 (p10)
    Given the cheese wheel with id 3 is located at shelf "A12" at column 1 and row 1 (p10)
    Given the following order exists in the system (p10)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | companyName  |
      |      2025-04-04 |              5 | Six        |   2026-04-04 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 1 are part of order 2 (p10)
    When the facility manager attempts to display all cheese wheels (p10)
    Then the number of cheese wheels in the system shall be 5 (p10)
    Then the following cheese wheels shall be presented (p10)
      | id | monthsAged | isSpoiled | purchaseDate | shelfId | column | row | isOrdered |
      |  1 | Six        | true      |   2025-04-04 |         |     -1 |  -1 | false     |
      |  2 | Six        | false     |   2025-04-04 | A12     |      2 |   1 | true      |
      |  3 | Six        | false     |   2025-04-04 | A12     |      1 |   1 | true      |
      |  4 | Six        | false     |   2025-04-04 |         |     -1 |  -1 | true      |
      |  5 | Six        | false     |   2025-04-04 |         |     -1 |  -1 | true      |
    Then the number of cheese wheels in the system shall be 5 (p10)

  Scenario: Unsuccessfully display a cheese wheel that does not exist
    When the facility manager attempts to display cheese wheel 10 (p10)
    Then no cheese wheels shall be presented (p10)
    Then the number of cheese wheels in the system shall be 5 (p10)
