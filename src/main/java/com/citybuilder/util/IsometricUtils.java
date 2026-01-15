package com.citybuilder.util;

import java.awt.Point;

/**
 * Utility class for isometric coordinate conversions.
 */
public class IsometricUtils {
    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 32;
    
    /**
     * Convert grid coordinates to screen coordinates (isometric).
     */
    public static Point gridToScreen(int gridX, int gridY, int height) {
        int screenX = (gridX - gridY) * (TILE_WIDTH / 2);
        int screenY = (gridX + gridY) * (TILE_HEIGHT / 2) - (height * 8);
        return new Point(screenX, screenY);
    }
    
    /**
     * Convert screen coordinates to grid coordinates (isometric).
     */
    public static Point screenToGrid(int screenX, int screenY) {
        int gridX = (screenX / (TILE_WIDTH / 2) + screenY / (TILE_HEIGHT / 2)) / 2;
        int gridY = (screenY / (TILE_HEIGHT / 2) - screenX / (TILE_WIDTH / 2)) / 2;
        return new Point(gridX, gridY);
    }
    
    public static int getTileWidth() {
        return TILE_WIDTH;
    }
    
    public static int getTileHeight() {
        return TILE_HEIGHT;
    }
}
