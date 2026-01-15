package com.citybuilder.system;

import com.citybuilder.model.*;

import java.util.*;

/**
 * System for managing electricity distribution.
 */
public class ElectricitySystem {
    
    public static void updateElectricity(CityMap map) {
        // Reset all electricity status
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                tile.setHasElectricity(false);
            }
        }
        
        // Find all power plants
        List<Tile> powerPlants = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                Building building = tile.getBuilding();
                if (building != null && building.getType() == BuildingType.POWER_PLANT) {
                    powerPlants.add(tile);
                }
            }
        }
        
        // Spread electricity from power plants using BFS
        for (Tile powerPlant : powerPlants) {
            spreadElectricity(map, powerPlant, 20); // Range of 20 tiles
        }
    }
    
    private static void spreadElectricity(CityMap map, Tile source, int maxRange) {
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();
        Map<Tile, Integer> distances = new HashMap<>();
        
        queue.add(source);
        visited.add(source);
        distances.put(source, 0);
        source.setHasElectricity(true);
        
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
                
                if (map.isValidPosition(nx, ny)) {
                    Tile neighbor = map.getTile(nx, ny);
                    if (neighbor != null && !visited.contains(neighbor)) {
                        visited.add(neighbor);
                        neighbor.setHasElectricity(true);
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
}
