package com.citybuilder.ui;

import com.citybuilder.model.BuildingType;
import com.citybuilder.model.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Toolbox panel with building options.
 */
public class ToolboxPanel extends JPanel {
    private GameState gameState;
    private GamePanel gamePanel;
    
    public ToolboxPanel(GameState gameState, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(40, 40, 40));
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initUI();
    }
    
    private void initUI() {
        // Title
        JLabel titleLabel = new JLabel("TOOLBOX");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Terrain tools section
        addSectionLabel("Terrain");
        addToolButton("Raise Terrain", e -> gamePanel.setToolMode(ToolMode.RAISE_TERRAIN));
        addToolButton("Lower Terrain", e -> gamePanel.setToolMode(ToolMode.LOWER_TERRAIN));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Zone tools section
        addSectionLabel("Zones");
        addToolButton("Residential", e -> gamePanel.setToolMode(ToolMode.ZONE_RESIDENTIAL));
        addToolButton("Commercial", e -> gamePanel.setToolMode(ToolMode.ZONE_COMMERCIAL));
        addToolButton("Industrial", e -> gamePanel.setToolMode(ToolMode.ZONE_INDUSTRIAL));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Roads section
        addSectionLabel("Roads");
        addBuildingButton("Road", BuildingType.ROAD);
        addBuildingButton("Roundabout", BuildingType.ROUNDABOUT);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Residential buildings section
        addSectionLabel("Residential");
        addBuildingButton("House", BuildingType.HOUSE);
        addBuildingButton("Apartment", BuildingType.APARTMENT);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Services section
        addSectionLabel("Services");
        addBuildingButton("Police", BuildingType.POLICE_STATION);
        addBuildingButton("Fire Station", BuildingType.FIRE_STATION);
        addBuildingButton("School", BuildingType.SCHOOL);
        addBuildingButton("Hospital", BuildingType.HOSPITAL);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Commercial/Industrial section
        addSectionLabel("Commercial/Industrial");
        addBuildingButton("Shop", BuildingType.SHOP);
        addBuildingButton("Office", BuildingType.OFFICE);
        addBuildingButton("Factory", BuildingType.FACTORY);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Government section
        addSectionLabel("Government");
        addBuildingButton("Town Hall", BuildingType.TOWN_HALL);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Utilities section
        addSectionLabel("Utilities");
        addBuildingButton("Power Plant", BuildingType.POWER_PLANT);
        addBuildingButton("Water Tower", BuildingType.WATER_TOWER);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Other section
        addSectionLabel("Other");
        addBuildingButton("Park", BuildingType.PARK);
        addToolButton("Demolish", e -> gamePanel.setToolMode(ToolMode.DEMOLISH));
        
        // Add glue to push everything to the top
        add(Box.createVerticalGlue());
    }
    
    private void addSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.LIGHT_GRAY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private void addBuildingButton(String name, BuildingType type) {
        JButton button = new JButton("<html>" + name + "<br>$" + type.getCost() + "</html>");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 10));
        button.addActionListener(e -> {
            gamePanel.setSelectedBuilding(type);
            gamePanel.requestFocus();
        });
        add(button);
        add(Box.createRigidArea(new Dimension(0, 3)));
    }
    
    private void addToolButton(String name, java.awt.event.ActionListener listener) {
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 30));
        button.setFont(new Font("Arial", Font.PLAIN, 10));
        button.addActionListener(e -> {
            listener.actionPerformed(e);
            gamePanel.requestFocus();
        });
        add(button);
        add(Box.createRigidArea(new Dimension(0, 3)));
    }
}
