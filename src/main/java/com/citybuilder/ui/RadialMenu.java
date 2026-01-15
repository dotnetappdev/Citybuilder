package com.citybuilder.ui;

import com.citybuilder.model.BuildingType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

/**
 * RDR2-style radial menu for quick access to tools and buildings.
 */
public class RadialMenu extends JPanel {
    private static final int MENU_RADIUS = 150;
    private static final int ICON_RADIUS = 100;
    private static final int RESIDENT_SPAWN_DIVISOR = 10; // How many building capacity units per resident
    private boolean visible;
    private Point centerPoint;
    private List<RadialMenuItem> menuItems;
    private int hoveredIndex = -1;
    private RadialMenuListener listener;
    
    public RadialMenu() {
        setOpaque(false);
        visible = false;
        menuItems = new ArrayList<>();
        initializeMenuItems();
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (visible && centerPoint != null) {
                    updateHoveredItem(e.getPoint());
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (visible && hoveredIndex >= 0 && hoveredIndex < menuItems.size()) {
                    RadialMenuItem item = menuItems.get(hoveredIndex);
                    if (listener != null) {
                        listener.onMenuItemSelected(item);
                    }
                    hide();
                }
            }
        });
    }
    
    private void initializeMenuItems() {
        // Quick access items
        menuItems.add(new RadialMenuItem("Build", "🔨", ToolMode.BUILD));
        menuItems.add(new RadialMenuItem("Demolish", "✖", ToolMode.DEMOLISH));
        menuItems.add(new RadialMenuItem("Road", "═", BuildingType.ROAD));
        menuItems.add(new RadialMenuItem("House", "🏠", BuildingType.HOUSE));
        menuItems.add(new RadialMenuItem("Restaurant", "🍽", BuildingType.RESTAURANT));
        menuItems.add(new RadialMenuItem("Shop", "🛒", BuildingType.SHOP));
        menuItems.add(new RadialMenuItem("Traffic", "🚦", ToolMode.SET_TRAFFIC_DIRECTION));
        menuItems.add(new RadialMenuItem("Terrain", "⛰", ToolMode.RAISE_TERRAIN));
    }
    
    public void show(Point location) {
        this.centerPoint = location;
        this.visible = true;
        this.hoveredIndex = -1;
        repaint();
    }
    
    public void hide() {
        this.visible = false;
        this.hoveredIndex = -1;
        repaint();
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    private void updateHoveredItem(Point mousePoint) {
        if (centerPoint == null) return;
        
        int dx = mousePoint.x - centerPoint.x;
        int dy = mousePoint.y - centerPoint.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < 30 || distance > MENU_RADIUS + 20) {
            hoveredIndex = -1;
            return;
        }
        
        double angle = Math.atan2(dy, dx);
        if (angle < 0) angle += 2 * Math.PI;
        
        double segmentAngle = (2 * Math.PI) / menuItems.size();
        int index = (int) ((angle + segmentAngle / 2) / segmentAngle) % menuItems.size();
        hoveredIndex = index;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!visible || centerPoint == null) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cx = centerPoint.x;
        int cy = centerPoint.y;
        
        // Draw semi-transparent background circle
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillOval(cx - MENU_RADIUS, cy - MENU_RADIUS, MENU_RADIUS * 2, MENU_RADIUS * 2);
        
        // Draw menu segments
        double segmentAngle = 360.0 / menuItems.size();
        
        for (int i = 0; i < menuItems.size(); i++) {
            double startAngle = i * segmentAngle - 90;
            
            // Highlight hovered item
            if (i == hoveredIndex) {
                g2d.setColor(new Color(100, 150, 255, 200));
            } else {
                g2d.setColor(new Color(50, 50, 50, 180));
            }
            
            Arc2D.Double arc = new Arc2D.Double(
                cx - MENU_RADIUS,
                cy - MENU_RADIUS,
                MENU_RADIUS * 2,
                MENU_RADIUS * 2,
                startAngle,
                segmentAngle,
                Arc2D.PIE
            );
            g2d.fill(arc);
            
            // Draw segment border
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.draw(arc);
            
            // Draw icon/text
            RadialMenuItem item = menuItems.get(i);
            double iconAngle = Math.toRadians(startAngle + segmentAngle / 2);
            int iconX = cx + (int) (ICON_RADIUS * Math.cos(iconAngle));
            int iconY = cy + (int) (ICON_RADIUS * Math.sin(iconAngle));
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(item.label);
            g2d.drawString(item.label, iconX - textWidth / 2, iconY + fm.getAscent() / 2);
            
            // Draw icon
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            fm = g2d.getFontMetrics();
            textWidth = fm.stringWidth(item.icon);
            g2d.drawString(item.icon, iconX - textWidth / 2, iconY - 10);
        }
        
        // Draw center circle
        g2d.setColor(new Color(30, 30, 30, 200));
        g2d.fillOval(cx - 30, cy - 30, 60, 60);
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.drawOval(cx - 30, cy - 30, 60, 60);
    }
    
    public void setListener(RadialMenuListener listener) {
        this.listener = listener;
    }
    
    public interface RadialMenuListener {
        void onMenuItemSelected(RadialMenuItem item);
    }
    
    public static class RadialMenuItem {
        String label;
        String icon;
        Object data; // Can be ToolMode or BuildingType
        
        public RadialMenuItem(String label, String icon, Object data) {
            this.label = label;
            this.icon = icon;
            this.data = data;
        }
        
        public Object getData() {
            return data;
        }
    }
}
