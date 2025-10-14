Feature: Display Farmer (p5)
As the facility manager, I want to display a farmer from the system with all their details including supplied cheese wheels.

  Background:
    Given the following farmer exists in the system (p5)
      | email              | password  | address | name     |
      | farmer@cheecse.fr  | P@ssw0rd  |  112 Av | Farmer A |
      | farmer2@cheecse.ca | Pass$word |  55 Mtl | Farmer B |
    Given the following purchase exists in the system (p5)
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail       |
      |   2025-09-01 |              2 | Six        | farmer@cheecse.fr |
      |   2025-08-15 |              3 | Twelve     | farmer@cheecse.fr |
    Given all cheese wheels from purchase 1 are created (p5)
    Given all cheese wheels from purchase 2 are created (p5)
    Given cheese wheel 2 is spoiled (p5)

  Scenario: Successfully display all farmers from the system
    When the facility manager attempts to display from the system all the farmers (p5)
    Then the number of farmers in the system shall be 2 (p5)
    Then the following farmer details shall be presented (p5)
      | email              | password  | address | name     |
      | farmer@cheecse.fr  | P@ssw0rd  |  112 Av | Farmer A |
      | farmer2@cheecse.ca | Pass$word |  55 Mtl | Farmer B |
    Then the following cheese wheels shall be presented for farmer "farmer@cheecse.fr" (p5)
      | id | monthsAged | isSpoiled | purchaseDate |
      |  1 | Six        | false     |   2025-09-01 |
      |  2 | Six        | true      |   2025-09-01 |
      |  3 | Twelve     | false     |   2025-08-15 |
      |  4 | Twelve     | false     |   2025-08-15 |
      |  5 | Twelve     | false     |   2025-08-15 |
    Then no cheese wheels shall be presented for farmer "farmer2@cheecse.ca" (p5)

  Scenario: Successfully display a farmer from the system
    When the facility manager attempts to display from the system the farmer with email "farmer@cheecse.fr" (p5)
    Then the number of farmers in the system shall be 2 (p5)
    Then the following farmer details shall be presented (p5)
      | email             | password | address | name     |
      | farmer@cheecse.fr | P@ssw0rd |  112 Av | Farmer A |
    Then the following cheese wheels shall be presented for farmer "farmer@cheecse.fr" (p5)
      | id | monthsAged | isSpoiled | purchaseDate |
      |  1 | Six        | false     |   2025-09-01 |
      |  2 | Six        | true      |   2025-09-01 |
      |  3 | Twelve     | false     |   2025-08-15 |
      |  4 | Twelve     | false     |   2025-08-15 |
      |  5 | Twelve     | false     |   2025-08-15 |

  Scenario: Unsuccessfully display a farmer that does not exist in the system
    When the facility manager attempts to display from the system the farmer with email "farmer@cheecse.ca" (p5)
    Then the number of farmers in the system shall be 2 (p5)
    Then no farmers shall be presented (p5)
