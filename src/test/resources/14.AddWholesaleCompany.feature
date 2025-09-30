Feature: Add WholesaleCompany (p13)
As the facility manager, I want to add a wholesale company to the system.

  Background:
    Given the following wholesale company exists in the system (p13)
      | name         | address |
      | Cheesy Bites |  112 Av |

  Scenario Outline: Successfully add a wholesale company to the system
    When the facility manager attempts to add a wholesale company in the system with name "<name>" and address "<address>" (p13)
    Then the number of wholesale companies in the system shall be 2 (p13)
    Then the wholesale company "<name>" with address "<address>" shall exist in the system (p13)

    Examples:
      | name            | address |
      | Dairy Something |  55 Mtl |
      | Cheese Masters  | 123 Rue |

  Scenario Outline: Unsuccessfully add a wholesale company to the system with invalid values
    When the facility manager attempts to add a wholesale company in the system with name "<name>" and address "<address>" (p13)
    Then the number of wholesale companies in the system shall be 1 (p13)
    Then the following wholesale companies shall exist in the system (p13)
      | name         | address |
      | Cheesy Bites |  112 Av |
    Then the error "<error>" shall be raised (p13)

    Examples:
      | name         | address | error                                 |
      | Cheesy Bites |  55 Mtl | The wholesale company already exists. |
      |              |  55 Mtl | Name must not be empty.               |
      | New Company  |         | Address must not be empty.            |
