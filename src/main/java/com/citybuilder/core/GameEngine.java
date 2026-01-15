package com.citybuilder.core;

import com.citybuilder.model.GameState;
import com.citybuilder.ui.MainMenuFrame;
import com.citybuilder.ui.GameFrame;

import javax.swing.*;

/**
 * Core game engine that manages the game lifecycle.
 */
public class GameEngine {
    private GameState gameState;
    private MainMenuFrame mainMenu;
    private GameFrame gameFrame;
    
    public GameEngine() {
        this.gameState = new GameState();
    }
    
    public void start() {
        showMainMenu();
    }
    
    public void showMainMenu() {
        if (gameFrame != null) {
            gameFrame.dispose();
            gameFrame = null;
        }
        
        mainMenu = new MainMenuFrame(this);
        mainMenu.setVisible(true);
    }
    
    public void startNewGame() {
        if (mainMenu != null) {
            mainMenu.dispose();
            mainMenu = null;
        }
        
        gameState = new GameState();
        gameState.initializeNewGame();
        
        gameFrame = new GameFrame(this, gameState);
        gameFrame.setVisible(true);
    }
    
    public void loadGame(String filename) {
        if (mainMenu != null) {
            mainMenu.dispose();
            mainMenu = null;
        }
        
        gameState = GameState.loadFromFile(filename);
        if (gameState == null) {
            JOptionPane.showMessageDialog(null, 
                "Failed to load game: " + filename, 
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
            showMainMenu();
            return;
        }
        
        gameFrame = new GameFrame(this, gameState);
        gameFrame.setVisible(true);
    }
    
    public void saveGame(String filename) {
        if (gameState != null) {
            boolean success = gameState.saveToFile(filename);
            if (success) {
                JOptionPane.showMessageDialog(gameFrame, 
                    "Game saved successfully!", 
                    "Save Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(gameFrame, 
                    "Failed to save game!", 
                    "Save Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public GameState getGameState() {
        return gameState;
    }
}
