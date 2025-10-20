Feature: Sell cheese wheel to wholesale company (p15)
As the facility manager, I want to sell cheese wheel to wholesale company.

  Background:
    Given the following wholesale company exists in the system (p15)
      | name         | address |
      | Cheesy Bites |  112 Av |
    Given the following farmer exists in the system (p15)
      | email            | password | address | name     |
      | farmer@cheesy.fr | P@ssw0rd |  112 Av | Farmer A |

  Scenario Outline: Successfully add an order to the system
    Given the following purchase exists in the system (p15)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail      |
      |   2025-04-04 |              5 | Twelve     | farmer@cheesy.fr |
      |   2025-04-04 |             10 | Six        | farmer@cheesy.fr |
      |   2025-04-04 |              1 | Six        | farmer@cheesy.fr |
      |   2025-04-04 |             10 | TwentyFour | farmer@cheesy.fr |
    Given all cheese wheels for the following purchases are created (p15)
      | purchaseId |
      |          1 |
      |          2 |
      |          3 |
      |          4 |
    Given the following order exists in the system (p15)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      |
      |      2025-04-04 |             10 | Six        |   2025-12-01 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 2 are added to order 5 (p15)
    When the facility manager attempts to add an order in the system with transaction date "<transactionDate>", <nrCheeseWheels> cheese wheels, months aged "<monthsAged>", delivery date "<deliveryDate>", and company "<company>" (p15)
    Then the number of orders in the system shall be 2 (p15)
    Then the order 6 with transaction date "<transactionDate>", <nrCheeseWheels> cheese wheels, months aged "<monthsAged>", delivery date "<deliveryDate>", and company "<company>" shall exist in the system (p15)
    Then <addedCheeseWheels> cheese wheels from purchase <purchaseId> shall be added to the order 6 (p15)

    Examples:
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      | addedCheeseWheels | purchaseId |
      |      2025-09-01 |             22 | Twelve     |   2026-05-01 | Cheesy Bites |                 5 |          1 |
      |      2025-09-01 |             10 | Six        |   2025-11-01 | Cheesy Bites |                 1 |          3 |
      |      2025-09-01 |              5 | TwentyFour |   2027-05-01 | Cheesy Bites |                 5 |          4 |
      |      2025-09-01 |              5 | ThirtySix  |   2028-05-01 | Cheesy Bites |                 0 |         -1 |

  Scenario Outline: Successfully add an order to the system with spoiled cheese wheels
    Given the following purchase exists in the system (p15)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail      |
      |   2025-04-04 |              5 | Twelve     | farmer@cheesy.fr |
      |   2025-04-04 |              2 | Six        | farmer@cheesy.fr |
      |   2025-04-04 |             10 | TwentyFour | farmer@cheesy.fr |
    Given all cheese wheels for the following purchases are created (p15)
      | purchaseId |
      |          1 |
      |          2 |
      |          3 |
    Given the following cheese wheels are spoiled (p15)
      | id |
      |  1 |
      |  6 |
      | 10 |
    Given the following order exists in the system (p15)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      |
      |      2025-04-04 |             10 | Six        |   2025-12-01 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 2 are added to order 4 (p15)
    When the facility manager attempts to add an order in the system with transaction date "<transactionDate>", <nrCheeseWheels> cheese wheels, months aged "<monthsAged>", delivery date "<deliveryDate>", and company "<company>" (p15)
    Then the number of orders in the system shall be 2 (p15)
    Then the order 5 with transaction date "<transactionDate>", <nrCheeseWheels> cheese wheels, months aged "<monthsAged>", delivery date "<deliveryDate>", and company "<company>" shall exist in the system (p15)
    Then <addedCheeseWheels> cheese wheels from purchase <purchaseId> shall be added to the order 5 (p15)

    Examples:
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      | addedCheeseWheels | purchaseId |
      |      2025-11-01 |             22 | Twelve     |   2026-05-01 | Cheesy Bites |                 4 |          1 |
      |      2025-11-01 |             10 | Six        |   2025-11-01 | Cheesy Bites |                 0 |         -1 |
      |      2025-11-01 |              5 | TwentyFour |   2027-05-01 | Cheesy Bites |                 5 |          3 |

  Scenario Outline: Successfully add an order to the system with various maturation dates
    Given the following purchase exists in the system (p15)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail      |
      |   2025-08-01 |              3 | Six        | farmer@cheesy.fr |
      |   2025-04-04 |             11 | Six        | farmer@cheesy.fr |
      |   2025-08-04 |              3 | ThirtySix  | farmer@cheesy.fr |
      |   2025-04-04 |              1 | ThirtySix  | farmer@cheesy.fr |
    Given all cheese wheels for the following purchases are created (p15)
      | purchaseId |
      |          1 |
      |          2 |
      |          3 |
      |          4 |
    Given the following order exists in the system (p15)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      |
      |      2025-04-04 |             10 | Six        |   2025-12-01 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 2 are added to order 5 (p15)
    When the facility manager attempts to add an order in the system with transaction date "<transactionDate>", <nrCheeseWheels> cheese wheels, months aged "<monthsAged>", delivery date "<deliveryDate>", and company "<company>" (p15)
    Then the number of orders in the system shall be 2 (p15)
    Then the order 6 with transaction date "<transactionDate>", <nrCheeseWheels> cheese wheels, months aged "<monthsAged>", delivery date "<deliveryDate>", and company "<company>" shall exist in the system (p15)
    Then <addedCheeseWheels> cheese wheels from purchase <purchaseId> shall be added to the order 6 (p15)

    Examples:
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      | addedCheeseWheels | purchaseId |
      |      2025-09-01 |             22 | Six        |   2025-11-01 | Cheesy Bites |                 1 |          2 |
      |      2025-09-01 |             10 | ThirtySix  |   2028-05-01 | Cheesy Bites |                 1 |          4 |

  Scenario Outline: Unsuccessfully add an order to the system with invalid values
    When the facility manager attempts to add an order in the system with transaction date "<transactionDate>", <nrCheeseWheels> cheese wheels, months aged "<monthsAged>", delivery date "<deliveryDate>", and company "<company>" (p15)
    Then the number of orders in the system shall be 0 (p15)
    Then the error "<error>" shall be raised (p15)

    Examples:
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company         | error                                                         |
      |      2025-11-01 |              0 | Twelve     |   2026-11-01 | Cheesy Bites    | nrCheeseWheels must be greater than zero.                     |
      |      2025-11-01 |             -5 | Six        |   2026-05-01 | Cheesy Bites    | nrCheeseWheels must be greater than zero.                     |
      |      2025-11-01 |             22 | Invalid    |   2026-11-01 | Cheesy Bites    | The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix. |
      |      2025-11-01 |             22 | Twelve     |   2026-11-01 | Dairy Something | The wholesale company Dairy Something does not exist.         |
      |      2025-11-01 |             22 | Twelve     |   2025-10-01 | Cheesy Bites    | The delivery date must be on or after the transaction date.   |
