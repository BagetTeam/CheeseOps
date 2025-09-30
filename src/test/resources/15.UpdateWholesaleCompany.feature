Feature: Update WholesaleCompany (p14)
As the facility manager, I want to update a wholesale company in the system.

  Background:
    Given the following wholesale company exists in the system (p14)
      | name         | address |
      | Cheesy Bites |  112 Av |

  Scenario Outline: Successfully update a wholesale company in the system
    When the facility manager attempts to update wholesale company "Cheesy Bites" in the system with name "<updatedName>" and address "<updatedAddress>" (p14)
    Then the number of wholesale companies in the system shall be 1 (p14)
    Then the wholesale company with name "<updatedName>" and address "<updatedAddress>" shall exist in the system (p14)

    Examples:
      | updatedName     | updatedAddress |
      | Dairy Something |         55 Mtl |
      | Milk Depot      |     123 Quebec |
      | Cheesy Bites    |    456 Ontario |

  Scenario Outline: Unsuccessfully update a wholesale company that does not exist in the system
    When the facility manager attempts to update wholesale company "Dairy Something" in the system with name "<updatedName>" and address "<updatedAddress>" (p14)
    Then the number of wholesale companies in the system shall be 1 (p14)
    Then the wholesale company with name "<updatedName>" and address "<updatedAddress>" shall not exist in the system (p14)
    Then the following wholesale companies shall exist in the system (p14)
      | name         | address |
      | Cheesy Bites |  112 Av |
    Then the error "<error>" shall be raised (p14)

    Examples:
      | updatedName     | updatedAddress | error                                                 |
      | Dairy Something |         55 Mtl | The wholesale company Dairy Something does not exist. |

  Scenario Outline: Unsuccessfully update a wholesale company in the system with an invalid value
    Given the following wholesale company exists in the system (p14)
      | name            | address |
      | Dairy Something |  55 Mtl |
    When the facility manager attempts to update wholesale company "Cheesy Bites" in the system with name "<updatedName>" and address "<updatedAddress>" (p14)
    Then the number of wholesale companies in the system shall be 2 (p14)
    Then the wholesale company with name "<updatedName>" and address "<updatedAddress>" shall not exist in the system (p14)
    Then the following wholesale companies shall exist in the system (p14)
      | name            | address |
      | Cheesy Bites    |  112 Av |
      | Dairy Something |  55 Mtl |
    Then the error "<error>" shall be raised (p14)

    Examples:
      | updatedName     | updatedAddress | error                                                 |
      | Dairy Something |         112 Av | The wholesale company Dairy Something already exists. |
      |                 |         55 Mtl | The name must not be empty.                           |
      | New Cheese Co   |                | The address must not be empty.                        |
