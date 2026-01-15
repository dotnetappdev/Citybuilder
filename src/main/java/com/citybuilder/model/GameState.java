package com.citybuilder.model;

import com.citybuilder.util.IsometricUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game state that holds all game data.
 */
public class GameState {
    private static final int MAP_WIDTH = 50;
    private static final int MAP_HEIGHT = 50;
    
    private CityMap cityMap;
    private int money;
    private int population;
    private transient Camera camera;
    
    public GameState() {
        this.camera = new Camera();
    }
    
    public void initializeNewGame() {
        this.cityMap = new CityMap(MAP_WIDTH, MAP_HEIGHT);
        this.money = 50000;
        this.population = 0;
        this.camera = new Camera();
    }
    
    public CityMap getCityMap() {
        return cityMap;
    }
    
    public int getMoney() {
        return money;
    }
    
    public void setMoney(int money) {
        this.money = money;
    }
    
    public void addMoney(int amount) {
        this.money += amount;
    }
    
    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }
    
    public int getPopulation() {
        return population;
    }
    
    public void setPopulation(int population) {
        this.population = population;
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public boolean saveToFile(String filename) {
        try {
            File saveDir = new File("saves");
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            
            File saveFile = new File(saveDir, filename + ".json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);
            
            try (FileWriter writer = new FileWriter(saveFile)) {
                writer.write(json);
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static GameState loadFromFile(String filename) {
        try {
            File saveFile = new File("saves", filename + ".json");
            if (!saveFile.exists()) {
                return null;
            }
            
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(saveFile)) {
                GameState state = gson.fromJson(reader, GameState.class);
                state.camera = new Camera();
                return state;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<String> listSavedGames() {
        List<String> saves = new ArrayList<>();
        File saveDir = new File("saves");
        
        if (saveDir.exists() && saveDir.isDirectory()) {
            File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    saves.add(name.substring(0, name.length() - 5));
                }
            }
        }
        
        return saves;
    }
}
