Feature: Display WholesaleCompany (p4)
As the facility manager, I want to display a wholesale company from the system with all its details.

  Background:
    Given the following wholesale company exists in the system (p4)
      | name         | address |
      | Cheesy Bites |  112 Av |
    Given the following farmer exists in the system (p4)
      | email               | password | address  | name     |
      | farmer1@example.com | pass123  | 123 Farm | Farmer 1 |
    Given the following purchase exists in the system (p4)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail         |
      |   2025-01-01 |              5 | Six        | farmer1@example.com |
    Given all cheese wheels from purchase 1 are created (p4)
    Given the following order exists in the system (p4)
      | orderDate  | nrCheeseWheels | monthsAged | companyName  | deliveryDate |
      | 2025-08-15 |             10 | Six        | Cheesy Bites |   2025-12-15 |
      | 2025-09-01 |              3 | Twelve     | Cheesy Bites |   2026-03-01 |
    Given all non-spoiled cheese wheels from purchase 1 are added to order 2 (p4)

  Scenario: Successfully display all wholesale companies from the system
    Given the following wholesale company exists in the system (p4)
      | name            | address     |
      | Dairy something |      55 Mtl |
      | Cheese Masters  | 456 Ontario |
    Given the following purchase exists in the system (p4)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail         |
      |   2025-02-01 |              3 | TwentyFour | farmer1@example.com |
    Given all cheese wheels from purchase 4 are created (p4)
    Given the following order exists in the system (p4)
      | orderDate  | nrCheeseWheels | monthsAged | companyName    | deliveryDate |
      | 2025-10-01 |             15 | TwentyFour | Cheese Masters |   2027-02-02 |
    Given all non-spoiled cheese wheels from purchase 4 are added to order 5 (p4)
    When the facility manager attempts to display from the system all the wholesale companies (p4)
    Then the number of wholesale companies in the system shall be 3 (p4)
    Then the following wholesale company details shall be presented (p4)
      | name            | address     |
      | Cheesy Bites    |      112 Av |
      | Dairy something |      55 Mtl |
      | Cheese Masters  | 456 Ontario |
    Then the following order details shall be presented for wholesale company "Cheesy Bites" (p4)
      | orderDate  | monthsAged | nrCheeseWheelsOrdered | nrCheeseWheelsMissing | deliveryDate |
      | 2025-08-15 | Six        |                    10 |                     5 |   2025-12-15 |
      | 2025-09-01 | Twelve     |                     3 |                     3 |   2026-03-01 |
    Then no order details shall be presented for wholesale company "Dairy something" (p4)
    Then the following order details shall be presented for wholesale company "Cheese Masters" (p4)
      | orderDate  | monthsAged | nrCheeseWheelsOrdered | nrCheeseWheelsMissing | deliveryDate |
      | 2025-10-01 | TwentyFour |                    15 |                    12 |   2027-02-02 |

  Scenario: Successfully display a wholesale company from the system
    When the facility manager attempts to display from the system the wholesale company with name "Cheesy Bites" (p4)
    Then the number of wholesale companies in the system shall be 1 (p4)
    Then the following wholesale company details shall be presented (p4)
      | name         | address |
      | Cheesy Bites |  112 Av |
    Then the following order details shall be presented for wholesale company "Cheesy Bites" (p4)
      | orderDate  | monthsAged | nrCheeseWheelsOrdered | nrCheeseWheelsMissing | deliveryDate |
      | 2025-08-15 | Six        |                    10 |                     5 |   2025-12-15 |
      | 2025-09-01 | Twelve     |                     3 |                     3 |   2026-03-01 |

  Scenario: Unsuccessfully display a wholesale company that does not exist in the system
    When the facility manager attempts to display from the system the wholesale company with name "Cheese Paradise" (p4)
    Then the number of wholesale companies in the system shall be 1 (p4)
    Then no wholesale companies shall be presented (p4)
