package com.citybuilder.system;

import com.citybuilder.model.*;

import java.util.*;

/**
 * System for managing water distribution.
 */
public class WaterSystem {
    
    public static void updateWater(CityMap map) {
        // Reset all water status
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                tile.setHasWater(false);
            }
        }
        
        // Find all water sources (water towers and water tiles)
        List<Tile> waterSources = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                Building building = tile.getBuilding();
                
                if (building != null && building.getType() == BuildingType.WATER_TOWER) {
                    waterSources.add(tile);
                } else if (tile.getTerrainType() == TerrainType.WATER) {
                    waterSources.add(tile);
                }
            }
        }
        
        // Spread water from sources using BFS
        for (Tile waterSource : waterSources) {
            spreadWater(map, waterSource, 15); // Range of 15 tiles
        }
    }
    
    private static void spreadWater(CityMap map, Tile source, int maxRange) {
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();
        Map<Tile, Integer> distances = new HashMap<>();
        
        queue.add(source);
        visited.add(source);
        distances.put(source, 0);
        source.setHasWater(true);
        
        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            int currentDist = distances.get(current);
            
            if (currentDist >= maxRange) {
                continue;
            }
            
            // Check all adjacent tiles
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : directions) {
                int nx = current.getX() + dir[0];
                int ny = current.getY() + dir[1];
                
                Tile neighbor = map.getTile(nx, ny);
                if (neighbor != null && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    neighbor.setHasWater(true);
                    distances.put(neighbor, currentDist + 1);
                    
                    // Continue spreading if there's a road or building
                    if (neighbor.getBuilding() != null) {
                        queue.add(neighbor);
                    }
                }
            }
        }
    }
}
