package de.mfwk.sudokuservice.test;

import de.mfwk.sudokuservice.client.SudokuClient;
import de.mfwk.sudokuservice.server.SudokuServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by micha on 28.04.2017.
 */
public class IntegrationTest {
    private SudokuServer server;
    private Thread serverthread;

    @BeforeEach
    public void setup() {
        System.out.println("STAGING");
        this.server = new SudokuServer();
        this.serverthread = new Thread(() -> this.server.start(1337));
    }

    @AfterEach
    public void teardown() {
        System.out.println("TEARDOWN");
        this.server.shutdown();
        this.serverthread = null;
    }

    @Test
    public void testPing() throws MalformedURLException {
        assertTrue(new SudokuClient("localhost", 1337).pingrequest());
    }
}
