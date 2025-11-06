Feature: Age cheese
As the facility manager, I want to age cheese in a purchase to obtain a high-quality product
  # These scenarios test the RobotController (i.e., the facility manager does something in the UI, 
  # which results in a call to the RobotController).
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
  # ---------- Activate robot ----------

  Scenario: Successfully activate the robot
    When the facility manager attempts to activate the robot
    Then the robot shall be marked as "Idle"
    Then the current shelf of the robot shall be not specified
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be empty
    When the facility manager attempts to view the action log of the robot
    Then the presented action log of the robot shall be empty

  Scenario Outline: Unsuccessfully activate the robot due to wrong state
    Given the robot is marked as "<state>"
    When the facility manager attempts to activate the robot
    Then the robot shall be marked as "<state>"
    Then the error "The robot has already been activated." shall be raised

    Examples:
      | state                    |
      | Idle                     |
      | AtEntranceNotFacingAisle |
      | AtEntranceFacingAisle    |
      | AtCheeseWheel            |
  # ---------- Initialize robot ----------

  Scenario Outline: Successfully initialize the robot
    Given the robot is marked as "Idle"
    When the facility manager attempts to initialize the robot with shelf "<shelfID>"
    Then the robot shall be marked as "AtEntranceNotFacingAisle"
    Then the current shelf of the robot shall be "<shelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"
    When the facility manager attempts to view the action log of the robot
    Then the presented action log of the robot shall be "<log>"

    Examples:
      | shelfID | log            |
      | A12     | At shelf #A12; |
      | B21     | At shelf #B21; |

  Scenario Outline: Unsuccessfully initialize the robot due to wrong input
    Given the robot is marked as "Idle"
    When the facility manager attempts to initialize the robot with shelf "<shelfID>"
    Then the robot shall be marked as "Idle"
    Then the current shelf of the robot shall be not specified
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be empty
    Then the error "<error>" shall be raised

    Examples:
      | shelfID | error                         |
      |         | A shelf must be specified.    |
      | B22     | The shelf B22 does not exist. |

  Scenario Outline: Unsuccessfully initialize the robot due to wrong state
    Given the robot is marked as "<state>"
    When the facility manager attempts to initialize the robot with shelf "A12"
    Then the robot shall be marked as "<state>"
    Then the error "The robot has already been initialized." shall be raised

    Examples:
      | state                    |
      | AtEntranceNotFacingAisle |
      | AtEntranceFacingAisle    |
      | AtCheeseWheel            |

  Scenario: Unsuccessfully initialize the robot when in storage
    When the facility manager attempts to initialize the robot with shelf "A12"
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
  # ---------- Trigger robot to perform treatment ----------

  Scenario Outline: Successfully trigger the robot to perform treatment
    Given the robot is marked as "AtEntranceNotFacingAisle" and at shelf "<shelfID>" with action log "<initialLog>"
    When the facility manager attempts to trigger the robot to perform treatment on "<monthsAged>" old cheese wheels of purchase <purchaseID>
    Then the robot shall be marked as "AtEntranceNotFacingAisle"
    Then the current shelf of the robot shall be "<lastShelfID>"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "<log>"
    When the facility manager attempts to view the action log of the robot
    Then the presented action log of the robot shall be "<log>"

    Examples:
      | shelfID | initialLog     | monthsAged | purchaseID | lastShelfID | log                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
      | A12     | At shelf #A12; | Twelve     |          1 | B21         | At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1; Treat cheese wheel #1; Straight +1 meters; Adjust height +40 centimeters; At cheese wheel #2; Treat cheese wheel #2; Straight -3 meters; Adjust height -40 centimeters; Turn right; Straight +2 meters; At shelf #B21; Turn left; Straight +2 meters; Adjust height +40 centimeters; At cheese wheel #3; Treat cheese wheel #3; Straight -2 meters; Adjust height -40 centimeters; Turn right;                                    |
      | B21     | At shelf #B21; | Twelve     |          1 | B21         | At shelf #B21; Straight -2 meters; At shelf #A12; Turn left; Straight +2 meters; At cheese wheel #1; Treat cheese wheel #1; Straight +1 meters; Adjust height +40 centimeters; At cheese wheel #2; Treat cheese wheel #2; Straight -3 meters; Adjust height -40 centimeters; Turn right; Straight +2 meters; At shelf #B21; Turn left; Straight +2 meters; Adjust height +40 centimeters; At cheese wheel #3; Treat cheese wheel #3; Straight -2 meters; Adjust height -40 centimeters; Turn right; |
      | A12     | At shelf #A12; | Six        |          1 | A12         | At shelf #A12;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | B21     | At shelf #B21; | Six        |          1 | B21         | At shelf #B21;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | A12     | At shelf #A12; | Twelve     |          2 | A12         | At shelf #A12;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | B21     | At shelf #B21; | Twelve     |          2 | B21         | At shelf #B21;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | A12     | At shelf #A12; | Six        |          2 | A12         | At shelf #A12; Turn left; Straight +5 meters; Adjust height +40 centimeters; At cheese wheel #4; Treat cheese wheel #4; Adjust height -40 centimeters; At cheese wheel #5; Treat cheese wheel #5; Straight -5 meters; Turn right;                                                                                                                                                                                                                                                                   |
      | B21     | At shelf #B21; | Six        |          2 | A12         | At shelf #B21; Straight -2 meters; At shelf #A12; Turn left; Straight +5 meters; Adjust height +40 centimeters; At cheese wheel #4; Treat cheese wheel #4; Adjust height -40 centimeters; At cheese wheel #5; Treat cheese wheel #5; Straight -5 meters; Turn right;                                                                                                                                                                                                                                |
      | A12     | At shelf #A12; | Twelve     |          3 | A12         | At shelf #A12;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | B21     | At shelf #B21; | Twelve     |          3 | B21         | At shelf #B21;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | A12     | At shelf #A12; | Six        |          3 | B21         | At shelf #A12; Straight +2 meters; At shelf #B21; Turn left; Straight +1 meters; Adjust height +80 centimeters; At cheese wheel #6; Treat cheese wheel #6; Straight -1 meters; Adjust height -80 centimeters; Turn right;                                                                                                                                                                                                                                                                           |
      | B21     | At shelf #B21; | Six        |          3 | B21         | At shelf #B21; Turn left; Straight +1 meters; Adjust height +80 centimeters; At cheese wheel #6; Treat cheese wheel #6; Straight -1 meters; Adjust height -80 centimeters; Turn right;                                                                                                                                                                                                                                                                                                              |
      | A12     | At shelf #A12; | ThirtySix  |          4 | A12         | At shelf #A12; Turn left; Straight +4 meters; Adjust height +80 centimeters; At cheese wheel #8; Treat cheese wheel #8; Straight -4 meters; Adjust height -80 centimeters; Turn right;                                                                                                                                                                                                                                                                                                              |
      | B21     | At shelf #B21; | ThirtySix  |          4 | A12         | At shelf #B21; Straight -2 meters; At shelf #A12; Turn left; Straight +4 meters; Adjust height +80 centimeters; At cheese wheel #8; Treat cheese wheel #8; Straight -4 meters; Adjust height -80 centimeters; Turn right;                                                                                                                                                                                                                                                                           |
      | A12     | At shelf #A12; | Six        |          4 | A12         | At shelf #A12; Turn left; Straight +1 meters; At cheese wheel #9; Treat cheese wheel #9; Straight -1 meters; Turn right;                                                                                                                                                                                                                                                                                                                                                                            |
      | B21     | At shelf #B21; | Six        |          4 | A12         | At shelf #B21; Straight -2 meters; At shelf #A12; Turn left; Straight +1 meters; At cheese wheel #9; Treat cheese wheel #9; Straight -1 meters; Turn right;                                                                                                                                                                                                                                                                                                                                         |
      | A12     | At shelf #A12; | Twelve     |          5 | A12         | At shelf #A12;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | B21     | At shelf #B21; | Twelve     |          5 | B21         | At shelf #B21;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | A12     | At shelf #A12; | TwentyFour |          5 | A12         | At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; Treat cheese wheel #10; Straight -3 meters; At cheese wheel #11; Treat cheese wheel #11; Straight -1 meters; Adjust height -40 centimeters; Turn right;                                                                                                                                                                                                                                           |
      | B21     | At shelf #B21; | TwentyFour |          5 | A12         | At shelf #B21; Straight -2 meters; At shelf #A12; Turn left; Straight +4 meters; Adjust height +40 centimeters; At cheese wheel #10; Treat cheese wheel #10; Straight -3 meters; At cheese wheel #11; Treat cheese wheel #11; Straight -1 meters; Adjust height -40 centimeters; Turn right;                                                                                                                                                                                                        |

  Scenario Outline: Unsuccessfully trigger the robot to perform treatment due to wrong input
    Given the robot is marked as "AtEntranceNotFacingAisle" and at shelf "A12" with action log "At shelf #A12;"
    When the facility manager attempts to trigger the robot to perform treatment on "<monthsAged>" old cheese wheels of purchase <purchaseID>
    Then the robot shall be marked as "AtEntranceNotFacingAisle"
    Then the current shelf of the robot shall be "A12"
    Then the current cheese wheel of the robot shall be not specified
    Then the action log of the robot shall be "At shelf #A12;"
    Then the error "<error>" shall be raised

    Examples:
      | monthsAged | purchaseID | error                                                         |
      | Twelve     |          0 | The purchase 0 does not exist.                                |
      | Four       |          1 | The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix. |

  Scenario Outline: Unsuccessfully trigger the robot to perform treatment due to wrong state
    Given the robot is marked as "<state>"
    When the facility manager attempts to trigger the robot to perform treatment on "Twelve" old cheese wheels of purchase 1
    Then the robot shall be marked as "<state>"
    Then the error "<error>" shall be raised

    Examples:
      | state                 | error                                                                             |
      | Idle                  | The robot must be initialized first.                                              |
      | AtEntranceFacingAisle | The robot cannot be triggered to perform treatment again during active treatment. |
      | AtCheeseWheel         | The robot cannot be triggered to perform treatment again during active treatment. |

  Scenario: Unsuccessfully trigger the robot to perform treatment when in storage
    When the facility manager attempts to trigger the robot to perform treatment on "Twelve" old cheese wheels of purchase 1
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
  # ---------- Deactivate robot ----------

  Scenario Outline: Successfully deactivate the robot
    Given the robot is marked as "<state>"
    When the facility manager attempts to deactivate the robot
    Then the number of robots in the system shall be 0
    When the facility manager attempts to view the action log of the robot
    Then the presented action log of the robot shall be empty

    Examples:
      | state                    |
      | Idle                     |
      | AtEntranceNotFacingAisle |

  Scenario Outline: Unsuccessfully deactivate the robot due to wrong state
    Given the robot is marked as "<state>"
    When the facility manager attempts to deactivate the robot
    Then the robot shall be marked as "<state>"
    Then the error "The robot cannot be deactivated during active treatment." shall be raised

    Examples:
      | state                 |
      | AtEntranceFacingAisle |
      | AtCheeseWheel         |

  Scenario: Unsuccessfully deactivate the robot when in storage
    When the facility manager attempts to deactivate the robot
    Then the number of robots in the system shall be 0
    Then the error "The robot must be activated first." shall be raised
