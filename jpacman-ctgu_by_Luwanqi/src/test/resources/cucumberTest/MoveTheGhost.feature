@S3
Feature: Move The Ghost
  As a ghost
  I get automatically moved around
  So that I can try to kill the player

  @S3.1
  Scenario: A ghost moves
    Given the game has started with "/cucumberTest/strings/ghost_ground.txt".
    And   a ghost is next to an empty cell
    When  a tick event occurs
    Then  the ghost can move to that cell

  @S3.2
  Scenario: The ghost moves over a square with a pellet
    Given the game has started with "/cucumberTest/strings/ghost_pellet.txt".
    And   a ghost is next to a cell containing a pellet
    When  a tick event occurs
    Then  the ghost can move to the cell with the pellet
    And   the pellet on that cell is not visible anymore

  @S3.3
  Scenario: The ghost leaves a cell with a pellet
    Given a ghost is on a cell with a pellet
    When  a tick event occurs
    Then  the ghost can move away from the cell with the pellet
    And   the pellet on that cell is visible again

  @S3.4
  Scenario: The player dies
    Given the game has started with "/cucumberTest/strings/ghost_player.txt".
    And   a ghost is next to a cell containing the player
    When  a tick event occurs
    Then  the ghost can move to the player
    And   the game is over.