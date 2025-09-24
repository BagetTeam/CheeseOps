Feature: Register Farmer (p9)
As the facility manager, I want to register a farmer in the system.

  Background:
    Given the following farmer exists in the system (p9)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |

  Scenario Outline: Successfully register a farmer in the system
    When the facility manager attempts to register a farmer in the system with email "<email>", password "<password>", address "<address>", and name "<name>" (p9)
    Then the number of farmers in the system shall be 2 (p9)
    Then the farmer "<email>" with password "<password>", address "<address>", and name "<name>" shall exist in the system (p9)

    Examples:
      | email              | password  | address | name     |
      | farmer@cheecse.ca  | Pass$word |  55 Mtl | Farmer B |
      | farmer2@domain.com | P4$$w0rd  |  123 St | Farmer C |
      | john@farming.org   | Ab#1234   | Farm Rd | Farmer D |
      | jane@farming.org   | Ab#1234   | Farm Rd |          |

  Scenario Outline: Unsuccessfully register a farmer in the system with invalid values
    When the facility manager attempts to register a farmer in the system with email "<email>", password "<password>", address "<address>", and name "<name>" (p9)
    Then the number of farmers in the system shall be 1 (p9)
    Then the following farmers shall exist in the system (p9)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Then the error "<error>" shall be raised (p9)

    Examples:
      | email                | password  | address | name     | error                                 |
      | farmer@cheecse.fr    | Pass$word |  55 Mtl | Farmer B | The farmer email already exists.      |
      | farmer without at    | Pass$word |  55 Mtl | Farmer B | Email must contain @ symbol.          |
      | @nodomain.com        | Pass$word |  55 Mtl | Farmer B | Email must have characters before @.  |
      | invalid@             | Pass$word |  55 Mtl | Farmer B | Email must contain a dot after @.     |
      | invalid@domain       | Pass$word |  55 Mtl | Farmer B | Email must contain a dot after @.     |
      | invalid@domain.      | Pass$word |  55 Mtl | Farmer B | Email must have characters after dot. |
      | farmer email@with.sp | Pass$word |  55 Mtl | Farmer B | Email must not contain spaces.        |
      | manager@cheecse.fr   | Pass$word |  55 Mtl | Farmer B | Email cannot be manager@cheecse.fr.   |
      | farmer@cheecse.ca    |           |  55 Mtl | Farmer B | Password must not be empty.           |
      | farmer@cheecse.ca    | Pass$word |         | Farmer B | Address must not be empty.            |
