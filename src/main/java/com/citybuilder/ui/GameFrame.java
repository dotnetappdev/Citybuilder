package com.citybuilder.ui;

import com.citybuilder.core.GameEngine;
import com.citybuilder.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main game frame that contains the game view and UI.
 */
public class GameFrame extends JFrame {
    private static final int RESIDENT_SPAWN_DIVISOR = 10; // How many building capacity units per resident
    private static final int VEHICLE_CLEANUP_MARGIN = 10; // Distance beyond map to remove vehicles
    
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
        
        // Start update timer (fast for game updates)
        Timer fastTimer = new Timer(50, e -> {
            gameState.updateTraffic();
            cleanupVehicles(); // Clean up off-map vehicles frequently
            gamePanel.repaint();
        });
        fastTimer.start();
        
        // Monthly update timer (3 seconds = 1 game month)
        Timer monthlyTimer = new Timer(3000, e -> {
            gameState.updateMonthly();
            spawnVehicles();
            updatePopulation();
            infoPanel.update();
        });
        monthlyTimer.start();
        
        // UI update timer
        Timer uiTimer = new Timer(100, e -> {
            infoPanel.update();
        });
        uiTimer.start();
    }
    
    private void spawnVehicles() {
        // Spawn random vehicles on roads
        CityMap map = gameState.getCityMap();
        for (int i = 0; i < 3; i++) {
            int x = (int) (Math.random() * map.getWidth());
            int y = (int) (Math.random() * map.getHeight());
            Tile tile = map.getTile(x, y);
            if (tile != null && tile.getBuilding() != null && 
                (tile.getBuilding().getType() == BuildingType.ROAD || 
                 tile.getBuilding().getType() == BuildingType.ROUNDABOUT)) {
                VehicleType type = VehicleType.values()[(int) (Math.random() * VehicleType.values().length)];
                gameState.addVehicle(new Vehicle(x, y, type));
            }
        }
    }
    
    private void cleanupVehicles() {
        // Remove vehicles that are far off map
        CityMap map = gameState.getCityMap();
        gameState.getVehicles().removeIf(v -> 
            v.getX() < -VEHICLE_CLEANUP_MARGIN || v.getX() > map.getWidth() + VEHICLE_CLEANUP_MARGIN ||
            v.getY() < -VEHICLE_CLEANUP_MARGIN || v.getY() > map.getHeight() + VEHICLE_CLEANUP_MARGIN
        );
    }
    
    private void updatePopulation() {
        // Count residents from residential buildings
        int totalCapacity = 0;
        CityMap map = gameState.getCityMap();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                Building building = tile.getBuilding();
                if (building != null) {
                    totalCapacity += building.getType().getResidents();
                    
                    // Add residents to happy buildings
                    if (tile.hasElectricity() && tile.hasWater()) {
                        for (int i = 0; i < building.getType().getResidents() / RESIDENT_SPAWN_DIVISOR; i++) {
                            if (gameState.getPopulation() < totalCapacity) {
                                Resident resident = new Resident(x, y);
                                resident.adjustHappiness(10); // Happy with utilities
                                gameState.addResident(resident);
                            }
                        }
                    }
                }
            }
        }
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
