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
    private int monthlyIncome;
    private int monthlyExpenses;
    private int gameMonth;
    private double cityHappiness;
    private List<Resident> residents;
    private List<Vehicle> vehicles;
    private List<TrafficLight> trafficLights;
    private transient Camera camera;
    
    // New systems
    private Currency currency;
    private GameDate gameDate;
    private CityBudget cityBudget;
    private TimeOfDay currentTimeOfDay;
    
    public GameState() {
        this.camera = new Camera();
        this.residents = new ArrayList<>();
        this.vehicles = new ArrayList<>();
        this.trafficLights = new ArrayList<>();
        this.currency = Currency.USD;
        this.gameDate = new GameDate(2000);
        this.cityBudget = new CityBudget(50000);
        this.currentTimeOfDay = TimeOfDay.MORNING;
    }
    
    public void initializeNewGame() {
        this.cityMap = new CityMap(MAP_WIDTH, MAP_HEIGHT);
        this.money = 50000;
        this.population = 0;
        this.camera = new Camera();
        this.residents = new ArrayList<>();
        this.vehicles = new ArrayList<>();
        this.trafficLights = new ArrayList<>();
        this.gameMonth = 0;
        this.monthlyIncome = 0;
        this.monthlyExpenses = 0;
        this.cityHappiness = 75.0;
        this.currency = Currency.USD;
        this.gameDate = new GameDate(2000);
        this.cityBudget = new CityBudget(50000);
        this.currentTimeOfDay = TimeOfDay.MORNING;
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
    
    public List<Resident> getResidents() {
        return residents;
    }
    
    public List<Vehicle> getVehicles() {
        return vehicles;
    }
    
    public List<TrafficLight> getTrafficLights() {
        return trafficLights;
    }
    
    public void addResident(Resident resident) {
        residents.add(resident);
        population++;
    }
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
    
    public void addTrafficLight(TrafficLight light) {
        trafficLights.add(light);
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    
    public GameDate getGameDate() {
        return gameDate;
    }
    
    public CityBudget getCityBudget() {
        return cityBudget;
    }
    
    public TimeOfDay getCurrentTimeOfDay() {
        return currentTimeOfDay;
    }
    
    public void advanceTime() {
        gameDate.advanceHour();
        TimeOfDay newTime = gameDate.getTimeOfDay();
        
        // Update time of day
        if (newTime != currentTimeOfDay) {
            currentTimeOfDay = newTime;
            updateForTimeOfDay();
        }
    }
    
    private void updateForTimeOfDay() {
        // Update all residents for time of day
        for (Resident resident : residents) {
            resident.updateForTimeOfDay(currentTimeOfDay);
        }
        
        // Fewer vehicles spawn at night
        if (currentTimeOfDay.isNight()) {
            // Remove some vehicles to simulate less traffic at night
            int toRemove = vehicles.size() / 3;
            for (int i = 0; i < toRemove && !vehicles.isEmpty(); i++) {
                vehicles.remove(0);
            }
        }
    }
    
    public int getMonthlyIncome() {
        return monthlyIncome;
    }
    
    public int getMonthlyExpenses() {
        return monthlyExpenses;
    }
    
    public int getGameMonth() {
        return gameMonth;
    }
    
    public double getCityHappiness() {
        return cityHappiness;
    }
    
    public void updateMonthly() {
        gameMonth++;
        
        // Calculate income and expenses by category
        int resTax = 0, comTax = 0, indTax = 0;
        int transCost = 0, healthCost = 0, eduCost = 0, safetyCost = 0, utilCost = 0;
        
        for (int x = 0; x < cityMap.getWidth(); x++) {
            for (int y = 0; y < cityMap.getHeight(); y++) {
                Tile tile = cityMap.getTile(x, y);
                Building building = tile.getBuilding();
                if (building != null) {
                    int income = building.getType().getMonthlyIncome();
                    int cost = building.getType().getMonthlyCost();
                    
                    monthlyIncome += income;
                    monthlyExpenses += cost;
                    
                    // Categorize by building type
                    if (building.getType() == BuildingType.HOUSE || 
                        building.getType() == BuildingType.APARTMENT) {
                        resTax += income;
                    } else if (building.getType() == BuildingType.SHOP ||
                              building.getType() == BuildingType.MALL ||
                              building.getType() == BuildingType.RESTAURANT ||
                              building.getType() == BuildingType.FAST_FOOD) {
                        comTax += income;
                    } else if (building.getType() == BuildingType.FACTORY ||
                              building.getType() == BuildingType.OFFICE) {
                        indTax += income;
                    }
                    
                    // Categorize expenses
                    if (building.getType() == BuildingType.ROAD ||
                        building.getType() == BuildingType.ROUNDABOUT ||
                        building.getType() == BuildingType.TRAFFIC_LIGHT) {
                        transCost += cost;
                    } else if (building.getType() == BuildingType.HOSPITAL) {
                        healthCost += cost;
                    } else if (building.getType() == BuildingType.SCHOOL ||
                              building.getType() == BuildingType.LIBRARY) {
                        eduCost += cost;
                    } else if (building.getType() == BuildingType.POLICE_STATION ||
                              building.getType() == BuildingType.FIRE_STATION) {
                        safetyCost += cost;
                    } else if (building.getType() == BuildingType.POWER_PLANT ||
                              building.getType() == BuildingType.WATER_TOWER) {
                        utilCost += cost;
                    }
                }
            }
        }
        
        // Update city budget
        cityBudget.setResidentialTax(resTax);
        cityBudget.setCommercialTax(comTax);
        cityBudget.setIndustrialTax(indTax);
        cityBudget.setTransportationCost(transCost);
        cityBudget.setHealthcareCost(healthCost);
        cityBudget.setEducationCost(eduCost);
        cityBudget.setPublicsafetyCost(safetyCost);
        cityBudget.setUtilitiesCost(utilCost);
        cityBudget.calculateMonthly();
        
        int netIncome = monthlyIncome - monthlyExpenses;
        addMoney(netIncome);
        cityBudget.addIncome(netIncome);
        
        // Update city happiness
        updateCityHappiness();
        
        // Update residents - age them
        int currentYear = gameDate.getYear();
        for (Resident resident : residents) {
            resident.updateAge(currentYear);
            resident.updateMood();
        }
        
        // Advance date by one month
        gameDate.advanceMonth();
    }
    
    private void updateCityHappiness() {
        if (residents.isEmpty()) {
            cityHappiness = 75.0;
            return;
        }
        
        double totalHappiness = 0;
        for (Resident resident : residents) {
            totalHappiness += resident.getHappiness();
        }
        cityHappiness = totalHappiness / residents.size();
    }
    
    public void updateTraffic() {
        // Update traffic lights
        for (TrafficLight light : trafficLights) {
            light.update();
        }
        
        // Update vehicles
        for (Vehicle vehicle : vehicles) {
            // Check if vehicle is at a traffic light
            boolean atTrafficLight = false;
            for (TrafficLight light : trafficLights) {
                if (Math.abs(vehicle.getX() - light.getX()) <= 1 && 
                    Math.abs(vehicle.getY() - light.getY()) <= 1) {
                    if (light.shouldStop()) {
                        vehicle.stop();
                        atTrafficLight = true;
                    } else {
                        vehicle.resume();
                    }
                    break;
                }
            }
            
            if (!atTrafficLight && vehicle.isStopped()) {
                vehicle.resume();
            }
            
            vehicle.move();
        }
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
