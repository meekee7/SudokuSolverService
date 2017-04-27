package de.mfwk.sudokuservice.client;

import de.mfwk.sudokuservice.core.Sudoku;
import de.mfwk.sudokuservice.core.SudokuAccess;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SudokuGUI extends JFrame {
    /**
     * Main method to start the GUI.
     *
     * @param args All arguments are ignored.
     */
    public static void main(String[] args) {
        try {                             //Set look-and-feel to system standard
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Beim Versuch der Look-and-Feel-Konfiguration ist ein Fehler aufgetreten.");
        }
        new SudokuGUI();                 //Start the GUI
    }

    private class CustomTextfield extends JTextField {
        /**
         * Constructor.
         *
         * @param cols The size of the text field.
         */
        public CustomTextfield(int cols) {
            super(cols);
        }

        /**
         * Sets the text field in a way that it holds one digit, excluding 0.
         *
         * @return The document model of the text field.
         */
        @Override
        protected Document createDefaultModel() {
            return new PlainDocument() {
                /**
                 * This method is called whenever the text field content is expanded.
                 * @param offs The position of the modification.
                 * @param str  The inserted text.
                 * @param a    The attributes.
                 * @throws BadLocationException Happens when the position is modified to invalidity.
                 */
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (str == null || str.length() != 1)
                        return;
                    if (this.getLength() > 0)         //Only one digit in the field
                        this.remove(0, 1);
                    try {                             //Insert acceptable digit
                        if (Integer.parseInt(str) != 0)
                            super.insertString(offs == 0 ? 0 : offs - 1, str, a);
                    } catch (NumberFormatException e) {
                    }
                }
            };
        }
    }

    private final CustomTextfield[][] textgrid;
    private final JTextField hostfield;
    private final JTextField portfield;
    private final JCheckBox btfield;

    /**
     * Constructor creating the complete GUI.
     */
    private SudokuGUI() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 500));
        this.setMinimumSize(new Dimension(500, 300));      //Basic window configuration
        this.setLocationRelativeTo(null);
        this.setTitle("Sudokulöser Client 1.2");

        this.textgrid = new CustomTextfield[9][9];             //Set up text fields for the sudoku grid
        JPanel grid = new JPanel(new GridLayout(3, 3, 20, 20));
        JPanel[] houses = new JPanel[9];
        for (int i = 0; i < 9; i++) {
            houses[i] = new JPanel(new GridLayout(3, 3, 10, 10));
            int a = ((i / 3) + 1) * 3 - 2;                      //Both functions were found by poking around
            int b = ((i + 1) % 3 == 0 ? 3 : (i + 1) % 3) * 3 - 2;
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
                    CustomTextfield textfield = new CustomTextfield(2);
                    textfield.setHorizontalAlignment(JTextField.CENTER);
                    textfield.setFont(new Font(textfield.getFont().getFontName(), textfield.getFont().getStyle(), 24));
                    this.textgrid[a + j][b + k] = textfield;
                    houses[i].add(textfield);
                }
            grid.add(houses[i]);
        }
        grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        grid.setMinimumSize(new Dimension(300, 480));

        JPanel config = new JPanel(new GridBagLayout());       //The configuration panel
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 5, 10, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        this.hostfield = new JTextField(SudokuClient.selfhosted, 20);
        this.portfield = new JTextField(5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        config.add(new JLabel("Hostname: "), constraints);
        constraints.gridx = 1;
        config.add(hostfield, constraints);
        constraints.gridy = 1;
        constraints.gridx = 0;
        config.add(new JLabel("Portnummer: "), constraints);
        constraints.gridx = 1;
        config.add(portfield, constraints);

        this.btfield = new JCheckBox("Backtracking verwenden", true);
        JButton pingbutton = new JButton("Ping");
        pingbutton.addActionListener(e -> SudokuGUI.this.ping());
        JButton valibutton = new JButton("Validieren");
        valibutton.addActionListener(e -> SudokuGUI.this.validator());
        JButton solvebutton = new JButton("Lösen");
        solvebutton.addActionListener(e -> {
            if (SudokuGUI.this.btfield.isSelected())
                SudokuGUI.this.solveguessing();
            else
                SudokuGUI.this.solve();
        });

        JPanel controls = new JPanel(new GridLayout(5, 1, 5, 5));
        controls.add(config);
        controls.add(this.btfield);
        controls.add(pingbutton);
        controls.add(valibutton);
        controls.add(solvebutton);
        controls.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, grid, controls);
        splitPane.setContinuousLayout(true);        //Put controls and grid together
        splitPane.setDividerLocation(500);
        this.add(splitPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu filemenu = new JMenu("Datei");        //Create the menu bar
        JMenuItem openitem = new JMenuItem("Datei öffnen");
        openitem.addActionListener(e -> SudokuGUI.this.openfile());
        filemenu.add(openitem);
        JMenuItem saveitem = new JMenuItem("Datei speichern");
        saveitem.addActionListener(e -> SudokuGUI.this.savefile());
        filemenu.add(saveitem);
        JMenuItem closeitem = new JMenuItem("Datei schließen");
        closeitem.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(SudokuGUI.this, "Möchten sie wirklich die Datei schließen?", "Datei schließen", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION)
                for (CustomTextfield[] line : SudokuGUI.this.textgrid)
                    for (CustomTextfield field : line)
                        field.setText("");
        });
        filemenu.add(closeitem);
        filemenu.addSeparator();
        JMenuItem quititem = new JMenuItem("Beenden");
        quititem.addActionListener(e -> SudokuGUI.this.dispose());
        filemenu.add(quititem);
        menuBar.add(filemenu);
        JMenu infomenu = new JMenu("Info");
        JMenuItem infoitem = new JMenuItem("Info");
        infoitem.addActionListener(e -> JOptionPane.showMessageDialog(SudokuGUI.this, "Sudokulöser 1.2\n\nDieses Programm wurde auf GitHub veröffentlicht:\nhttps://github.com/meekee7/SudokuSolverService", "Info", JOptionPane.INFORMATION_MESSAGE));
        infomenu.add(infoitem);
        menuBar.add(infomenu);
        this.setJMenuBar(menuBar);

        this.setVisible(true);                    //Do not forget this
    }

    /**
     * Creates a file choosing dialog with filters for sudoku files and XML files.
     *
     * @return A preconfigured filechooser.
     */
    private static JFileChooser makefilechooser() {
        JFileChooser fileChooser = new JFileChooser();    //Automatic all-files-filter remains first filter
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Sudoku-Dateien (*.sudo)", "sudo"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML-Dateien (*.xml)", "xml"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return fileChooser;
    }

    /**
     * Tries to open and load a sudoku.
     */
    private void openfile() {
        JFileChooser fileChooser = makefilechooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            try {
                this.setData(SudokuAccess.readSudoku(fileChooser.getSelectedFile().toPath()));
            } catch (JAXBException e) {
                JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Die Daten konnte nicht interpretiert werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Die Datei konnte nicht gespeichert werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
    }

    /**
     * Tries to save a sudoku.
     */
    private void savefile() {
        JFileChooser fileChooser = makefilechooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            try {
                SudokuAccess.writeSudoku(fileChooser.getSelectedFile().toPath(), this.getData());
            } catch (JAXBException e) {
                JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Die Daten konnten nicht geschrieben werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Die Datei konnte nicht gespeichert werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
    }

    /**
     * Stores the data from the grid into a sudoku.
     *
     * @return The current sudoku on the grid.
     */
    private Sudoku getData() {
        int[][] grid = new int[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                int value = 0;
                if (!this.textgrid[i][j].getText().isEmpty())
                    try {
                        value = Integer.parseInt(this.textgrid[i][j].getText());
                    } catch (NumberFormatException e) {
                    }                                  //Test input again for validity
                grid[i][j] = (value < 0 || value > 9) ? 0 : value;
            }
        return new Sudoku(grid);
    }

    /**
     * Loads a sudoku to the grid.
     *
     * @param sudoku The sudoku to load.
     */
    private void setData(Sudoku sudoku) {
        int[][] grid = sudoku.getSudoku();
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                this.textgrid[i][j].setText(grid[i][j] == 0 ? "" : Integer.toString(grid[i][j]));
    }

    /**
     * Creates a preconfigured client for the web service.
     *
     * @return The client for the service, null if the client could not be created.
     */
    private SudokuClient makeClient() {
        try {                                          //Configuration information from GUI
            String host = this.hostfield.getText();
            int port = host.equals(SudokuClient.selfhosted) ? 0 : Integer.parseInt(this.portfield.getText());
            return new SudokuClient(host, port);
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Die Hostname oder die Portangabe sind ungültig.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Die Portangabe ist ungültig.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Der Webservice ist nicht verfügbar.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Attempts a solve request to the service.
     */
    private void solveguessing() {
        SudokuClient client = this.makeClient();
        if (client == null)
            return;
        this.setData(client.solverguessingrequest(this.getData()));
    }

    /**
     * Attempts a solve request to the service.
     */
    private void solve() {
        SudokuClient client = this.makeClient();
        if (client == null)
            return;
        this.setData(client.solverequest(this.getData()));
    }

    /**
     * Attempts a validation request to the service.
     */
    private void validator() {
        SudokuClient client = this.makeClient();
        if (client == null)
            return;
        int result = client.validaterequest(this.getData());
        if (result < 0)
            JOptionPane.showMessageDialog(this, "Das Sudoku ist nicht gültig.", "Validierung", JOptionPane.ERROR_MESSAGE);
        else if (result == 0)
            JOptionPane.showMessageDialog(this, "Das Sudoku ist gültig und vollständig gelöst.", "Validierung", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(this, "Das Sudoku ist gültig, aber " + result + " Felder sind offen.", "Validierung", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Attemps a ping to the service.
     */
    private void ping() {
        SudokuClient client = this.makeClient();
        if (client != null)
            if (client.pingrequest())
                JOptionPane.showMessageDialog(this, "Ping erfolgreich, der Sudokuservice ist unter der aktuellen Konfiguration erreichbar.", "Ping", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Bei dem aktuellen Vorgang ist ein Fehler aufgetreten. Der angegebene Sudokuservice ist nicht erreichbar.", "Fehler", JOptionPane.ERROR_MESSAGE);
    }

}
