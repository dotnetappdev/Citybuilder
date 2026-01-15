package com.citybuilder.model;

/**
 * Currency types for the game.
 */
public enum Currency {
    USD("$", "US Dollar"),
    EUR("€", "Euro"),
    GBP("£", "British Pound"),
    JPY("¥", "Japanese Yen"),
    CAD("C$", "Canadian Dollar"),
    AUD("A$", "Australian Dollar");
    
    private final String symbol;
    private final String name;
    
    Currency(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public String format(int amount) {
        return symbol + String.format("%,d", amount);
    }
}
