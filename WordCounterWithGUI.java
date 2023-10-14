package WordCounter;

import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WordCounterWithGUI {
    private JTextArea textArea;
    private JLabel wordCountLabel;
    private JLabel uniqueWordCountLabel;
    private JTextArea statisticsArea;
    private int wordCount;  

    public WordCounterWithGUI() {
        JFrame frame = new JFrame("Word Counter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea(10, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        wordCountLabel = new JLabel("Word Count: 0");
        uniqueWordCountLabel = new JLabel("Unique Word Count: 0");

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());
        labelPanel.add(wordCountLabel);
        labelPanel.add(uniqueWordCountLabel);
        frame.add(labelPanel, BorderLayout.NORTH);

        statisticsArea = new JTextArea(10, 40);
        statisticsArea.setEditable(false);
        frame.add(new JScrollPane(statisticsArea), BorderLayout.SOUTH);

        JButton countButton = new JButton("Count Words");
        countButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                countWords();
            }
        });

        JButton openFileButton = new JButton("Open File");
        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(countButton);
        buttonPanel.add(openFileButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private String[] splitText(String text) {
        return text.split("[\\s\\p{Punct}]+");
    }

    private void countWords() {
        String text = getTextInput();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Text input is empty. Please enter text.");
            return;
        }

        String[] words = splitText(text);

        // Handle stop words
        Set<String> stopWords = new HashSet<>(Arrays.asList("the", "a", "an", "in", "on", "and", "is", "it"));

        List<String> filteredWords = new ArrayList<>();
        Map<String, Integer> wordFrequency = new HashMap();

        for (String word : words) {
            if (!stopWords.contains(word.toLowerCase())) {
                filteredWords.add(word);
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                wordCount++;  // Increment the word count variable
            }
        }

        int uniqueWordCount = wordFrequency.size();

        wordCountLabel.setText("Word Count: " + wordCount);
        uniqueWordCountLabel.setText("Unique Word Count: " + uniqueWordCount);

        // Display statistics
        StringBuilder statistics = new StringBuilder();
        statistics.append("Word Frequency:\n");
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            statistics.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        statisticsArea.setText(statistics.toString());
    }

    private String getTextInput() {
        return textArea.getText();
    }

    private String getFileInput(File selectedFile) {
        try {
            StringBuilder fileContent = new StringBuilder();
            Scanner scanner = new Scanner(selectedFile);

            while (scanner.hasNextLine()) {
                fileContent.append(scanner.nextLine()).append("\n");
            }

            return fileContent.toString();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error opening the selected file.");
        }
        return null;
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileText = getFileInput(selectedFile);

            if (fileText != null) {
                textArea.setText(fileText);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WordCounterWithGUI();
            }
        });
    }
}