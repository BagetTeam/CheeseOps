Feature: Move robot
As the robot controller, I want to move the robot to shelfs and cheese wheels so that the desired cheese wheel can be treated
  # These scenarios test the state machine directly (i.e., the RobotController calls the state machine).
  # Most Given statements have already been used for Iteration 2. Their step definitions should be reused here.

  Background:
    Given the following shelf exists in the system
      | id  | nrColumns | nrRows |
      | A12 |         5 |      4 |
      | B21 |         3 |      3 |
    Given all locations are created for shelf "A12"
    Given all locations are created for shelf "B21"
    Given the following farmer exists in the system
      | email            | password | address | name     |
      | farmer@cheesy.fr | P@ssw0rd |  112 Av | Farmer A |
    Given the following purchase exists in the system
      | purchaseDate | nrCheeseWheels | monthsAged | farmerEmail      |
      |   2025-04-04 |              3 | Twelve     | farmer@cheesy.fr |
      |   2025-04-04 |              2 | Six        | farmer@cheesy.fr |
      |   2025-04-04 |              1 | Six        | farmer@cheesy.fr |
      |   2025-04-04 |              3 | Six        | farmer@cheesy.fr |
      |   2025-04-04 |              3 | Twelve     | farmer@cheesy.fr |
    Given all cheese wheels for the following purchases are created
      | purchaseId |
      |          1 |
      |          2 |
      |          3 |
      |          4 |
      |          5 |
    Given cheese wheel 1 is at shelf location with column 2 and row 1 of shelf "A12"
    Given cheese wheel 2 is at shelf location with column 3 and row 2 of shelf "A12"
    Given cheese wheel 3 is at shelf location with column 2 and row 2 of shelf "B21"
    Given cheese wheel 4 is at shelf location with column 5 and row 2 of shelf "A12"
    Given cheese wheel 5 is at shelf location with column 5 and row 1 of shelf "A12"
    Given cheese wheel 6 is at shelf location with column 1 and row 3 of shelf "B21"
    Given cheese wheel 8 is at shelf location with column 4 and row 3 of shelf "A12"
    Given cheese wheel 9 is at shelf location with column 1 and row 1 of shelf "A12"
    Given cheese wheel 10 is at shelf location with column 4 and row 2 of shelf "A12"
    Given cheese wheel 11 is at shelf location with column 1 and row 2 of shelf "A12"
    Given the following cheese wheels are spoiled
      | id |
      |  7 |
      | 12 |
    Given the months aged value of the following cheese wheels is increased
      | id | newMonthsAged |
      |  8 | ThirtySix     |
      | 10 | TwentyFour    |
      | 11 | TwentyFour    |
    Given the following wholesale company exists in the system
      | name         | address |
      | Cheesy Bites |  112 Av |
    Given the following order exists in the system
      | transactionDate | nrCheeseWheels | monthsAged | deliveryDate | company      |
      |      2025-04-04 |              2 | Six        |   2025-12-01 | Cheesy Bites |
      |      2025-04-04 |              3 | Twelve     |   2026-05-01 | Cheesy Bites |
    Given all non-spoiled cheese wheels from purchase 2 are added to order 6
    Given all non-spoiled cheese wheels from purchase 1 are added to order 7
  # ---------- Turn robot left ----------

  Scenario Outline: Successfully turn the robot left
    Given the robot is marked as "AtEntranceNotFacingAisle" and at shelf "<shelfID>" with action log "<initialLog>"
    When the robot controller attempts to turn the robot left
    Then the robot shall be marked as "AtEntranceFacingAisle"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"

    Examples:
      | shelfID | initialLog     | log                       |
      | A12     | At shelf #A12; | At shelf #A12; Turn left; |
      | B21     | At shelf #B21; | At shelf #B21; Turn left; |

  Scenario Outline: Unsuccessfully turn the robot left due to wrong state
    Given the robot is marked as "<state>"
    When the robot controller attempts to turn the robot left
    Then the robot shall be marked as "<state>"
    Then the error "The robot cannot be turned left." shall be raised

    Examples:
      | state                 |
      | Idle                  |
      | AtEntranceFacingAisle |
      | AtCheeseWheel         |

  Scenario: Unsuccessfully turn the robot left when in storage
    When the robot controller attempts to turn the robot left
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
  # ---------- Turn robot right ----------

  Scenario Outline: Successfully turn the robot right
    Given the robot is marked as "AtEntranceFacingAisle" and at shelf "<shelfID>" with action log "<initialLog>"
    When the robot controller attempts to turn the robot right
    Then the robot shall be marked as "AtEntranceNotFacingAisle"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"

    Examples:
      | shelfID | initialLog                | log                                   |
      | A12     | At shelf #A12; Turn left; | At shelf #A12; Turn left; Turn right; |
      | B21     | At shelf #B21; Turn left; | At shelf #B21; Turn left; Turn right; |

  Scenario Outline: Unsuccessfully turn the robot right due to wrong state
    Given the robot is marked as "<state>"
    When the robot controller attempts to turn the robot right
    Then the robot shall be marked as "<state>"
    Then the error "The robot cannot be turned right." shall be raised

    Examples:
      | state                    |
      | Idle                     |
      | AtEntranceNotFacingAisle |
      | AtCheeseWheel            |

  Scenario: Unsuccessfully turn the robot right when in storage
    When the robot controller attempts to turn the robot right
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
  # ---------- Move robot to shelf ----------

  Scenario Outline: Successfully move the robot to shelf
    Given the robot is marked as "AtEntranceNotFacingAisle" and at shelf "<shelfID>" with action log "<initialLog>"
    When the robot controller attempts to move the robot to shelf "<newShelfID>"
    Then the robot shall be marked as "AtEntranceNotFacingAisle"
    Then the current shelf of the robot shall be "<newShelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"

    Examples:
      | shelfID | initialLog     | newShelfID | log                                               |
      | A12     | At shelf #A12; | B21        | At shelf #A12; Straight +2 meters; At shelf #B21; |
      | B21     | At shelf #B21; | A12        | At shelf #B21; Straight -2 meters; At shelf #A12; |

  Scenario Outline: Unsuccessfully move the robot to shelf due to wrong input
    Given the robot is marked as "AtEntranceNotFacingAisle" and at shelf "<shelfID>" with action log "<log>"
    When the robot controller attempts to move the robot to shelf "<newShelfID>"
    Then the robot shall be marked as "AtEntranceNotFacingAisle"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"
    Then the error "<error>" shall be raised

    Examples:
      | shelfID | log            | newShelfID | error                         |
      | A12     | At shelf #A12; |            | A shelf must be specified.    |
      | B21     | At shelf #B21; | B22        | The shelf B22 does not exist. |

  Scenario Outline: Unsuccessfully move the robot to shelf due to wrong state
    Given the robot is marked as "<state>"
    When the robot controller attempts to move the robot to shelf "A12"
    Then the robot shall be marked as "<state>"
    Then the error "The robot cannot be moved to shelf #A12." shall be raised

    Examples:
      | state                 |
      | Idle                  |
      | AtEntranceFacingAisle |
      | AtCheeseWheel         |

  Scenario: Unsuccessfully move the robot to shelf when in storage
    When the robot controller attempts to move the robot to shelf "A12"
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
  # ---------- Move robot to cheese wheel ----------

  Scenario Outline: Successfully move the robot to cheese wheel from entrance
    Given the robot is marked as "AtEntranceFacingAisle" and at shelf "<shelfID>" with action log "<initialLog>"
    When the robot controller attempts to move the robot to cheese wheel <cheeseWheelID>
    Then the robot shall be marked as "AtCheeseWheel"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall <cheeseWheelID>
    Then the action log of the robot shall be "<log>"

    Examples:
      | shelfID | initialLog                | cheeseWheelID | log                                                                                              |
      | A12     | At shelf #A12; Turn left; |             1 | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1;                                |
      | B21     | At shelf #B21; Turn left; |             3 | At shelf #B21; Turn left; Straight +2 meters; Adjust height +40 centimeters; At cheese wheel #3; |

  Scenario Outline: Successfully move the robot to cheese wheel from another cheese wheel
    Given the robot is marked as "AtCheeseWheel" and at cheese wheel <cheeseWheelID> on shelf "<shelfID>" with action log "<initialLog>"
    When the robot controller attempts to move the robot to cheese wheel <newCheeseWheelID>
    Then the robot shall be marked as "AtCheeseWheel"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall <newCheeseWheelID>
    Then the action log of the robot shall be "<log>"

    Examples:
      | cheeseWheelID | shelfID | initialLog                                                                                        | newCheeseWheelID | log                                                                                                                                        |
      |             1 | A12     | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1;                                 |                2 | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1; Straight +1 meters; Adjust height +40 centimeters; At cheese wheel #2;   |
      |            10 | A12     | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; |               11 | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; Straight -3 meters; At cheese wheel #11; |

  Scenario Outline: Unsuccessfully move the robot to cheese wheel from entrance
    Given the robot is marked as "AtEntranceFacingAisle" and at shelf "<shelfID>" with action log "<log>"
    When the robot controller attempts to move the robot to cheese wheel <cheeseWheelID>
    Then the robot shall be marked as "AtEntranceFacingAisle"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"
    Then the error "<error>" shall be raised

    Examples:
      | shelfID | log                       | cheeseWheelID | error                                  |
      | A12     | At shelf #A12; Turn left; |            6  | Cheese wheel #6 is not on shelf #A12. |
      | B21     | At shelf #B21; Turn left; |             1 | Cheese wheel #1 is not on shelf #B21.  |

  Scenario Outline: Unsuccessfully move the robot to cheese wheel from another cheese wheel
    Given the robot is marked as "AtCheeseWheel" and at cheese wheel <cheeseWheelID> on shelf "<shelfID>" with action log "<log>"
    When the robot controller attempts to move the robot to cheese wheel <newCheeseWheelID>
    Then the robot shall be marked as "AtCheeseWheel"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall <cheeseWheelID>
    Then the action log of the robot shall be "<log>"
    Then the error "<error>" shall be raised

    Examples:
      | cheeseWheelID | shelfID | log                                                                                               | newCheeseWheelID | error                                  |
      |             1 | A12     | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1;                                 |               6 | Cheese wheel #6 is not on shelf #A12. |
      |            10 | A12     | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; |                3 | Cheese wheel #3 is not on shelf #A12.  |

  Scenario Outline: Unsuccessfully move the robot to cheese wheel due to wrong state
    Given the robot is marked as "<state>"
    When the robot controller attempts to move the robot to cheese wheel 1
    Then the robot shall be marked as "<state>"
    Then the error "The robot cannot be moved to cheese wheel #1." shall be raised

    Examples:
      | state                    |
      | Idle                     |
      | AtEntranceNotFacingAisle |

  Scenario: Unsuccessfully move the robot to cheese wheel when in storage
    When the robot controller attempts to move the robot to cheese wheel 1
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
  # ---------- Move robot to entrance ----------

  Scenario Outline: Successfully move the robot to entrance
    Given the robot is marked as "AtCheeseWheel" and at cheese wheel <cheeseWheelID> on shelf "<shelfID>" with action log "<initialLog>"
    When the robot controller attempts to move the robot to the entrance
    Then the robot shall be marked as "AtEntranceFacingAisle"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"

    Examples:
      | cheeseWheelID | shelfID | initialLog                                                                                        | log                                                                                                                                                  |
      |             1 | A12     | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1;                                 | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1; Straight -2 meters;                                                                |
      |            10 | A12     | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; Straight -4 meters; Adjust height -40 centimeters; |

  Scenario Outline: Unsuccessfully move the robot to entrance due to wrong state
    Given the robot is marked as "<state>"
    When the robot controller attempts to move the robot to the entrance
    Then the robot shall be marked as "<state>"
    Then the error "The robot cannot be moved to the entrance of the aisle." shall be raised

    Examples:
      | state                    |
      | Idle                     |
      | AtEntranceNotFacingAisle |
      | AtEntranceFacingAisle    |

  Scenario: Unsuccessfully move the robot to entrance when in storage
    When the robot controller attempts to move the robot to the entrance
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
  # ---------- Trigger robot to perform treatment ----------

  Scenario Outline: Successfully trigger the robot to perform treatment
    Given the robot is marked as "AtCheeseWheel" and at cheese wheel <cheeseWheelID> on shelf "<shelfID>" with action log "<initialLog>"
    When the robot controller attempts to trigger the robot to perform treatment
    Then the robot shall be marked as "AtCheeseWheel"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall be <cheeseWheelID>
    Then the action log of the robot shall be "<log>"

    Examples:
      | cheeseWheelID | shelfID | initialLog                                                                                        | log                                                                                                                       |
      |             1 | A12     | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1;                                 | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1; Treat cheese wheel #1;                                  |
      |            10 | A12     | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; Treat cheese wheel #10; |

  Scenario Outline: Unsuccessfully trigger the robot to perform treatment due to wrong state
    Given the robot is marked as "<state>"
    When the robot controller attempts to trigger the robot to perform treatment
    Then the robot shall be marked as "<state>"
    Then the error "The robot cannot be perform treatment." shall be raised

    Examples:
      | state                    |
      | Idle                     |
      | AtEntranceNotFacingAisle |
      | AtEntranceFacingAisle    |

  Scenario: Unsuccessfully trigger the robot to perform treatment when in storage
    When the robot controller attempts to trigger the robot to perform treatment
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
