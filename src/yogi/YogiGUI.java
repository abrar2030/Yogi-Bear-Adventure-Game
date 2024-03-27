package yogi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;


public class YogiGUI {

    private final JFrame frame;
    private final AdventureGame gameArea;

    
    public YogiGUI() {
        frame = new JFrame("Yogi Bear Adventure");
        gameArea = new AdventureGame();
        initializeInterface();
    }

    private void initializeInterface() {
        setupMainFrame();
        addMenuBar();
        frame.setVisible(true);
    }

    private void setupMainFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gameArea);
        frame.setPreferredSize(new Dimension(415, 460));
        frame.setResizable(false);
        frame.pack();
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = createGameMenu();
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private JMenu createGameMenu() {
        JMenu menu = new JMenu("Game Menu");
        menu.add(createMenuItem("New Game", this::actionNewGame));
        menu.add(createMenuItem("High Scores", this::displayScores));
        return menu;
    }

    private JMenuItem createMenuItem(String name, Runnable action) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(e -> action.run());
        return item;
    }

    private void actionNewGame() {
        gameArea.setLevel(0);
        gameArea.restart();
    }

    private void displayScores() {
        ArrayList<TopScore> scores = gameArea.getScores();
        Vector<String> columns = createColumns();
        Vector<Vector<Object>> scoreData = formatScoreData(scores);
        showScoresTable(columns, scoreData);
    }

    private Vector<String> createColumns() {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Rank");
        columnNames.add("Name");
        columnNames.add("Score");
        return columnNames;
    }

    private Vector<Vector<Object>> formatScoreData(ArrayList<TopScore> scores) {
        Vector<Vector<Object>> data = new Vector<>();
        for (int i = 0; i < scores.size(); i++) {
            Vector<Object> row = scoreToRow(scores.get(i), i);
            data.add(row);
        }
        return data;
    }

    private Vector<Object> scoreToRow(TopScore score, int position) {
        Vector<Object> row = new Vector<>();
        row.add(position + 1);
        row.add(score.getPlayerName());
        row.add(score.getPlayerScore());
        return row;
    }

    private void showScoresTable(Vector<String> columnNames, Vector<Vector<Object>> data) {
        JTable table = new JTable(data, columnNames);
        JScrollPane pane = new JScrollPane(table);
        JOptionPane.showMessageDialog(gameArea, pane, "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
}
