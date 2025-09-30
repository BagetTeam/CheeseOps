Feature: Update Cheese Wheel (p6)
As the facility manager, I want to update a cheese wheel in the system to mark it as spoiled.

  Background:
    Given the following farmer exists in the system (p6)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Given the following purchase exists in the system (p6)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-04-04 |              5 | Six        | farmer@cheecse.fr |
    Given all cheese wheels from purchase 1 are created (p6)
    Given cheese wheel 1 is spoiled (p6)

  Scenario Outline: Successfully update a cheese wheel in the system
    When the facility manager attempts to update cheese wheel <id> in the system with isSpoiled "<updatedIsSpoiled>" and monthsAged "<updatedMonthsAged>" (p6)
    Then the number of cheese wheels in the system shall be 5 (p6)
    Then the cheese wheel <id> with monthsAged "<updatedMonthsAged>", isSpoiled "<updatedIsSpoiled>", and purchaseId 1 shall exist in the system (p6)
    Then the purchase 1 shall have 5 cheese wheels (p6)

    Examples:
      | id | updatedIsSpoiled | updatedMonthsAged |
      |  1 | false            | ThirtySix         |
      |  2 | true             | Twelve            |

  Scenario: Successfully update a cheese wheel on a shelf to be spoiled
    Given the following shelf exists in the system (p6)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      2 |
    Given all locations are created for shelf "A12" (p6)
    Given cheese wheel 2 is at shelf location with column 2 and row 1 of shelf "A12" (p6)
    When the facility manager attempts to update cheese wheel 2 in the system with isSpoiled "true" and monthsAged "Six" (p6)
    Then the number of cheese wheels in the system shall be 5 (p6)
    Then the cheese wheel 2 with monthsAged "Six", isSpoiled "true", and purchaseId 1 shall exist in the system (p6)
    Then the purchase 1 shall have 5 cheese wheels (p6)
    Then the cheese wheel 2 shall not be on any shelf (p6)
    Then the number of cheese wheels on shelf "A12" shall be 0 (p6)

  Scenario: Successfully update a cheese wheel in an order to be spoiled
    Given the following order exists in the system (p6)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | companyName  |
      |      2025-04-04 |              5 | Six        |   2025-10-10 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 1 are added to order 2 (p6)
    When the facility manager attempts to update cheese wheel 3 in the system with isSpoiled "true" and monthsAged "Six" (p6)
    Then the number of cheese wheels in the system shall be 5 (p6)
    Then the cheese wheel 3 with monthsAged "Six", isSpoiled "true", and purchaseId 1 shall exist in the system (p6)
    Then the purchase 1 shall have 5 cheese wheels (p6)
    Then the cheese wheel 3 shall not be part of any order (p6)
    Then the order 2 shall have 3 cheese wheels (p6)

  Scenario: Successfully update a cheese wheel in an order to a new months aged value
    Given the following order exists in the system (p6)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | companyName  |
      |      2025-04-04 |              5 | Six        |   2025-10-10 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 1 are added to order 2 (p6)
    When the facility manager attempts to update cheese wheel 4 in the system with isSpoiled "false" and monthsAged "Twelve" (p6)
    Then the number of cheese wheels in the system shall be 5 (p6)
    Then the cheese wheel 4 with monthsAged "Twelve", isSpoiled "false", and purchaseId 1 shall exist in the system (p6)
    Then the purchase 1 shall have 5 cheese wheels (p6)
    Then the cheese wheel 4 shall not be part of any order (p6)
    Then the order 2 shall have 3 cheese wheels (p6)

  Scenario: Unsuccessfully update a cheese wheel with an invalid monthsAged value
    When the facility manager attempts to update cheese wheel 2 in the system with isSpoiled "true" and monthsAged "notALiteral" (p6)
    Then the number of cheese wheels in the system shall be 5 (p6)
    Then the cheese wheel 2 with monthsAged "Six", isSpoiled "false", and purchaseId 1 shall exist in the system (p6)
    Then the purchase 1 shall have 5 cheese wheels (p6)
    Then the error "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix." shall be raised (p6)

  Scenario Outline: Unsuccessfully update a cheese wheel to decrease its monthsAged value
    Given the following purchase exists in the system (p6)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-04-04 |              2 | ThirtySix  | farmer@cheecse.fr |
    Given all cheese wheels from purchase 2 are created (p6)
    When the facility manager attempts to update cheese wheel <id> in the system with isSpoiled "<updatedIsSpoiled>" and monthsAged "<updatedMonthsAged>" (p6)
    Then the number of cheese wheels in the system shall be 7 (p6)
    Then the cheese wheel <id> with monthsAged "<originalMonthsAged>", isSpoiled "<originalIsSpoiled>", and purchaseId <originalPurchaseId> shall exist in the system (p6)
    Then the purchase <originalPurchaseId> shall have 2 cheese wheels (p6)
    Then the error "Cannot decrease the monthsAged of a cheese wheel." shall be raised (p6)

    Examples:
      | id | updatedIsSpoiled | updatedMonthsAged | originalMonthsAged | originalIsSpoiled | originalPurchaseId |
      |  6 | false            | Twelve            | ThirtySix          | false             |                  2 |
      |  6 | false            | TwentyFour        | ThirtySix          | false             |                  2 |
      |  7 | true             | Six               | ThirtySix          | false             |                  2 |

  Scenario: Unsuccessfully update a cheese wheel that does not exist in the system
    When the facility manager attempts to update cheese wheel 30 in the system with isSpoiled "false" and monthsAged "Twelve" (p6)
    Then the number of cheese wheels in the system shall be 5 (p6)
    Then the cheese wheel 30 shall not exist in the system (p6)
    Then the purchase 1 shall have 5 cheese wheels (p6)
    Then the error "The cheese wheel with id 30 does not exist." shall be raised (p6)
