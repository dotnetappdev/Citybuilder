package com.citybuilder.model;

/**
 * Types of buildings that can be constructed.
 */
public enum BuildingType {
    HOUSE("House", 500, 10),
    APARTMENT("Apartment", 1200, 25),
    POLICE_STATION("Police Station", 2000, 0),
    FIRE_STATION("Fire Station", 1800, 0),
    SCHOOL("School", 2500, 0),
    HOSPITAL("Hospital", 3000, 0),
    FACTORY("Factory", 3500, 0),
    OFFICE("Office Building", 2800, 0),
    SHOP("Shop", 800, 0),
    TOWN_HALL("Town Hall", 5000, 0),
    POWER_PLANT("Power Plant", 4000, 0),
    WATER_TOWER("Water Tower", 2500, 0),
    ROAD("Road", 50, 0),
    ROUNDABOUT("Roundabout", 200, 0),
    PARK("Park", 300, 0),
    RESTAURANT("Restaurant", 1500, 0),
    FAST_FOOD("Fast Food", 900, 0),
    PETROL_STATION("Petrol Station", 2000, 0),
    TRAFFIC_LIGHT("Traffic Light", 150, 0),
    MALL("Shopping Mall", 4000, 0),
    CINEMA("Cinema", 2200, 0),
    GYM("Gym", 1800, 0),
    LIBRARY("Library", 2000, 0);
    
    private final String name;
    private final int cost;
    private final int residents; // Number of residents this building can house
    
    BuildingType(String name, int cost, int residents) {
        this.name = name;
        this.cost = cost;
        this.residents = residents;
    }
    
    public String getName() {
        return name;
    }
    
    public int getCost() {
        return cost;
    }
    
    public int getResidents() {
        return residents;
    }
    
    public int getMonthlyIncome() {
        // Different building types generate different income
        switch (this) {
            case RESTAURANT:
            case FAST_FOOD:
                return 200;
            case SHOP:
            case MALL:
                return 150;
            case PETROL_STATION:
                return 180;
            case OFFICE:
                return 100;
            case FACTORY:
                return 120;
            case HOUSE:
                return 50; // Tax
            case APARTMENT:
                return 100; // Tax
            default:
                return 0;
        }
    }
    
    public int getMonthlyCost() {
        // Service buildings have maintenance costs
        switch (this) {
            case POLICE_STATION:
            case FIRE_STATION:
                return 100;
            case SCHOOL:
            case HOSPITAL:
            case LIBRARY:
                return 80;
            case TOWN_HALL:
                return 150;
            case POWER_PLANT:
                return 200;
            case WATER_TOWER:
                return 100;
            case ROAD:
                return 5;
            case TRAFFIC_LIGHT:
                return 10;
            default:
                return 0;
        }
    }
}
