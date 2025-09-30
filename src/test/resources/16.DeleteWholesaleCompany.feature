Feature: Delete wholesale company (p16)
As the facility manager, I want to delete a wholesale company from the system.

  Background:
    Given the following wholesale company exists in the system (p16)
      | name         | address |
      | Cheesy Bites |  112 Av |
      | Dairy World  |  55 Mtl |

  Scenario: Successfully delete a wholesale company from the system
    When the facility manager attempts to delete the wholesale company with name "Cheesy Bites" from the system (p16)
    Then the number of wholesale companies in the system shall be 1 (p16)
    Then the following wholesale companies shall exist in the system (p16)
      | name        | address |
      | Dairy World |  55 Mtl |

  Scenario: Unsuccessfully delete a wholesale company that does not exist in the system
    When the facility manager attempts to delete the wholesale company with name "Unknown Company" from the system (p16)
    Then the number of wholesale companies in the system shall be 2 (p16)
    Then the following wholesale companies shall exist in the system (p16)
      | name         | address |
      | Cheesy Bites |  112 Av |
      | Dairy World  |  55 Mtl |
    Then the error "The wholesale company Unknown Company does not exist." shall be raised (p16)

  Scenario: Unsuccessfully delete a wholesale company that has ordered cheese
    Given the following orders in the system (p16)
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      |
      |      2025-04-04 |              5 | Six        |   2025-12-01 | Cheesy Bites |
    When the facility manager attempts to delete the wholesale company with name "Cheesy Bites" from the system (p16)
    Then the number of wholesale companies in the system shall be 2 (p16)
    Then the following wholesale companies shall exist in the system (p16)
      | name         | address |
      | Cheesy Bites |  112 Av |
      | Dairy World  |  55 Mtl |
    Then the error "Cannot delete a wholesale company that has ordered cheese." shall be raised (p16)
