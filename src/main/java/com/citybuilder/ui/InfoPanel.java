package com.citybuilder.ui;

import com.citybuilder.model.*;

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
    private JLabel dateLabel;
    private JLabel timeLabel;
    private JComboBox<Currency> currencySelector;
    
    public InfoPanel(GameState gameState) {
        this.gameState = gameState;
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        setBackground(new Color(50, 50, 50));
        setPreferredSize(new Dimension(0, 50));
        
        // Currency selector
        currencySelector = new JComboBox<>(Currency.values());
        currencySelector.setSelectedItem(gameState.getCurrency());
        currencySelector.addActionListener(e -> {
            Currency selected = (Currency) currencySelector.getSelectedItem();
            gameState.setCurrency(selected);
            update();
        });
        add(new JLabel("💱"));
        add(currencySelector);
        
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
        
        // Date label
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(Color.LIGHT_GRAY);
        
        // Time label
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeLabel.setForeground(Color.ORANGE);
        
        add(moneyLabel);
        add(populationLabel);
        add(happinessLabel);
        add(incomeLabel);
        add(dateLabel);
        add(timeLabel);
        
        update();
    }
    
    public void update() {
        Currency curr = gameState.getCurrency();
        moneyLabel.setText("💰 " + curr.format(gameState.getMoney()));
        populationLabel.setText("👥 " + gameState.getPopulation());
        happinessLabel.setText("😊 " + String.format("%.1f%%", gameState.getCityHappiness()));
        
        int netIncome = gameState.getMonthlyIncome() - gameState.getMonthlyExpenses();
        String incomeText = "📊 ";
        if (netIncome >= 0) {
            incomeText += "+" + curr.format(netIncome);
        } else {
            incomeText += curr.format(netIncome);
        }
        incomeLabel.setText(incomeText + "/mo");
        incomeLabel.setForeground(netIncome >= 0 ? Color.GREEN : Color.RED);
        
        dateLabel.setText("📅 " + gameState.getGameDate().getFormattedDate());
        
        TimeOfDay tod = gameState.getCurrentTimeOfDay();
        String timeIcon = tod.isNight() ? "🌙" : "☀️";
        timeLabel.setText(timeIcon + " " + gameState.getGameDate().getFormattedTime());
    }
}
