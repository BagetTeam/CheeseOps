Feature: Add Shelf (p17)
As the facility manager, I want to add a shelf to the system.

  Background:
    Given the following shelf exists in the system (p17)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      1 |
    Given all locations are created for shelf "A12" (p17)

  Scenario Outline: Successfully add a shelf to the system
    When the facility manager attempts to add a shelf in the system with id "<id>", <nrColumns> columns, and <nrRows> rows (p17)
    Then the number of shelves in the system shall be 2 (p17)
    Then the shelf "<id>" with <nrColumns> columns and <nrRows> rows and a total of <locationsSize> locations shall exist in the system (p17)
    Then for each row and column of the shelf "<id>", there is exactly one location in the system associated with that row and column (p17)

    Examples:
      | id  | nrColumns | nrRows | locationsSize |
      | B23 |         6 |      2 |            12 |
      | C45 |         3 |      5 |            15 |
      | Z99 |         1 |     10 |            10 |

  Scenario Outline: Unsuccessfully add a shelf to the system with invalid values
    When the facility manager attempts to add a shelf in the system with id "<id>", <nrColumns> columns, and <nrRows> rows (p17)
    Then the number of shelves in the system shall be 1 (p17)
    Then the following shelves shall exist in the system (p17)
      | id  | nrColumns | nrRows |
      | A12 |         5 |      1 |
    Then the error "<error>" shall be raised (p17)

    Examples:
      | id   | nrColumns | nrRows | error                                           |
      | A12  |         6 |      2 | The shelf A12 already exists.                   |
      | B23  |         0 |      2 | Number of columns must be greater than zero.    |
      | B23  |         6 |      0 | Number of rows must be greater than zero.       |
      | B23  |         6 |     11 | Number of rows must be at the most ten.         |
      | A    |         6 |      2 | The id must be three characters long.           |
      | A123 |         6 |      2 | The id must be three characters long.           |
      |  123 |         6 |      2 | The first character must be a letter.           |
      | A1X  |         6 |      2 | The second and third characters must be digits. |
      | AXY  |         6 |      2 | The second and third characters must be digits. |
      | AX1  |         6 |      2 | The second and third characters must be digits. |
