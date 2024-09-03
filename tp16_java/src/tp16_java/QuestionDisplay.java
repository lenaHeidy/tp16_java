package tp16_java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionDisplay extends JFrame {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private JLabel titleLabel;
    private JLabel stimulusLabel;
    private JTextArea promptArea;
    private JRadioButton[] choiceButtons;
    private JButton nextButton;
    private Timer timer;
    private JLabel timerLabel;
    private int timeRemaining = 30 * 60; // 30 minutos en segundos
    private int score = 0;

    public QuestionDisplay(List<Question> questions) {
        this.questions = questions;
        Collections.shuffle(this.questions); // Mezcla las preguntas

        // Configuración de la ventana
        setTitle("Multiple Choice Quiz");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        // Componentes
        titleLabel = new JLabel();
        stimulusLabel = new JLabel();
        promptArea = new JTextArea();
        promptArea.setEditable(false);
        choiceButtons = new JRadioButton[4];
        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i] = new JRadioButton();
            buttonGroup.add(choiceButtons[i]);
            add(choiceButtons[i]);
        }
        nextButton = new JButton("Siguiente");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextQuestion();
            }
        });
        add(nextButton);

        timerLabel = new JLabel();
        add(timerLabel);

        // Iniciar el temporizador
        startTimer();

        // Mostrar la primera pregunta
        showQuestion();
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            titleLabel.setText("Title: " + q.getTitle());
            stimulusLabel.setText("Stimulus: " + q.getStimulus());
            promptArea.setText(q.getPrompt());

            for (int i = 0; i < q.getChoices().size(); i++) {
                Question.Choice choice = q.getChoices().get(i);
                choiceButtons[i].setText(choice.getContent());
                choiceButtons[i].setActionCommand(choice.getId());
            }
            currentQuestionIndex++;
        } else {
            endQuiz();
        }
    }

    private void showNextQuestion() {
        // Aquí deberías implementar la lógica para verificar la respuesta y calcular el puntaje
        showQuestion();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
                if (timeRemaining <= 0) {
                    timer.cancel();
                    endQuiz();
                }
            }
        }, 0, 1000);
    }

    private void endQuiz() {
        // Mostrar el puntaje
        JOptionPane.showMessageDialog(this, "Quiz terminado. Puntaje: " + score);
        System.exit(0);
    }
}
