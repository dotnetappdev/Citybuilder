package com.citybuilder;

import com.citybuilder.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Utility to create a game with buildings and take a screenshot.
 */
public class GameScreenshotUtil {
    public static void main(String[] args) throws Exception {
        // Set headless property to false for GUI
        System.setProperty("java.awt.headless", "false");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Create a simple frame to show the game
                JFrame frame = new JFrame("City Builder - Graphics Demo");
                frame.setSize(1200, 800);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                // Create game state and initialize
                GameState gameState = new GameState();
                gameState.initializeNewGame();
                
                // Add some buildings for demonstration
                addSampleBuildings(gameState);
                
                // Create game panel
                com.citybuilder.ui.GamePanel gamePanel = new com.citybuilder.ui.GamePanel(gameState);
                frame.add(gamePanel);
                
                frame.setVisible(true);
                
                // Wait for rendering
                Thread.sleep(3000);
                
                // Capture screenshot
                BufferedImage screenshot = new BufferedImage(
                    frame.getWidth(),
                    frame.getHeight(),
                    BufferedImage.TYPE_INT_RGB
                );
                
                frame.paint(screenshot.getGraphics());
                
                // Save screenshot
                File output = new File("game-screenshot.png");
                ImageIO.write(screenshot, "png", output);
                System.out.println("Screenshot saved to: " + output.getAbsolutePath());
                
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    private static void addSampleBuildings(GameState gameState) {
        CityMap map = gameState.getCityMap();
        
        // Add a variety of buildings to showcase the improved graphics
        // Roads
        for (int x = 10; x <= 20; x++) {
            map.getTile(x, 10).setBuilding(new Building(BuildingType.ROAD));
        }
        for (int y = 10; y <= 15; y++) {
            map.getTile(15, y).setBuilding(new Building(BuildingType.ROAD));
        }
        
        // Roundabout
        map.getTile(15, 10).setBuilding(new Building(BuildingType.ROUNDABOUT));
        
        // Residential buildings
        map.getTile(12, 8).setBuilding(new Building(BuildingType.HOUSE));
        map.getTile(14, 8).setBuilding(new Building(BuildingType.HOUSE));
        map.getTile(16, 8).setBuilding(new Building(BuildingType.APARTMENT));
        map.getTile(18, 8).setBuilding(new Building(BuildingType.APARTMENT));
        
        // Commercial buildings
        map.getTile(12, 12).setBuilding(new Building(BuildingType.RESTAURANT));
        map.getTile(14, 12).setBuilding(new Building(BuildingType.SHOP));
        map.getTile(16, 12).setBuilding(new Building(BuildingType.FAST_FOOD));
        map.getTile(18, 12).setBuilding(new Building(BuildingType.MALL));
        
        // Service buildings
        map.getTile(10, 14).setBuilding(new Building(BuildingType.POLICE_STATION));
        map.getTile(12, 14).setBuilding(new Building(BuildingType.FIRE_STATION));
        map.getTile(14, 14).setBuilding(new Building(BuildingType.HOSPITAL));
        map.getTile(16, 14).setBuilding(new Building(BuildingType.SCHOOL));
        
        // Infrastructure
        map.getTile(20, 12).setBuilding(new Building(BuildingType.POWER_PLANT));
        map.getTile(20, 14).setBuilding(new Building(BuildingType.WATER_TOWER));
        
        // Industrial
        map.getTile(10, 8).setBuilding(new Building(BuildingType.FACTORY));
        map.getTile(20, 8).setBuilding(new Building(BuildingType.OFFICE));
        
        // Parks and amenities
        map.getTile(13, 15).setBuilding(new Building(BuildingType.PARK));
        map.getTile(17, 15).setBuilding(new Building(BuildingType.PARK));
        
        // Traffic light
        map.getTile(15, 10).setBuilding(new Building(BuildingType.TRAFFIC_LIGHT));
        gameState.addTrafficLight(new TrafficLight(15, 10));
        
        // Add some natural features
        map.getTile(8, 8).setNaturalFeature(NaturalFeature.TREE);
        map.getTile(22, 8).setNaturalFeature(NaturalFeature.TREE);
        map.getTile(8, 16).setNaturalFeature(NaturalFeature.BUSH);
        map.getTile(22, 16).setNaturalFeature(NaturalFeature.ROCK);
        
        // Add some zones
        map.getTile(8, 10).setZoneType(ZoneType.RESIDENTIAL);
        map.getTile(8, 11).setZoneType(ZoneType.RESIDENTIAL);
        map.getTile(22, 10).setZoneType(ZoneType.COMMERCIAL);
        map.getTile(22, 11).setZoneType(ZoneType.COMMERCIAL);
        map.getTile(10, 16).setZoneType(ZoneType.INDUSTRIAL);
        map.getTile(11, 16).setZoneType(ZoneType.INDUSTRIAL);
    }
}
