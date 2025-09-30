Feature: Update Farmer (p3)
As the facility manager, I want to update a farmer in the system.

  Background:
    Given the following farmer exists in the system (p3)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |

  Scenario Outline: Successfully update a farmer in the system
    When the facility manager attempts to update farmer "<email>" in the system with password "<updatedPassword>", address "<updatedAddress>", and name "<updatedName>" (p3)
    Then the number of farmers in the system shall be 1 (p3)
    Then the farmer "<email>" with password "<updatedPassword>", address "<updatedAddress>", and name "<updatedName>" shall exist in the system (p3)

    Examples:
      | email             | updatedPassword | updatedName | updatedAddress |
      | farmer@cheecse.fr | Password$       | Jericho     |         55 Mtl |
      | farmer@cheecse.fr | P4$$w0rd        | John Smith  |    123 Farm St |
      | farmer@cheecse.fr | Ab#1234         | Farmer B    |     42 Main Rd |
      | farmer@cheecse.fr | P@ssw0rd        |             |         112 Av |

  Scenario Outline: Unsuccessfully update a farmer with invalid values
    When the facility manager attempts to update farmer "<email>" in the system with password "<updatedPassword>", address "<updatedAddress>", and name "<updatedName>" (p3)
    Then the number of farmers in the system shall be 1 (p3)
    Then the farmer "<email>" with password "<updatedPassword>", address "<updatedAddress>", and name "<updatedName>" shall not exist in the system (p3)
    Then the following farmers shall exist in the system (p3)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Then the error "<error>" shall be raised (p3)

    Examples:
      | email             | updatedPassword | updatedName | updatedAddress | error                                                   |
      | farmer@cheecse.ca | Password$       | Jericho     |         55 Mtl | The farmer with email farmer@cheecse.ca does not exist. |
      | farmer@cheecse.fr |                 | Jericho     |         55 Mtl | Password must not be empty.                             |
      | farmer@cheecse.fr | Password$       | Jericho     |                | Address must not be empty.                              |
