package com.citybuilder.model;

/**
 * Types of buildings that can be constructed.
 */
public enum BuildingType {
    HOUSE("House", 500),
    APARTMENT("Apartment", 1200),
    POLICE_STATION("Police Station", 2000),
    FIRE_STATION("Fire Station", 1800),
    SCHOOL("School", 2500),
    HOSPITAL("Hospital", 3000),
    FACTORY("Factory", 3500),
    OFFICE("Office Building", 2800),
    SHOP("Shop", 800),
    TOWN_HALL("Town Hall", 5000),
    POWER_PLANT("Power Plant", 4000),
    WATER_TOWER("Water Tower", 2500),
    ROAD("Road", 50),
    ROUNDABOUT("Roundabout", 200),
    PARK("Park", 300);
    
    private final String name;
    private final int cost;
    
    BuildingType(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }
    
    public String getName() {
        return name;
    }
    
    public int getCost() {
        return cost;
    }
}
