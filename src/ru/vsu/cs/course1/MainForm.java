package ru.vsu.cs.course1;
import javax.swing.*;

import static guru.nidi.graphviz.attribute.Label.Justification.*;
import static guru.nidi.graphviz.model.Factory.*;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.parse.*;
import guru.nidi.graphviz.service.*;
import guru.nidi.graphviz.use.*;
import guru.nidi.graphviz.*;

import java.awt.*;
import java.awt.image.BufferedImage;


public class MainForm {
    private static final String FORM_TITLE = "Task 7_15 by @kalitkin_a_v";
    private JPanel rootPanel;
    private JButton loadDotButton;
    private JButton saveDotButton;
    private JButton generateButton;
    private JButton findWayButton;
    private JButton exitButton;
    private JSpinner nSpinner;
    private JSpinner kSpinner;
    private JSpinner sSpinner;
    private JSpinner pSpinner;
    private ImagePanel imagePanel;
    private JButton splitButton;
    private JPanel drawPanel;
    private JTextArea textArea1;

    private int getN() { return (int) nSpinner.getValue(); }
    private int getK() { return (int) kSpinner.getValue(); }
    private int getS() { return (int) sSpinner.getValue(); }
    private int getP() { return (int) pSpinner.getValue(); }

    public static void main(String[] args) {
        FormUtils.prepare();
        MainForm mainForm = new MainForm();

        JFrame frame = new JFrame(FORM_TITLE);
        frame.setContentPane(mainForm.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FormUtils.processFormAfter(mainForm.rootPanel, frame, true);
    }

    MainForm() {
        nSpinner.setModel(new SpinnerNumberModel(20,  2, 1000, 2));
        kSpinner.setModel(new SpinnerNumberModel(1,  1, 100, 1));
        sSpinner.setModel(new SpinnerNumberModel(1,  1, 100, 1));
        pSpinner.setModel(new SpinnerNumberModel(1,  1, 100, 1));
        generateButton.addActionListener(e -> generateButtonClicked());
        exitButton.addActionListener(e -> System.exit(0));
        imagePanel = new ImagePanel();
        drawPanel.setLayout(new GridLayout());
        drawPanel.add(imagePanel);
    }

    private void generateButtonClicked() {
        RelationshipGraph graph = GraphUtils.generate(getN());
        BufferedImage image = Graphviz.fromString(graph.toDot()).render(Format.SVG).toImage();
        imagePanel.update(image);
    }
}
