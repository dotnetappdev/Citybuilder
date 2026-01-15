package com.citybuilder.ui;

import com.citybuilder.core.GameEngine;
import com.citybuilder.model.GameState;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Main menu frame for the game.
 */
public class MainMenuFrame extends JFrame {
    private GameEngine gameEngine;
    
    public MainMenuFrame(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        
        setTitle("City Builder - Main Menu");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("CITY BUILDER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Menu buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 50, 100));
        
        JButton newGameButton = createMenuButton("New Game");
        newGameButton.addActionListener(e -> {
            gameEngine.startNewGame();
        });
        
        JButton loadGameButton = createMenuButton("Load Game");
        loadGameButton.addActionListener(e -> {
            showLoadGameDialog();
        });
        
        JButton exitButton = createMenuButton("Exit");
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
        
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(loadGameButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(exitButton);
        
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        button.setFocusPainted(false);
        return button;
    }
    
    private void showLoadGameDialog() {
        List<String> saves = GameState.listSavedGames();
        
        if (saves.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No saved games found!", 
                "Load Game", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] saveArray = saves.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select a save game:",
            "Load Game",
            JOptionPane.QUESTION_MESSAGE,
            null,
            saveArray,
            saveArray[0]
        );
        
        if (selected != null) {
            gameEngine.loadGame(selected);
        }
    }
}
