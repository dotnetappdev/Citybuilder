package com.citybuilder.ui;

import com.citybuilder.model.*;
import com.citybuilder.util.IsometricUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main game rendering panel.
 */
public class GamePanel extends JPanel {
    private static final int RADIAL_MENU_SIZE = 2000; // Large enough for any screen
    
    // Shadow and visual effect constants
    private static final int SHADOW_OFFSET_X = 3;
    private static final int SHADOW_OFFSET_Y = 3;
    private static final int SHADOW_ALPHA = 50;
    
    // Star badge constants for police station
    private static final int STAR_POINTS = 10;
    private static final int STAR_OUTER_RADIUS = 8;
    private static final int STAR_INNER_RADIUS = 4;
    
    private GameState gameState;
    private Point dragStart;
    private BuildingType selectedBuilding;
    private ToolMode toolMode;
    private RadialMenu radialMenu;
    
    public GamePanel(GameState gameState) {
        this.gameState = gameState;
        this.toolMode = ToolMode.NONE;
        this.radialMenu = new RadialMenu();
        
        setLayout(null); // Allow absolute positioning for radial menu
        add(radialMenu);
        radialMenu.setBounds(0, 0, RADIAL_MENU_SIZE, RADIAL_MENU_SIZE);
        
        setBackground(new Color(100, 150, 100));
        setFocusable(true);
        
        // Setup radial menu listener
        radialMenu.setListener(item -> {
            Object data = item.getData();
            if (data instanceof ToolMode) {
                setToolMode((ToolMode) data);
            } else if (data instanceof BuildingType) {
                setSelectedBuilding((BuildingType) data);
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Show radial menu on right-click
                    radialMenu.show(e.getPoint());
                    repaint();
                } else if (SwingUtilities.isMiddleMouseButton(e) || 
                    (SwingUtilities.isLeftMouseButton(e) && e.isControlDown())) {
                    dragStart = e.getPoint();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!radialMenu.isVisible()) {
                        handleClick(e.getX(), e.getY());
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    gameState.getCamera().move(dx, dy);
                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q:
                        gameState.getCamera().rotateCounterClockwise();
                        repaint();
                        break;
                    case KeyEvent.VK_E:
                        gameState.getCamera().rotateClockwise();
                        repaint();
                        break;
                    case KeyEvent.VK_W:
                        gameState.getCamera().move(0, 20);
                        repaint();
                        break;
                    case KeyEvent.VK_S:
                        gameState.getCamera().move(0, -20);
                        repaint();
                        break;
                    case KeyEvent.VK_A:
                        gameState.getCamera().move(20, 0);
                        repaint();
                        break;
                    case KeyEvent.VK_D:
                        gameState.getCamera().move(-20, 0);
                        repaint();
                        break;
                }
            }
        });
    }
    
    private void handleClick(int mouseX, int mouseY) {
        Point gridPos = screenToGrid(mouseX, mouseY);
        
        if (gameState.getCityMap().isValidPosition(gridPos.x, gridPos.y)) {
            Tile tile = gameState.getCityMap().getTile(gridPos.x, gridPos.y);
            
            switch (toolMode) {
                case BUILD:
                    if (selectedBuilding != null && tile.isEmpty()) {
                        if (gameState.spendMoney(selectedBuilding.getCost())) {
                            tile.setBuilding(new Building(selectedBuilding));
                            
                            // If it's a traffic light, also add to traffic light list
                            if (selectedBuilding == BuildingType.TRAFFIC_LIGHT) {
                                gameState.addTrafficLight(new TrafficLight(gridPos.x, gridPos.y));
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Not enough money!");
                        }
                    }
                    break;
                    
                case DEMOLISH:
                    tile.demolish();
                    break;
                    
                case ZONE_RESIDENTIAL:
                    if (tile.isEmpty()) {
                        tile.setZoneType(ZoneType.RESIDENTIAL);
                    }
                    break;
                    
                case ZONE_COMMERCIAL:
                    if (tile.isEmpty()) {
                        tile.setZoneType(ZoneType.COMMERCIAL);
                    }
                    break;
                    
                case ZONE_INDUSTRIAL:
                    if (tile.isEmpty()) {
                        tile.setZoneType(ZoneType.INDUSTRIAL);
                    }
                    break;
                    
                case RAISE_TERRAIN:
                    tile.raiseHeight();
                    break;
                    
                case LOWER_TERRAIN:
                    tile.lowerHeight();
                    break;
                    
                case SET_TRAFFIC_DIRECTION:
                    if (tile.isRoad()) {
                        tile.cycleTrafficDirection();
                    }
                    break;
            }
            
            repaint();
        }
    }
    
    private Point screenToGrid(int screenX, int screenY) {
        Camera camera = gameState.getCamera();
        
        // Adjust for camera offset
        int adjustedX = screenX - camera.getOffsetX();
        int adjustedY = screenY - camera.getOffsetY();
        
        // Convert to grid coordinates
        Point gridPos = IsometricUtils.screenToGrid(adjustedX, adjustedY);
        
        // Apply reverse rotation
        CityMap map = gameState.getCityMap();
        int rotation = (4 - camera.getRotation()) % 4;
        
        int x = gridPos.x;
        int y = gridPos.y;
        
        switch (rotation) {
            case 1:
                gridPos.x = y;
                gridPos.y = map.getWidth() - 1 - x;
                break;
            case 2:
                gridPos.x = map.getWidth() - 1 - x;
                gridPos.y = map.getHeight() - 1 - y;
                break;
            case 3:
                gridPos.x = map.getHeight() - 1 - y;
                gridPos.y = x;
                break;
        }
        
        return gridPos;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Camera camera = gameState.getCamera();
        CityMap map = gameState.getCityMap();
        
        // Render tiles
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                renderTile(g2d, tile, x, y);
            }
        }
        
        // Render vehicles
        for (Vehicle vehicle : gameState.getVehicles()) {
            renderVehicle(g2d, vehicle);
        }
        
        // Render traffic lights
        for (TrafficLight light : gameState.getTrafficLights()) {
            renderTrafficLight(g2d, light);
        }
        
        // Apply day/night overlay
        TimeOfDay timeOfDay = gameState.getCurrentTimeOfDay();
        float brightness = timeOfDay.getBrightness();
        if (brightness < 1.0f) {
            int alpha = (int) ((1.0f - brightness) * 128);
            g2d.setColor(new Color(0, 0, 30, alpha));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void renderTile(Graphics2D g2d, Tile tile, int gridX, int gridY) {
        Camera camera = gameState.getCamera();
        CityMap map = gameState.getCityMap();
        
        // Apply rotation
        Point rotated = camera.applyRotation(gridX, gridY, map.getWidth(), map.getHeight());
        
        // Convert to screen coordinates
        Point screen = IsometricUtils.gridToScreen(rotated.x, rotated.y, tile.getHeight());
        
        // Apply camera offset
        int screenX = screen.x + camera.getOffsetX();
        int screenY = screen.y + camera.getOffsetY();
        
        // Draw tile
        int tileWidth = IsometricUtils.getTileWidth();
        int tileHeight = IsometricUtils.getTileHeight();
        
        // Create isometric diamond shape
        int[] xPoints = {
            screenX, 
            screenX + tileWidth / 2, 
            screenX, 
            screenX - tileWidth / 2
        };
        int[] yPoints = {
            screenY - tileHeight / 2, 
            screenY, 
            screenY + tileHeight / 2, 
            screenY
        };
        
        // Determine tile color
        Color tileColor = getTileColor(tile);
        g2d.setColor(tileColor);
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // Draw tile border
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 4);
        
        // Draw zone overlay
        if (tile.getZoneType() != ZoneType.NONE) {
            Color zoneColor = getZoneColor(tile.getZoneType());
            g2d.setColor(new Color(zoneColor.getRed(), zoneColor.getGreen(), zoneColor.getBlue(), 100));
            g2d.fillPolygon(xPoints, yPoints, 4);
        }
        
        // Draw building or natural feature
        if (tile.getBuilding() != null) {
            drawBuilding(g2d, tile.getBuilding(), screenX, screenY);
            
            // Draw traffic direction arrow on roads
            if (tile.isRoad()) {
                drawTrafficArrow(g2d, tile.getTrafficDirection(), screenX, screenY);
            }
        } else if (tile.getNaturalFeature() != null) {
            drawNaturalFeature(g2d, tile.getNaturalFeature(), screenX, screenY);
        }
    }
    
    private Color getTileColor(Tile tile) {
        switch (tile.getTerrainType()) {
            case WATER:
                // More realistic water with depth variation
                int waterShade = Math.max(40, Math.min(120, 70 + tile.getHeight() * 5));
                return new Color(30, waterShade, waterShade + 80);
            case DIRT:
                // Richer brown earth tone
                return new Color(139, 100, 58);
            case SAND:
                // Warmer sand color
                return new Color(238, 224, 185);
            case GRASS:
            default:
                // More vibrant grass with height variation
                int heightShade = Math.max(80, Math.min(180, 110 + tile.getHeight() * 8));
                int baseGreen = Math.max(50, Math.min(220, 140 + tile.getHeight() * 6));
                return new Color(heightShade / 2, baseGreen, heightShade / 3);
        }
    }
    
    private Color getZoneColor(ZoneType zoneType) {
        switch (zoneType) {
            case RESIDENTIAL:
                // Brighter green for residential
                return new Color(100, 255, 100);
            case COMMERCIAL:
                // Brighter cyan/blue for commercial
                return new Color(80, 180, 255);
            case INDUSTRIAL:
                // Warmer yellow for industrial
                return new Color(255, 220, 80);
            default:
                return Color.WHITE;
        }
    }
    
    private void drawBuilding(Graphics2D g2d, Building building, int x, int y) {
        BuildingType type = building.getType();
        
        // Draw different building types with enhanced graphics
        switch (type) {
            case ROAD:
                drawRoad(g2d, x, y);
                break;
            case ROUNDABOUT:
                drawRoundabout(g2d, x, y);
                break;
            case PARK:
                drawPark(g2d, x, y);
                break;
            case POWER_PLANT:
                drawPowerPlant(g2d, x, y);
                break;
            case WATER_TOWER:
                drawWaterTower(g2d, x, y);
                break;
            case HOUSE:
                drawHouse(g2d, x, y);
                break;
            case APARTMENT:
                drawApartment(g2d, x, y);
                break;
            case FACTORY:
                drawFactory(g2d, x, y);
                break;
            case OFFICE:
                drawOfficeBuilding(g2d, x, y);
                break;
            case POLICE_STATION:
                drawPoliceStation(g2d, x, y);
                break;
            case FIRE_STATION:
                drawFireStation(g2d, x, y);
                break;
            case HOSPITAL:
                drawHospital(g2d, x, y);
                break;
            case SCHOOL:
            case LIBRARY:
                drawSchoolOrLibrary(g2d, x, y, type);
                break;
            case SHOP:
            case MALL:
                drawShopOrMall(g2d, x, y, type);
                break;
            case RESTAURANT:
            case FAST_FOOD:
                drawRestaurant(g2d, x, y, type);
                break;
            default:
                drawGenericBuilding(g2d, x, y, type);
                break;
        }
    }
    
    // Enhanced building drawing methods with 3D effects and details
    
    private void drawRoad(Graphics2D g2d, int x, int y) {
        int width = 32;
        int height = 16;
        
        // Draw road base with darker asphalt color
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(x - width / 2, y - height / 2, width, height);
        
        // Draw road markings (yellow dashed line)
        g2d.setColor(new Color(220, 200, 50));
        for (int i = 0; i < 3; i++) {
            g2d.fillRect(x - 6 + i * 6, y - 1, 4, 2);
        }
        
        // Draw road edges
        g2d.setColor(new Color(40, 40, 40));
        g2d.drawRect(x - width / 2, y - height / 2, width, height);
    }
    
    private void drawRoundabout(Graphics2D g2d, int x, int y) {
        // Draw circular road
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillOval(x - 20, y - 20, 40, 40);
        
        // Draw center island
        g2d.setColor(new Color(50, 150, 50));
        g2d.fillOval(x - 12, y - 12, 24, 24);
        
        // Draw edge
        g2d.setColor(new Color(40, 40, 40));
        g2d.drawOval(x - 20, y - 20, 40, 40);
        g2d.drawOval(x - 12, y - 12, 24, 24);
    }
    
    private void drawPark(Graphics2D g2d, int x, int y) {
        int width = 30;
        int height = 30;
        
        // Draw grass area
        g2d.setColor(new Color(60, 180, 80));
        g2d.fillRect(x - width / 2, y - height, width, height);
        
        // Draw trees
        g2d.setColor(new Color(40, 140, 40));
        g2d.fillOval(x - 12, y - height + 5, 8, 8);
        g2d.fillOval(x + 4, y - height + 5, 8, 8);
        g2d.fillOval(x - 4, y - height + 15, 8, 8);
        
        // Draw bench
        g2d.setColor(new Color(139, 90, 43));
        g2d.fillRect(x - 6, y - 10, 12, 4);
    }
    
    private void drawPowerPlant(Graphics2D g2d, int x, int y) {
        int width = 40;
        int height = 50;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw main building with gradient effect
        GradientPaint gradient = new GradientPaint(
            x - width / 2, y - height, new Color(255, 220, 100),
            x + width / 2, y, new Color(200, 160, 50)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x - width / 2, y - height, width, height);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw cooling towers
        g2d.setColor(new Color(180, 180, 180));
        g2d.fillArc(x - 20, y - height - 15, 12, 20, 0, 180);
        g2d.fillArc(x + 8, y - height - 15, 12, 20, 0, 180);
        
        // Draw steam
        g2d.setColor(new Color(240, 240, 240, 150));
        g2d.fillOval(x - 18, y - height - 25, 8, 8);
        g2d.fillOval(x + 10, y - height - 25, 8, 8);
        
        // Draw windows
        g2d.setColor(new Color(100, 150, 200));
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                g2d.fillRect(x - 12 + col * 8, y - height + 10 + row * 8, 4, 6);
            }
        }
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawWaterTower(Graphics2D g2d, int x, int y) {
        int baseWidth = 20;
        int height = 55;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - baseWidth / 2 + SHADOW_OFFSET_X - 1, y - 5, baseWidth, 8);
        
        // Draw support legs
        g2d.setColor(new Color(120, 120, 120));
        g2d.fillRect(x - 12, y - 30, 3, 30);
        g2d.fillRect(x + 9, y - 30, 3, 30);
        
        // Draw water tank with metallic gradient
        GradientPaint gradient = new GradientPaint(
            x - 15, y - height, new Color(150, 180, 255),
            x + 15, y - 30, new Color(80, 110, 180)
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x - 15, y - height, 30, 25);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw tank shine
        g2d.setColor(new Color(200, 220, 255, 150));
        g2d.fillOval(x - 10, y - height + 3, 12, 8);
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - 15, y - height, 30, 25);
        g2d.drawLine(x - 12, y - 30, x - 12, y);
        g2d.drawLine(x + 12, y - 30, x + 12, y);
    }
    
    private void drawHouse(Graphics2D g2d, int x, int y) {
        int width = 28;
        int height = 35;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X - 1, y - height / 2 + SHADOW_OFFSET_Y - 1, width, height);
        
        // Draw walls with gradient
        GradientPaint wallGradient = new GradientPaint(
            x - width / 2, y - height, new Color(220, 170, 120),
            x + width / 2, y, new Color(180, 130, 80)
        );
        g2d.setPaint(wallGradient);
        g2d.fillRect(x - width / 2, y - height, width, height - 5);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw roof
        int[] roofX = {x - width / 2 - 2, x, x + width / 2 + 2};
        int[] roofY = {y - height, y - height - 12, y - height};
        g2d.setColor(new Color(150, 50, 50));
        g2d.fillPolygon(roofX, roofY, 3);
        
        // Draw roof highlight
        g2d.setColor(new Color(180, 70, 70));
        g2d.drawLine(x - width / 2 - 2, y - height, x, y - height - 12);
        
        // Draw windows
        g2d.setColor(new Color(200, 230, 255));
        g2d.fillRect(x - 10, y - height + 8, 6, 6);
        g2d.fillRect(x + 4, y - height + 8, 6, 6);
        
        // Draw door
        g2d.setColor(new Color(100, 60, 20));
        g2d.fillRect(x - 4, y - height + 18, 8, 12);
        
        // Draw outlines
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height - 5);
        g2d.drawPolygon(roofX, roofY, 3);
    }
    
    private void drawApartment(Graphics2D g2d, int x, int y) {
        int width = 35;
        int height = 55;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building with gradient
        GradientPaint gradient = new GradientPaint(
            x - width / 2, y - height, new Color(200, 150, 120),
            x + width / 2, y, new Color(150, 100, 70)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x - width / 2, y - height, width, height);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw windows in a grid pattern
        g2d.setColor(new Color(200, 230, 255));
        for (int floor = 0; floor < 5; floor++) {
            for (int col = 0; col < 3; col++) {
                int wx = x - 12 + col * 10;
                int wy = y - height + 5 + floor * 10;
                g2d.fillRect(wx, wy, 6, 7);
                g2d.setColor(new Color(100, 120, 140));
                g2d.drawRect(wx, wy, 6, 7);
                g2d.drawLine(wx + 3, wy, wx + 3, wy + 7);
                g2d.setColor(new Color(200, 230, 255));
            }
        }
        
        // Draw balconies
        g2d.setColor(new Color(120, 90, 60));
        for (int floor = 1; floor < 5; floor++) {
            g2d.fillRect(x - width / 2 - 2, y - height + floor * 10 + 10, 4, 8);
        }
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawFactory(Graphics2D g2d, int x, int y) {
        int width = 40;
        int height = 45;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw main building
        g2d.setColor(new Color(120, 120, 120));
        g2d.fillRect(x - width / 2, y - height, width, height);
        
        // Draw darker side for 3D effect
        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(x - width / 2, y - height, width / 3, height);
        
        // Draw smokestacks
        g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(x - 15, y - height - 20, 6, 20);
        g2d.fillRect(x + 9, y - height - 20, 6, 20);
        
        // Draw smoke
        g2d.setColor(new Color(150, 150, 150, 120));
        g2d.fillOval(x - 17, y - height - 30, 10, 10);
        g2d.fillOval(x + 7, y - height - 30, 10, 10);
        g2d.fillOval(x - 15, y - height - 38, 8, 8);
        g2d.fillOval(x + 9, y - height - 38, 8, 8);
        
        // Draw loading bay
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(x - 8, y - 15, 16, 15);
        
        // Draw windows
        g2d.setColor(new Color(255, 200, 100, 180));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                g2d.fillRect(x + 4 + col * 8, y - height + 10 + row * 12, 5, 8);
            }
        }
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawOfficeBuilding(Graphics2D g2d, int x, int y) {
        int width = 35;
        int height = 60;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building with glass effect
        GradientPaint gradient = new GradientPaint(
            x - width / 2, y - height, new Color(180, 200, 230),
            x + width / 2, y, new Color(120, 140, 170)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x - width / 2, y - height, width, height);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw glass windows grid
        g2d.setColor(new Color(150, 200, 255, 180));
        for (int floor = 0; floor < 7; floor++) {
            for (int col = 0; col < 4; col++) {
                int wx = x - 14 + col * 7;
                int wy = y - height + 3 + floor * 8;
                g2d.fillRect(wx, wy, 5, 6);
            }
        }
        
        // Draw window frames
        g2d.setColor(new Color(80, 100, 120));
        for (int floor = 0; floor <= 7; floor++) {
            g2d.drawLine(x - width / 2, y - height + floor * 8, x + width / 2, y - height + floor * 8);
        }
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawPoliceStation(Graphics2D g2d, int x, int y) {
        int width = 38;
        int height = 42;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building
        g2d.setColor(new Color(70, 90, 180));
        g2d.fillRect(x - width / 2, y - height, width, height);
        
        // Draw roof light
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillOval(x - 6, y - height - 8, 12, 8);
        g2d.setColor(new Color(255, 100, 100, 150));
        g2d.fillOval(x - 8, y - height - 10, 16, 10);
        
        // Draw badge/star symbol
        g2d.setColor(new Color(255, 215, 0));
        int[] starX = new int[STAR_POINTS];
        int[] starY = new int[STAR_POINTS];
        for (int i = 0; i < STAR_POINTS; i++) {
            double angle = Math.PI * 2 * i / STAR_POINTS - Math.PI / 2;
            double radius = (i % 2 == 0) ? STAR_OUTER_RADIUS : STAR_INNER_RADIUS;
            starX[i] = x + (int)(Math.cos(angle) * radius);
            starY[i] = y - height / 2 + (int)(Math.sin(angle) * radius);
        }
        g2d.fillPolygon(starX, starY, STAR_POINTS);
        
        // Draw windows
        g2d.setColor(new Color(200, 220, 255));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                g2d.fillRect(x - 12 + j * 14, y - height + 18 + i * 8, 8, 6);
            }
        }
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawFireStation(Graphics2D g2d, int x, int y) {
        int width = 38;
        int height = 42;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building
        g2d.setColor(new Color(200, 60, 60));
        g2d.fillRect(x - width / 2, y - height, width, height);
        
        // Draw garage doors
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillRect(x - 14, y - 18, 12, 18);
        g2d.fillRect(x + 2, y - 18, 12, 18);
        
        // Draw door panels
        g2d.setColor(new Color(120, 120, 120));
        for (int i = 0; i < 4; i++) {
            g2d.drawLine(x - 14, y - 18 + i * 4, x - 2, y - 18 + i * 4);
            g2d.drawLine(x + 2, y - 18 + i * 4, x + 14, y - 18 + i * 4);
        }
        
        // Draw alarm bell
        g2d.setColor(new Color(180, 150, 50));
        g2d.fillArc(x - 6, y - height - 6, 12, 8, 0, 180);
        
        // Draw tower
        g2d.setColor(new Color(170, 50, 50));
        g2d.fillRect(x - 8, y - height - 18, 16, 12);
        
        // Draw windows on tower
        g2d.setColor(new Color(255, 255, 200));
        g2d.fillRect(x - 5, y - height - 14, 4, 4);
        g2d.fillRect(x + 1, y - height - 14, 4, 4);
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawHospital(Graphics2D g2d, int x, int y) {
        int width = 40;
        int height = 48;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building
        GradientPaint gradient = new GradientPaint(
            x - width / 2, y - height, new Color(255, 255, 255),
            x + width / 2, y, new Color(220, 220, 220)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x - width / 2, y - height, width, height);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw red cross
        g2d.setColor(new Color(220, 50, 50));
        g2d.fillRect(x - 10, y - height / 2 - 2, 20, 6);
        g2d.fillRect(x - 3, y - height / 2 - 9, 6, 20);
        
        // Draw windows
        g2d.setColor(new Color(180, 220, 255));
        for (int floor = 0; floor < 4; floor++) {
            for (int col = 0; col < 3; col++) {
                if (floor == 1 && col == 1) continue; // Skip center for cross
                g2d.fillRect(x - 15 + col * 10, y - height + 8 + floor * 10, 6, 7);
            }
        }
        
        // Draw entrance
        g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(x - 8, y - 12, 16, 12);
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawSchoolOrLibrary(Graphics2D g2d, int x, int y, BuildingType type) {
        int width = 38;
        int height = 40;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building
        Color mainColor = type == BuildingType.SCHOOL ? 
            new Color(220, 200, 100) : new Color(190, 160, 120);
        g2d.setColor(mainColor);
        g2d.fillRect(x - width / 2, y - height, width, height);
        
        // Draw entrance columns
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(x - 15, y - 20, 4, 20);
        g2d.fillRect(x + 11, y - 20, 4, 20);
        
        // Draw triangular pediment
        int[] pedX = {x - 20, x, x + 20};
        int[] pedY = {y - height, y - height - 10, y - height};
        g2d.fillPolygon(pedX, pedY, 3);
        
        // Draw windows
        g2d.setColor(new Color(150, 180, 220));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                g2d.fillRect(x - 12 + i * 8, y - height + 15 + j * 10, 6, 7);
            }
        }
        
        // Draw door
        g2d.setColor(new Color(100, 70, 40));
        g2d.fillRect(x - 6, y - 15, 12, 15);
        
        // Draw symbol (book or apple)
        if (type == BuildingType.LIBRARY) {
            g2d.setColor(new Color(100, 50, 50));
            g2d.fillRect(x - 4, y - height / 2 - 4, 8, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawLine(x - 2, y - height / 2 - 2, x - 2, y - height / 2 + 4);
        }
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
        g2d.drawPolygon(pedX, pedY, 3);
    }
    
    private void drawShopOrMall(Graphics2D g2d, int x, int y, BuildingType type) {
        int width = type == BuildingType.MALL ? 45 : 32;
        int height = type == BuildingType.MALL ? 38 : 32;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building
        GradientPaint gradient = new GradientPaint(
            x - width / 2, y - height, new Color(120, 220, 230),
            x + width / 2, y, new Color(80, 180, 190)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x - width / 2, y - height, width, height);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw awning
        g2d.setColor(new Color(200, 100, 100));
        g2d.fillRect(x - width / 2, y - height, width, 6);
        
        // Draw windows/display
        g2d.setColor(new Color(200, 230, 255, 200));
        if (type == BuildingType.MALL) {
            g2d.fillRect(x - width / 2 + 4, y - height + 10, width - 8, height - 20);
        } else {
            g2d.fillRect(x - width / 2 + 4, y - height + 10, width - 8, height - 18);
        }
        
        // Draw door
        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(x - 6, y - 12, 12, 12);
        
        // Draw $ sign for shop
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("$", x - 4, y - height + 22);
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawRestaurant(Graphics2D g2d, int x, int y, BuildingType type) {
        int width = 34;
        int height = 36;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building
        Color mainColor = type == BuildingType.FAST_FOOD ?
            new Color(255, 180, 80) : new Color(220, 140, 80);
        g2d.setColor(mainColor);
        g2d.fillRect(x - width / 2, y - height, width, height);
        
        // Draw roof/sign
        g2d.setColor(new Color(200, 50, 50));
        g2d.fillRect(x - width / 2 - 2, y - height - 8, width + 4, 8);
        
        // Draw text on sign
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 8));
        String text = type == BuildingType.FAST_FOOD ? "FF" : "REST";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, x - textWidth / 2, y - height - 2);
        
        // Draw windows
        g2d.setColor(new Color(255, 255, 200));
        g2d.fillRect(x - 12, y - height + 8, 8, 10);
        g2d.fillRect(x + 4, y - height + 8, 8, 10);
        
        // Draw door
        g2d.setColor(new Color(100, 60, 30));
        g2d.fillRect(x - 5, y - 14, 10, 14);
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private void drawGenericBuilding(Graphics2D g2d, int x, int y, BuildingType type) {
        int width = 32;
        int height = 38;
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(x - width / 2 + SHADOW_OFFSET_X, y - height + SHADOW_OFFSET_Y, width, height);
        
        // Draw building with gradient
        Color baseColor = getBuildingColor(type);
        GradientPaint gradient = new GradientPaint(
            x - width / 2, y - height, baseColor,
            x + width / 2, y, baseColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x - width / 2, y - height, width, height);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw windows
        g2d.setColor(new Color(200, 220, 255));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                g2d.fillRect(x - 10 + col * 12, y - height + 8 + row * 10, 6, 7);
            }
        }
        
        // Draw label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 9));
        String shortName = getShortName(type);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(shortName);
        g2d.drawString(shortName, x - textWidth / 2, y - height / 2);
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
    }
    
    private Color getBuildingColor(BuildingType type) {
        switch (type) {
            case HOUSE:
            case APARTMENT:
                return new Color(200, 150, 100);
            case POLICE_STATION:
                return new Color(50, 50, 200);
            case FIRE_STATION:
                return new Color(200, 50, 50);
            case SCHOOL:
            case LIBRARY:
                return new Color(200, 200, 50);
            case HOSPITAL:
                return new Color(200, 100, 100);
            case FACTORY:
                return new Color(100, 100, 100);
            case OFFICE:
                return new Color(150, 150, 200);
            case SHOP:
            case MALL:
                return new Color(100, 200, 200);
            case RESTAURANT:
                return new Color(220, 120, 60);
            case FAST_FOOD:
                return new Color(255, 165, 0);
            case PETROL_STATION:
                return new Color(180, 180, 0);
            case CINEMA:
                return new Color(160, 82, 160);
            case GYM:
                return new Color(150, 200, 100);
            case TOWN_HALL:
                return new Color(180, 140, 100);
            case POWER_PLANT:
                return new Color(255, 200, 0);
            case WATER_TOWER:
                return new Color(100, 150, 255);
            case ROAD:
                return new Color(80, 80, 80);
            case ROUNDABOUT:
                return new Color(100, 100, 100);
            case TRAFFIC_LIGHT:
                return new Color(50, 50, 50);
            case PARK:
                return new Color(50, 150, 50);
            case AIRPORT:
                return new Color(120, 120, 180);
            case RUNWAY:
                return new Color(90, 90, 90);
            case HANGAR:
                return new Color(100, 100, 150);
            case CONTROL_TOWER:
                return new Color(140, 140, 200);
            default:
                return Color.GRAY;
        }
    }
    
    private String getShortName(BuildingType type) {
        switch (type) {
            case HOUSE: return "H";
            case APARTMENT: return "APT";
            case POLICE_STATION: return "POL";
            case FIRE_STATION: return "FIRE";
            case SCHOOL: return "SCH";
            case HOSPITAL: return "HOSP";
            case FACTORY: return "FAC";
            case OFFICE: return "OFF";
            case SHOP: return "SHOP";
            case TOWN_HALL: return "HALL";
            case POWER_PLANT: return "PWR";
            case WATER_TOWER: return "H2O";
            case ROAD: return "═";
            case ROUNDABOUT: return "◯";
            case PARK: return "PARK";
            case RESTAURANT: return "REST";
            case FAST_FOOD: return "FF";
            case PETROL_STATION: return "GAS";
            case TRAFFIC_LIGHT: return "⚡";
            case MALL: return "MALL";
            case CINEMA: return "CIN";
            case GYM: return "GYM";
            case LIBRARY: return "LIB";
            case AIRPORT: return "AIR";
            case RUNWAY: return "RWY";
            case HANGAR: return "HGR";
            case CONTROL_TOWER: return "TWR";
            default: return "?";
        }
    }
    
    private void drawNaturalFeature(Graphics2D g2d, NaturalFeature feature, int x, int y) {
        switch (feature) {
            case TREE:
                // Draw tree trunk with gradient
                g2d.setColor(new Color(101, 67, 33));
                g2d.fillRect(x - 3, y - 10, 6, 12);
                
                // Draw tree foliage with multiple shades for depth
                g2d.setColor(new Color(34, 120, 34));
                g2d.fillOval(x - 10, y - 24, 20, 18);
                g2d.setColor(new Color(50, 160, 50));
                g2d.fillOval(x - 8, y - 22, 16, 14);
                g2d.setColor(new Color(70, 180, 70));
                g2d.fillOval(x - 6, y - 20, 12, 10);
                
                // Add highlight
                g2d.setColor(new Color(100, 220, 100, 150));
                g2d.fillOval(x - 4, y - 22, 6, 6);
                break;
                
            case ROCK:
                // Draw rock with 3D effect
                g2d.setColor(new Color(100, 100, 100));
                g2d.fillOval(x - 8, y - 8, 16, 16);
                g2d.setColor(new Color(140, 140, 140));
                g2d.fillOval(x - 7, y - 7, 10, 10);
                g2d.setColor(new Color(180, 180, 180));
                g2d.fillOval(x - 5, y - 6, 5, 5);
                
                // Add outline
                g2d.setColor(new Color(70, 70, 70));
                g2d.drawOval(x - 8, y - 8, 16, 16);
                break;
                
            case BUSH:
                // Draw bush with multiple layers
                g2d.setColor(new Color(40, 140, 40));
                g2d.fillOval(x - 8, y - 8, 16, 16);
                g2d.setColor(new Color(50, 160, 50));
                g2d.fillOval(x - 6, y - 6, 12, 12);
                g2d.setColor(new Color(60, 180, 60));
                g2d.fillOval(x - 4, y - 4, 8, 8);
                break;
        }
    }
    
    private void renderVehicle(Graphics2D g2d, Vehicle vehicle) {
        Camera camera = gameState.getCamera();
        CityMap map = gameState.getCityMap();
        
        // Apply rotation
        Point rotated = camera.applyRotation(vehicle.getX(), vehicle.getY(), map.getWidth(), map.getHeight());
        
        // Convert to screen coordinates
        Point screen = IsometricUtils.gridToScreen(rotated.x, rotated.y, 0);
        
        // Apply camera offset
        int screenX = screen.x + camera.getOffsetX();
        int screenY = screen.y + camera.getOffsetY();
        
        // Draw vehicle shadow
        g2d.setColor(new Color(0, 0, 0, SHADOW_ALPHA));
        g2d.fillRect(screenX - 7, screenY - 2, 16, 8);
        
        // Draw vehicle body with gradient
        Color vehicleColor = vehicle.getType().getColor();
        GradientPaint gradient = new GradientPaint(
            screenX - 8, screenY - 4, vehicleColor,
            screenX + 8, screenY + 4, vehicleColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(screenX - 8, screenY - 4, 16, 8);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw windows
        g2d.setColor(new Color(150, 180, 220, 180));
        g2d.fillRect(screenX - 4, screenY - 2, 8, 4);
        
        // Draw outline
        g2d.setColor(Color.BLACK);
        g2d.drawRect(screenX - 8, screenY - 4, 16, 8);
    }
    
    private void drawTrafficArrow(Graphics2D g2d, TrafficDirection direction, int x, int y) {
        // Draw traffic direction arrow on the road
        g2d.setColor(new Color(255, 255, 0, 200)); // Semi-transparent yellow
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        
        String arrow = direction.getArrow();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(arrow);
        int textHeight = fm.getAscent();
        
        g2d.drawString(arrow, x - textWidth / 2, y + textHeight / 4);
    }
    
    private void renderTrafficLight(Graphics2D g2d, TrafficLight light) {
        Camera camera = gameState.getCamera();
        CityMap map = gameState.getCityMap();
        
        // Apply rotation
        Point rotated = camera.applyRotation(light.getX(), light.getY(), map.getWidth(), map.getHeight());
        
        // Convert to screen coordinates
        Point screen = IsometricUtils.gridToScreen(rotated.x, rotated.y, 0);
        
        // Apply camera offset
        int screenX = screen.x + camera.getOffsetX();
        int screenY = screen.y + camera.getOffsetY() - 20;
        
        // Draw traffic light pole with gradient
        GradientPaint poleGradient = new GradientPaint(
            screenX - 2, screenY, new Color(80, 80, 80),
            screenX + 2, screenY + 15, new Color(50, 50, 50)
        );
        g2d.setPaint(poleGradient);
        g2d.fillRect(screenX - 2, screenY, 4, 15);
        g2d.setPaint(null); // Reset paint after gradient
        
        // Draw traffic light box with metallic look
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRoundRect(screenX - 6, screenY - 18, 12, 18, 3, 3);
        
        // Draw all three light positions (dimmed)
        g2d.setColor(new Color(80, 20, 20));
        g2d.fillOval(screenX - 4, screenY - 16, 8, 5);
        g2d.setColor(new Color(80, 80, 20));
        g2d.fillOval(screenX - 4, screenY - 10, 8, 5);
        g2d.setColor(new Color(20, 80, 20));
        g2d.fillOval(screenX - 4, screenY - 4, 8, 5);
        
        // Draw active light with glow based on traffic light state
        TrafficLightState state = light.getState();
        Color lightColor = state.getColor();
        int lightY = screenY - 16; // Default to red
        
        // Determine position based on state instead of color thresholds
        switch (state) {
            case GREEN:
                lightY = screenY - 4;
                break;
            case YELLOW:
                lightY = screenY - 10;
                break;
            case RED:
            default:
                lightY = screenY - 16;
                break;
        }
        
        // Draw glow effect
        g2d.setColor(new Color(lightColor.getRed(), lightColor.getGreen(), lightColor.getBlue(), 100));
        g2d.fillOval(screenX - 6, lightY - 2, 12, 9);
        
        // Draw bright light
        g2d.setColor(lightColor);
        g2d.fillOval(screenX - 4, lightY, 8, 5);
        
        // Add shine
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillOval(screenX - 2, lightY + 1, 3, 2);
    }
    
    public void setToolMode(ToolMode mode) {
        this.toolMode = mode;
    }
    
    public void setSelectedBuilding(BuildingType building) {
        this.selectedBuilding = building;
        this.toolMode = ToolMode.BUILD;
    }
}
