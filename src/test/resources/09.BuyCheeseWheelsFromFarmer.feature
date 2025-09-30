Feature: Buy Cheese Wheels From Farmer (p8)
As the facility manager, I want to buy cheese from farmers, so that I can age and sell it later.

  Background:
    Given the following farmer exists in the system (p8)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Given the following purchase exists in the system (p8)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-04-04 |              5 | Six        | farmer@cheecse.fr |
    Given all cheese wheels from purchase 1 are created (p8)

  Scenario Outline: Successfully add a purchase to the system
    When the facility manager attempts to add a purchase in the system with purchaseDate "<purchaseDate>", <nrCheeseWheels> cheese wheels, monthsAged "<monthsAged>", and farmerEmail "<farmerEmail>" (p8)
    Then the number of purchases in the system shall be 2 (p8)
    Then the purchase 2 with purchaseDate "<purchaseDate>" and farmerEmail "<farmerEmail>" shall exist in the system (p8)
    Then the purchase 2 shall have <nrCheeseWheels> cheese wheels (p8)
    Then all cheese wheels for purchase 2 shall have monthsAged "<monthsAged>" (p8)
    Then the number of cheese wheels in the system shall be <totalCheeseWheels> (p8)

    Examples:
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       | totalCheeseWheels |
      |   2025-11-01 |              5 | Twelve     | farmer@cheecse.fr |                10 |
      |   2025-11-02 |              3 | Six        | farmer@cheecse.fr |                 8 |
      |   2025-11-03 |             10 | TwentyFour | farmer@cheecse.fr |                15 |
      |   2025-11-04 |              8 | ThirtySix  | farmer@cheecse.fr |                13 |

  Scenario Outline: Unsuccessfully add a purchase to the system with invalid values
    When the facility manager attempts to add a purchase in the system with purchaseDate "<purchaseDate>", <nrCheeseWheels> cheese wheels, monthsAged "<monthsAged>", and farmerEmail "<farmerEmail>" (p8)
    Then the number of purchases in the system shall be 1 (p8)
    Then the following purchases shall exist in the system (p8)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-04-04 |              5 | Six        | farmer@cheecse.fr |
    Then the purchase 2 shall not exist in the system (p8)
    Then the number of cheese wheels in the system shall be 5 (p8)
    Then the error "<error>" shall be raised (p8)

    Examples:
      | purchaseDate | nrCheeseWheels | monthsAged  | farmerEmail        | error                                                         |
      |   2025-11-01 |              0 | Twelve      | farmer@cheecse.fr  | nrCheeseWheels must be greater than zero.                     |
      |   2025-11-01 |             -5 | Twelve      | farmer@cheecse.fr  | nrCheeseWheels must be greater than zero.                     |
      |   2025-11-01 |              5 | notALiteral | farmer@cheecse.fr  | The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix. |
      |   2025-11-01 |              5 | Twelve      | farmer@cheecse.ca  | The farmer with email farmer@cheecse.ca does not exist.       |
      |   2025-11-01 |              5 | Twelve      | manager@cheecse.fr | The farmer with email manager@cheecse.fr does not exist.      |
