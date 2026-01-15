package com.citybuilder;

import com.citybuilder.core.GameEngine;

import javax.swing.*;

/**
 * Main entry point for the City Builder game.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GameEngine game = new GameEngine();
            game.start();
        });
    }
}
