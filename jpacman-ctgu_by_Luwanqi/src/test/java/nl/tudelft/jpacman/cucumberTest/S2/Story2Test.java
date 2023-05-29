package nl.tudelft.jpacman.cucumberTest.S2;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;

import java.util.List;


public class Story2Test {
    private Launcher launcher;
    private Game game;
    private Player player;
    private Square playerSquare;
    private Square afterMovingSquare;
    private int originScore;
    private Pellet pellet;
    private Ghost ghost;


    @Given("the game has started with {string}")
    public void startUpTheGame(String string) {
        launcher = new Launcher();
        launcher.withMapFile(string);
        launcher.launch();

        game = launcher.getGame();
        game.start();

        Assertions.assertTrue(game.isInProgress());
    }

    @And("my Pacman is next to a square containing a pellet")
    public void pacmanNextPellet() {
        player = game.getPlayers().get(0);
        //check player exists
        assert player != null;
        //save the origin score of the player
        originScore = player.getScore();

        //find the square next to player(east direction)
        playerSquare = player.getSquare();
        Square nextSquare = playerSquare.getSquareAt(Direction.EAST);

        //get units occupied the square ,assume that the first and the only unit is a pellet
        Assertions.assertEquals(nextSquare.getOccupants().size(), 1);
        pellet = (Pellet) nextSquare.getOccupants().get(0);

        MatcherAssert.assertThat(pellet, CoreMatchers.instanceOf(Pellet.class));
    }

    @When("I press an arrow key towards that square")
    public void movePacmanTowardsSquare() {
        //move the player towards east square
        game.move(player, Direction.EAST);
    }

    @Then("my Pacman can move to that square")
    public void movePacmanTowardsSquareFinished() {
        //get the square after moving, it should be a different square to the origin player square
        afterMovingSquare = player.getSquare();
        //assert with assertNotSame, check two object is not the same
        Assertions.assertNotSame(playerSquare, afterMovingSquare);
    }

    @And("I earn the points for the pellet")
    public void pacmanEarnedPoint() {
        //get the score after pacman consumed the pellet
        int afterMovingScore = player.getScore();
        //assert that the score is added by pellet's value
        Assertions.assertEquals(afterMovingScore, originScore + pellet.getValue());
    }

    @And("the pellet disappears from that square")
    public void pelletDisappears() {
        //get units in the square
        List<Unit> unitsInSquare = afterMovingSquare.getOccupants();
        //assert that player is in the square while pellet is not
        Assertions.assertTrue(unitsInSquare.contains(player));
        Assertions.assertFalse(unitsInSquare.contains(pellet));
    }

    @And("my Pacman is next to an empty square")
    public void pacmanNextSquare() {
        player = game.getPlayers().get(0);
        assert player != null;

        Square playerSquare = player.getSquare();
        Square nextSquare = playerSquare.getSquareAt(Direction.EAST);
        assert nextSquare != null;

        Assertions.assertTrue(nextSquare.getOccupants().isEmpty());
    }

    @And("my points remain the same")
    public void pointsRemainTheSame() {
        int afterMovingScore = player.getScore();
        //assert the points remain the same as before moving
        Assertions.assertEquals(originScore, afterMovingScore);
    }

    @And("my Pacman is next to a cell containing a wall")
    public void pacmanNextToAWall() throws ClassNotFoundException {
        player = game.getPlayers().get(0);
        //check player exists
        assert player != null;
        //save the origin score of the player
        originScore = player.getScore();

        //find the square next to player(east direction)
        playerSquare = player.getSquare();
        Square nextSquare = playerSquare.getSquareAt(Direction.EAST);

        MatcherAssert.assertThat(nextSquare, CoreMatchers.instanceOf(Class.forName("nl.tudelft.jpacman.board.BoardFactory$Wall")));
    }

    @When("I press an arrow key towards that cell")
    public void moveTowardsCell() {
        //move the player towards east square
        game.move(player, Direction.EAST);
    }

    @Then("the move is not conducted")
    public void moveNotConducted() {
        //get the square after moving, it should be a same square to the origin player square
        afterMovingSquare = player.getSquare();
        //assert with assertSame, check two object is the same
        Assertions.assertSame(playerSquare, afterMovingSquare);
    }

    @And("my Pacman is next to a cell containing a ghost")
    public void nextToAGhost() {
        player = game.getPlayers().get(0);
        //check player exists
        assert player != null;
        //save the origin score of the player
        originScore = player.getScore();

        //find the square next to player(east direction)
        playerSquare = player.getSquare();
        Square nextSquare = playerSquare.getSquareAt(Direction.EAST);

        //get units occupied the square ,assume that the first and the only unit is a ghost
        Assertions.assertEquals(nextSquare.getOccupants().size(), 1);
        ghost = (Ghost) nextSquare.getOccupants().get(0);

        MatcherAssert.assertThat(ghost, CoreMatchers.instanceOf(Ghost.class));
    }

    @Then("my Pacman dies")
    public void pacmanDies() {
        Assertions.assertFalse(player.isAlive());
    }

    @And("the game is over")
    public void gameOver() {
        Assertions.assertFalse(game.isInProgress());
    }

    @Given("S2.1 has been done")
    public void s2_1Done() {
        startUpTheGame("/cucumberTest/strings/player_pellet.txt");
        pacmanNextPellet();
        movePacmanTowardsSquare();
        movePacmanTowardsSquareFinished();
        pacmanEarnedPoint();
        pelletDisappears();
    }

    @When("I have eaten the last pellet")
    public void eatenLastPellet() {
        Assertions.assertNull(Navigation.findUnitInBoard(Pellet.class, game.getLevel().getBoard()));
    }

    @Then("I win the game")
    public void winGame() {
        Assertions.assertFalse(game.isInProgress());
    }
}
