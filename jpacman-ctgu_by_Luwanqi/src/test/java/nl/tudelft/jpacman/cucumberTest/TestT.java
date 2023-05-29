package nl.tudelft.jpacman.cucumberTest;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import org.junit.Test;

public class TestT {
    @Test
    public void test() {
        Launcher launcher = new Launcher();
        launcher.withMapFile("/cucumberTest/strings/ghost_player.txt");
        launcher.launch();
        while (true) {

        }
    }
}
