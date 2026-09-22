import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UserQueryCSVDialog {

    private JFrame mainFrame;
    private GenericDataReaderIntoMap ourGenericDataReaderIntoMap;
    private String userInput;
    private JTextArea resultsArea;
    private JTextField inputField;
    private JLabel queryLabel;
    //private static final String DEFAULT_DIRECTORY = ".\\DataFiles\\ThirdPartyDataCSVFiles";
    private static final String DEFAULT_DIRECTORY = ".\\datafiles";
    private static final String DEFAULT_FILE = "TopSongs5000Edited.csv";

    public UserQueryCSVDialog() {
        showFileChooser();
    }

    private void showFileChooser() {
        // Set up the JFileChooser
        JFileChooser fileChooser = new JFileChooser(DEFAULT_DIRECTORY);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Optional: add a file filter for CSV
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        // Preselect the default file if it exists
        File defaultFile = new File(DEFAULT_DIRECTORY, DEFAULT_FILE);
        if (defaultFile.exists()) {
            fileChooser.setSelectedFile(defaultFile);
        }

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            showHashOptionsAndProceed(selectedFile);
        } else {
            System.exit(0); // Exit if cancelled
        }
    }

    private void showHashOptionsAndProceed(File selectedFile) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        // Hash Function selection
        JLabel hashFunctionLabel = new JLabel("Hash Function:");
        String[] hashFunctions = {"Default", "StringLength", "DJB2", "CyclicShiftExample", "PolynomialHash"};
        JComboBox<String> hashFunctionComboBox = new JComboBox<>(hashFunctions);
        panel.add(hashFunctionLabel);
        panel.add(hashFunctionComboBox);

        // Table Size input
        JLabel tableSizeLabel = new JLabel("Table Size:");
        JTextField tableSizeField = new JTextField("128");
        panel.add(tableSizeLabel);
        panel.add(tableSizeField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter hash function name and table size", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedHashFunction = (String) hashFunctionComboBox.getSelectedItem();
            String tableSizeText = tableSizeField.getText();
            try {
                int tableSize = Integer.parseInt(tableSizeText);
                openUserQueryDialog(selectedFile.getAbsolutePath(), selectedHashFunction, tableSize);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid table size. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                showHashOptionsAndProceed(selectedFile); // Show again
            }
        } else {
            System.exit(0); // Exit if cancelled
        }
    }

    private void openUserQueryDialog(String filename, String hashFunction, int tableSize) {
        // Instantiate the GenericDataReaderIntoMap with the selected file
        ourGenericDataReaderIntoMap = new GenericDataReaderIntoMap(filename, hashFunction, tableSize);


        // Prepare the main dialog window
        mainFrame = new JFrame("User Query CSV Dialog");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());

        // Panel at the top for input and submit button
        JPanel inputPanel = new JPanel(new BorderLayout(8, 8));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // Label using keyTitle
        String labelText = ourGenericDataReaderIntoMap.keyTitle + ": <Enter key>";
        queryLabel = new JLabel(labelText);
        queryLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Submit button action
        submitButton.addActionListener(e -> handleUserInput());

        // Allow Enter key to trigger submit
        inputField.addActionListener(e -> handleUserInput());

        inputPanel.add(queryLabel, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        // Results area in center
        resultsArea = new JTextArea();
        resultsArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setEditable(false);
        JScrollPane resultsScroll = new JScrollPane(resultsArea);

        // Panel at the bottom for action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton countsButton = new JButton("Bucket counts");
        countsButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        countsButton.addActionListener(e -> {
            String counts = ourGenericDataReaderIntoMap.keyValuePairs.getTableCounts();
            appendResult("\n--- Bucket Counts ---\n" + counts);
        });

        JButton keysButton = new JButton("Bucket keys");
        keysButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        keysButton.addActionListener(e -> {
            if (ourGenericDataReaderIntoMap.keyValuePairs instanceof SimpleMapHashTable) {
                String keys = ((SimpleMapHashTable) ourGenericDataReaderIntoMap.keyValuePairs).getTableKeys();
                appendResult("\n--- Bucket Keys ---\n" + keys);
            } else {
                appendResult("\n--- Error: getTableKeys() not available on this map type. ---\n");
            }
        });

        JButton contentButton = new JButton("Bucket content");
        contentButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        contentButton.addActionListener(e -> {
            String content = ourGenericDataReaderIntoMap.keyValuePairs.getTableContent();
            appendResult("\n--- Bucket Content ---\n" + content);
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 16));

        clearButton.addActionListener(e -> {
            inputField.setText("");
            resultsArea.setText("");
            inputField.requestFocusInWindow();
        });

        bottomPanel.add(countsButton);
        bottomPanel.add(keysButton);
        bottomPanel.add(contentButton);
        bottomPanel.add(clearButton);

        mainFrame.add(inputPanel, BorderLayout.NORTH);
        mainFrame.add(resultsScroll, BorderLayout.CENTER);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    private void handleUserInput() {
        userInput = inputField.getText().trim();
        if (userInput.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please enter a key to look up.", "Input Required", JOptionPane.WARNING_MESSAGE);
            inputField.requestFocusInWindow();
            return;
        }
        // Attempt to query the CSV using keyValueLookup
        String result = ourGenericDataReaderIntoMap.keyValueLookup(userInput);
        if (result != null) {
            appendResult("> " + userInput);
            appendResult(result);
        }
        //if (result != null) {
        //    appendResult("> " + userInput);
        //    appendResult(result);
        //} else {
            // Try keyValueNearby if not found
        //    String nearby = ourGenericDataReaderIntoMap.keyValueNearby(userInput);
        //    appendResult("> " + userInput);
        //    appendResult(nearby != null ? nearby : "No result or close matches found.");
        //}
        inputField.setText("");
        inputField.requestFocusInWindow();
    }

    void appendResult(String text) {
        resultsArea.append(text + "\n");
        resultsArea.setCaretPosition(resultsArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        // For better cross-platform appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(UserQueryCSVDialog::new);
        //SimpleMapHashTableVisualizer.display();
    }
}
