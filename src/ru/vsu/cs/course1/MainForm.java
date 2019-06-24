package ru.vsu.cs.course1;
import javax.swing.*;

import guru.nidi.graphviz.engine.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;


public class MainForm {
    private static final String FORM_TITLE = "Task 7_15 by @kalitkin_a_v";
    private JPanel rootPanel;
    private JButton saveDotButton;
    private JButton saveSVGButton;
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
    private JTextArea resultTextArea;
    private RelationshipGraph graph;
    private String dot;
    private String svg;

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

    private MainForm() {
        nSpinner.setModel(new SpinnerNumberModel(10,  2, 1000, 2));
        kSpinner.setModel(new SpinnerNumberModel(0,  0, 100, 1));
        sSpinner.setModel(new SpinnerNumberModel(1,  1, 100, 1));
        pSpinner.setModel(new SpinnerNumberModel(1,  0, 100, 1));
        saveDotButton.addActionListener(e -> saveDot());
        saveSVGButton.addActionListener(e -> saveSvg());
        generateButton.addActionListener(e -> generate());
        findWayButton.addActionListener(e -> findWay());
        splitButton.addActionListener(e -> split());
        exitButton.addActionListener(e -> System.exit(0));
        imagePanel = new ImagePanel();
        drawPanel.setLayout(new GridLayout());
        drawPanel.add(imagePanel);
    }

    private void generate() {
        graph = GraphUtils.generate(getN(), getK());
        dot = graph.toDot();
        drawGraph();
    }

    private void findWay() {
        if(graph == null)
            return;
        AtomicReference<String> ref = new AtomicReference<>();
        dot = graph.toDotWay(ref);
        resultTextArea.setText(ref.get());
        redrawResult();
        if (dot == null)
            return;
        drawGraph();
    }

    private void split() {
        if(graph == null)
            return;
        String warn = "";
        if(getS() * getP() < getK()) {
            warn = "Required S*P>=K\r\n";
        }
        AtomicReference<String> ref = new AtomicReference<>();
        dot = graph.toDotGroups(getS(), getP(), ref);
        resultTextArea.setText(warn+ref.get());
        redrawResult();
        if (dot == null)
            return;
        drawGraph();
    }

    private void redrawResult() {
        resultTextArea.paintImmediately(0, 0, resultTextArea.getWidth(), resultTextArea.getHeight());
    }

    private void drawGraph() {
        BufferedImage image = Graphviz.fromString(dot).render(Format.SVG).toImage();
        imagePanel.update(image);
    }

    private void saveDot() {
        Utils.saveToFile("graph.dot", dot);
    }

    private void saveSvg() {
        Utils.saveToFile("graph.svg", Graphviz.fromString(dot).render(Format.SVG).toString());
    }

}
