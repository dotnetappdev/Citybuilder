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
    private JLabel happinessLabel;
    private JLabel incomeLabel;
    private JLabel monthLabel;
    
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
        
        // Happiness label
        happinessLabel = new JLabel();
        happinessLabel.setFont(new Font("Arial", Font.BOLD, 16));
        happinessLabel.setForeground(Color.YELLOW);
        
        // Income label
        incomeLabel = new JLabel();
        incomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        incomeLabel.setForeground(Color.CYAN);
        
        // Month label
        monthLabel = new JLabel();
        monthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        monthLabel.setForeground(Color.LIGHT_GRAY);
        
        add(moneyLabel);
        add(populationLabel);
        add(happinessLabel);
        add(incomeLabel);
        add(monthLabel);
        
        update();
    }
    
    public void update() {
        moneyLabel.setText("💰 $" + gameState.getMoney());
        populationLabel.setText("👥 " + gameState.getPopulation());
        happinessLabel.setText("😊 " + String.format("%.1f%%", gameState.getCityHappiness()));
        
        int netIncome = gameState.getMonthlyIncome() - gameState.getMonthlyExpenses();
        String incomeText = "📊 ";
        if (netIncome >= 0) {
            incomeText += "+$" + netIncome;
        } else {
            incomeText += "-$" + Math.abs(netIncome);
        }
        incomeLabel.setText(incomeText + "/mo");
        incomeLabel.setForeground(netIncome >= 0 ? Color.GREEN : Color.RED);
        
        monthLabel.setText("📅 Month " + gameState.getGameMonth());
    }
}
