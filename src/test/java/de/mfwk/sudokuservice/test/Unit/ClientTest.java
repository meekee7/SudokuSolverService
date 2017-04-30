package de.mfwk.sudokuservice.test.Unit;

import de.mfwk.sudokuservice.client.SudokuClient;

import java.net.MalformedURLException;

/**
 * Created by micha on 28.04.2017.
 */
public class ClientTest {
    private static SudokuClient createClient() throws MalformedURLException {
        return new SudokuClient("selfhosted", 0);
    }
}
