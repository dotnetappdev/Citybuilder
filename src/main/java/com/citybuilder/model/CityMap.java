package com.citybuilder.model;

/**
 * Represents the city map with terrain and tiles.
 */
public class CityMap {
    private int width;
    private int height;
    private Tile[][] tiles;
    
    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
        
        initializeTiles();
    }
    
    private void initializeTiles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(x, y);
                
                // Add some variety to terrain height
                double noise = Math.sin(x * 0.1) * Math.cos(y * 0.1) * 2;
                tiles[x][y].setHeight((int) noise);
                
                // Add some rivers (vertical)
                if (x == 15 || x == 35) {
                    tiles[x][y].setTerrainType(TerrainType.WATER);
                }
                
                // Add some trees
                if (Math.random() < 0.1 && tiles[x][y].getTerrainType() != TerrainType.WATER) {
                    tiles[x][y].setNaturalFeature(NaturalFeature.TREE);
                }
            }
        }
        
        // Create a valley (lower terrain)
        for (int x = 20; x < 30; x++) {
            for (int y = 20; y < 30; y++) {
                tiles[x][y].setHeight(-2);
            }
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[x][y];
        }
        return null;
    }
    
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
