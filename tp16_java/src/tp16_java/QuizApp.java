package tp16_java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class QuizApp extends JFrame {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private JLabel titleLabel;
    private JLabel stimulusLabel;
    private JTextArea promptArea;
    private JPanel choicesPanel;
    private JButton nextButton;

    public QuizApp() {
        try {
            QuestionLoader questionLoader = new QuestionLoader();
            questions = questionLoader.loadQuestionsFromFile("questions.json");
            Collections.shuffle(questions); // Shuffle questions to randomize order
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading questions.");
            System.exit(1); // Exit the application if there is an error
        }

        setupUI();
        showQuestion();
    }

    private void setupUI() {
        setTitle("Quiz Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        stimulusLabel = new JLabel();
        stimulusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        promptArea = new JTextArea();
        promptArea.setEditable(false);
        promptArea.setLineWrap(true);
        promptArea.setWrapStyleWord(true);
        promptArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        choicesPanel = new JPanel();
        choicesPanel.setLayout(new GridLayout(0, 1));
        
        nextButton = new JButton("Siguiente");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check the selected option and update the score
                String selectedOption = getSelectedOption();
                if (selectedOption != null) {
                    Question question = questions.get(currentQuestionIndex);
                    if (isCorrectAnswer(question, selectedOption)) {
                        score += question.getPoints();
                    }
                }

                // Show the next question or finish if there are no more questions
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    showQuestion();
                } else {
                    // Show final score and exit
                    JOptionPane.showMessageDialog(QuizApp.this, "El cuestionario ha terminado.\nPuntuación final: " + score);
                    System.exit(0); // Exit the application
                }
            }
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(stimulusLabel, BorderLayout.CENTER);
        contentPanel.add(new JScrollPane(promptArea), BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(choicesPanel, BorderLayout.CENTER);
        bottomPanel.add(nextButton, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showQuestion() {
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se han cargado preguntas.");
            return;
        }

        Question question = questions.get(currentQuestionIndex);
        titleLabel.setText(question.getTitle());
        stimulusLabel.setText(question.getStimulus());
        promptArea.setText(question.getPrompt());

        choicesPanel.removeAll(); // Clear previous choices
        ButtonGroup buttonGroup = new ButtonGroup(); // Button group for radio buttons
        for (Question.Choice choice : question.getChoices()) {
            JRadioButton choiceButton = new JRadioButton(choice.getContent());
            choiceButton.setActionCommand(choice.getId());
            buttonGroup.add(choiceButton); // Add to button group
            choicesPanel.add(choiceButton);
        }
        choicesPanel.revalidate();
        choicesPanel.repaint();
    }

    private String getSelectedOption() {
        for (Component component : choicesPanel.getComponents()) {
            if (component instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) component;
                if (radioButton.isSelected()) {
                    return radioButton.getActionCommand();
                }
            }
        }
        return null;
    }

    private boolean isCorrectAnswer(Question question, String selectedOption) {
        for (List<String> answerList : question.getAnswers()) {
            if (answerList.contains(selectedOption)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizApp());
    }
}
