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
    private GameState gameState;
    private Point dragStart;
    private BuildingType selectedBuilding;
    private ToolMode toolMode;
    
    public GamePanel(GameState gameState) {
        this.gameState = gameState;
        this.toolMode = ToolMode.NONE;
        
        setBackground(new Color(100, 150, 100));
        setFocusable(true);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e) || 
                    (SwingUtilities.isLeftMouseButton(e) && e.isControlDown())) {
                    dragStart = e.getPoint();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    handleClick(e.getX(), e.getY());
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
        } else if (tile.getNaturalFeature() != null) {
            drawNaturalFeature(g2d, tile.getNaturalFeature(), screenX, screenY);
        }
    }
    
    private Color getTileColor(Tile tile) {
        switch (tile.getTerrainType()) {
            case WATER:
                return new Color(50, 100, 200);
            case DIRT:
                return new Color(139, 90, 43);
            case SAND:
                return new Color(238, 214, 175);
            case GRASS:
            default:
                int heightShade = Math.max(0, Math.min(255, 120 + tile.getHeight() * 10));
                return new Color(0, heightShade, 0);
        }
    }
    
    private Color getZoneColor(ZoneType zoneType) {
        switch (zoneType) {
            case RESIDENTIAL:
                return Color.GREEN;
            case COMMERCIAL:
                return Color.BLUE;
            case INDUSTRIAL:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }
    
    private void drawBuilding(Graphics2D g2d, Building building, int x, int y) {
        Color buildingColor = getBuildingColor(building.getType());
        g2d.setColor(buildingColor);
        
        int width = 30;
        int height = 40;
        
        // Draw simple building representation
        g2d.fillRect(x - width / 2, y - height, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - width / 2, y - height, width, height);
        
        // Draw building name
        g2d.setFont(new Font("Arial", Font.PLAIN, 8));
        String shortName = getShortName(building.getType());
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(shortName);
        g2d.drawString(shortName, x - textWidth / 2, y - height / 2);
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
                return new Color(200, 200, 50);
            case HOSPITAL:
                return new Color(200, 100, 100);
            case FACTORY:
                return new Color(100, 100, 100);
            case OFFICE:
                return new Color(150, 150, 200);
            case SHOP:
                return new Color(100, 200, 200);
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
            case PARK:
                return new Color(50, 150, 50);
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
            default: return "?";
        }
    }
    
    private void drawNaturalFeature(Graphics2D g2d, NaturalFeature feature, int x, int y) {
        switch (feature) {
            case TREE:
                g2d.setColor(new Color(34, 139, 34));
                g2d.fillOval(x - 8, y - 20, 16, 16);
                g2d.setColor(new Color(101, 67, 33));
                g2d.fillRect(x - 3, y - 10, 6, 10);
                break;
            case ROCK:
                g2d.setColor(Color.GRAY);
                g2d.fillOval(x - 6, y - 6, 12, 12);
                break;
            case BUSH:
                g2d.setColor(new Color(0, 128, 0));
                g2d.fillOval(x - 6, y - 6, 12, 12);
                break;
        }
    }
    
    public void setToolMode(ToolMode mode) {
        this.toolMode = mode;
    }
    
    public void setSelectedBuilding(BuildingType building) {
        this.selectedBuilding = building;
        this.toolMode = ToolMode.BUILD;
    }
}
