import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notes_AppGUI extends JFrame implements ActionListener {

    private static final String FILE_NAME = "notes.txt";

    private JTextArea noteArea;
    private JTextArea displayArea;
    private JButton createBtn, appendBtn, viewBtn, clearBtn;

    public Notes_AppGUI() {
        setTitle("Notes App");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240)); // light gray
        setContentPane(mainPanel);


        noteArea = new JTextArea(8, 50);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(noteArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Write a Note"));
        mainPanel.add(inputScroll, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        createBtn = new JButton("Create Note");
        appendBtn = new JButton("Append Note");
        viewBtn = new JButton("View Notes");
        clearBtn = new JButton("Clear Notes");

        /*createBtn.addActionListener(this);
        appendBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        clearBtn.addActionListener(this); // register new button

        buttonPanel.add(createBtn);
        buttonPanel.add(appendBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(clearBtn); // add to layout
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        */
        Color buttonColor = new Color(76, 175, 80); // green
        Color buttonText = Color.WHITE;

        JButton[] buttons = { createBtn, appendBtn, viewBtn, clearBtn };
        for (JButton btn : buttons) {
            btn.setBackground(buttonColor);
            btn.setForeground(buttonText);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            buttonPanel.add(btn);
            btn.addActionListener(this);
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);


        displayArea = new JTextArea(10, 50);
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setBackground((Color.white));
        JScrollPane outputScroll = new JScrollPane(displayArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Saved Notes"));
        mainPanel.add(outputScroll, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == createBtn) {
            createNote();
        } else if (source == appendBtn) {
            appendNote();
        } else if (source == viewBtn) {
            viewNote();
        } else if (source == clearBtn) {
            clearNotes();
        }
    }

    private void createNote() {
        String content = noteArea.getText().trim();
        System.out.println("Input captured: [" + content + "]");

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Note is empty. Please enter some text.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(content);
            JOptionPane.showMessageDialog(this, "Note created successfully!");
            noteArea.setText("");
        } catch (IOException e) {
            showError("Error writing note: " + e.getMessage());
        }
    }


    private void appendNote() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.newLine();
            writer.write(noteArea.getText().trim());
            JOptionPane.showMessageDialog(this, "Note appended successfully!");
            noteArea.setText("");
        } catch (IOException e) {
            showError("Error appending note: " + e.getMessage());
        }
    }

    private void viewNote() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            displayArea.setText("");
            String line;
            while ((line = reader.readLine()) != null) {
                displayArea.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            showError("File not found. Create a note first.");
        } catch (IOException e) {
            showError("Error reading notes: " + e.getMessage());
        }
    }

    private void clearNotes() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete all notes?",
                "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                writer.write(""); // Overwrite with empty content
                displayArea.setText("");
                JOptionPane.showMessageDialog(this, "All notes cleared!");
            } catch (IOException e) {
                showError("Error clearing notes: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        Notes_AppGUI app = new Notes_AppGUI();
        app.setVisible(true);
    }
}
