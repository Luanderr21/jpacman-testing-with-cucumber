package nl.tudelft.jpacman.cucumberTest.S3;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import static java.lang.Thread.sleep;

public class    Story3Test {
    private Launcher launcher;
    private Game game;
    private Ghost ghost;
    private Square ghostSquare;
    private Square nextSquare;

    @Given("the game has started with {string}.")
    public void startUpTheGame(String string) {
        launcher = new Launcher();
        launcher.withMapFile(string);
        launcher.launch();

        game = launcher.getGame();
        game.start();

        Assertions.assertTrue(game.isInProgress());
    }

    @And("a ghost is next to an empty cell")
    public void nextToEmptyCell() {
        ghost = Navigation.findUnitInBoard(Ghost.class, game.getLevel().getBoard());
        assert ghost != null;

        ghost.setDirection(Direction.EAST);

        ghostSquare = ghost.getSquare();
        Square nextSquare = ghostSquare.getSquareAt(Direction.EAST);

        Assertions.assertTrue(nextSquare.getOccupants().isEmpty());
    }

    @When("a tick event occurs")
    public void tickEventOccurs() throws InterruptedException {
        game.start();
        game.getLevel().move(ghost, ghost.nextAiMove().get());
        game.stop();
    }

    @When("the ghost can move to that cell")
    public void ghostMoveToThatCell() {
        nextSquare = ghost.getSquare();

        Assertions.assertNotSame(ghostSquare, nextSquare);
    }

    @And("a ghost is next to a cell containing a pellet")
    public void ghostNextToPellet() {
        ghost = Navigation.findUnitInBoard(Ghost.class, game.getLevel().getBoard());
        assert ghost != null;

        ghost.setDirection(Direction.EAST);

        ghostSquare = ghost.getSquare();
        Square nextSquare = ghostSquare.getSquareAt(Direction.EAST);

        Assertions.assertEquals(nextSquare.getOccupants().size(), 1);
        Pellet pellet = (Pellet) nextSquare.getOccupants().get(0);

        MatcherAssert.assertThat(pellet, CoreMatchers.instanceOf(Pellet.class));
    }

    @Then("the ghost can move to the cell with the pellet")
    public void moveToPellet() {
        Square nextSquare = ghost.getSquare();
        Assertions.assertEquals(nextSquare.getOccupants().size(), 2);
    }

    @And("the pellet on that cell is not visible anymore")
    public void pelletNotVisible() {
    /*  game.start();
        game.getLevel().move(Navigation.findUnitInBoard(Pellet.class, game.getLevel().getBoard()), Direction.EAST);
        game.getLevel().move(Navigation.findUnitInBoard(Pellet.class, game.getLevel().getBoard()), Direction.WEST);
        game.stop();
        while (true) {
        }*/
    }

    @Given("a ghost is on a cell with a pellet")
    public void ghostOnACellWithPellet() throws InterruptedException {
        startUpTheGame("/cucumberTest/strings/ghost_pellet.txt");
        ghostNextToPellet();
        tickEventOccurs();
        ghostMoveToThatCell();
    }

    @Then("the ghost can move away from the cell with the pellet")
    public void ghostMoveAwayFromTheCell() {
        Square finalSquare = ghost.getSquare();
        Assertions.assertNotSame(finalSquare, nextSquare);
    }

    @And("the pellet on that cell is visible again")
    public void pelletVisibleAgain() {
        Assertions.assertFalse(nextSquare.getOccupants().isEmpty());

        Pellet pellet = (Pellet) nextSquare.getOccupants().get(0);
        MatcherAssert.assertThat(pellet, CoreMatchers.instanceOf(Pellet.class));
    }

    @And("a ghost is next to a cell containing the player")
    public void ghostNextToPlayer() {
        ghost = Navigation.findUnitInBoard(Ghost.class, game.getLevel().getBoard());
        assert ghost != null;

        ghost.setDirection(Direction.EAST);

        ghostSquare = ghost.getSquare();
        Square nextSquare = ghostSquare.getSquareAt(Direction.EAST);

        Assertions.assertEquals(nextSquare.getOccupants().size(), 1);
        Player player = (Player) nextSquare.getOccupants().get(0);

        MatcherAssert.assertThat(player, CoreMatchers.instanceOf(Player.class));
    }

    @Then("the ghost can move to the player")
    public void ghostMoveToPlayer() {
        game.start();
        game.getLevel().move(ghost, Direction.EAST);
        game.stop();
    }

    @And("the game is over.")
    public void gameOver() {
        Assertions.assertFalse(game.isInProgress());
    }
}
