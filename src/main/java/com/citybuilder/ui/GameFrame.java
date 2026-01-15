package com.citybuilder.ui;

import com.citybuilder.core.GameEngine;
import com.citybuilder.model.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Main game frame that contains the game view and UI.
 */
public class GameFrame extends JFrame {
    private GameEngine gameEngine;
    private GameState gameState;
    private GamePanel gamePanel;
    private ToolboxPanel toolboxPanel;
    private InfoPanel infoPanel;
    
    public GameFrame(GameEngine gameEngine, GameState gameState) {
        this.gameEngine = gameEngine;
        this.gameState = gameState;
        
        setTitle("City Builder");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        initMenuBar();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Game panel (center)
        gamePanel = new GamePanel(gameState);
        add(gamePanel, BorderLayout.CENTER);
        
        // Toolbox panel (right)
        toolboxPanel = new ToolboxPanel(gameState, gamePanel);
        add(toolboxPanel, BorderLayout.EAST);
        
        // Info panel (top)
        infoPanel = new InfoPanel(gameState);
        add(infoPanel, BorderLayout.NORTH);
        
        // Start update timer
        Timer timer = new Timer(100, e -> {
            infoPanel.update();
            gamePanel.repaint();
        });
        timer.start();
    }
    
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem saveItem = new JMenuItem("Save Game");
        saveItem.addActionListener(e -> showSaveDialog());
        
        JMenuItem loadItem = new JMenuItem("Load Game");
        loadItem.addActionListener(e -> showLoadDialog());
        
        JMenuItem mainMenuItem = new JMenuItem("Main Menu");
        mainMenuItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                "Return to main menu? Unsaved progress will be lost.",
                "Confirm",
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                gameEngine.showMainMenu();
            }
        });
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(mainMenuItem);
        fileMenu.add(exitItem);
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        
        JMenuItem rotateLeftItem = new JMenuItem("Rotate Left (Q)");
        rotateLeftItem.addActionListener(e -> {
            gameState.getCamera().rotateCounterClockwise();
            gamePanel.repaint();
        });
        
        JMenuItem rotateRightItem = new JMenuItem("Rotate Right (E)");
        rotateRightItem.addActionListener(e -> {
            gameState.getCamera().rotateClockwise();
            gamePanel.repaint();
        });
        
        viewMenu.add(rotateLeftItem);
        viewMenu.add(rotateRightItem);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void showSaveDialog() {
        String filename = JOptionPane.showInputDialog(this,
            "Enter save name:",
            "Save Game",
            JOptionPane.QUESTION_MESSAGE);
        
        if (filename != null && !filename.trim().isEmpty()) {
            gameEngine.saveGame(filename.trim());
        }
    }
    
    private void showLoadDialog() {
        java.util.List<String> saves = GameState.listSavedGames();
        
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
            int result = JOptionPane.showConfirmDialog(this,
                "Load game? Unsaved progress will be lost.",
                "Confirm",
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                gameEngine.loadGame(selected);
            }
        }
    }
}
