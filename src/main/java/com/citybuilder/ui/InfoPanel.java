package com.citybuilder.ui;

import com.citybuilder.model.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Information panel showing game stats.
 */
public class InfoPanel extends JPanel {
    private GameState gameState;
    private JLabel moneyLabel;
    private JLabel populationLabel;
    
    public InfoPanel(GameState gameState) {
        this.gameState = gameState;
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        setBackground(new Color(50, 50, 50));
        setPreferredSize(new Dimension(0, 50));
        
        // Money label
        moneyLabel = new JLabel();
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setForeground(Color.GREEN);
        
        // Population label
        populationLabel = new JLabel();
        populationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        populationLabel.setForeground(Color.WHITE);
        
        add(moneyLabel);
        add(populationLabel);
        
        update();
    }
    
    public void update() {
        moneyLabel.setText("Money: $" + gameState.getMoney());
        populationLabel.setText("Population: " + gameState.getPopulation());
    }
}
