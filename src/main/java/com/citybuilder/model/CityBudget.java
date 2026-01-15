package com.citybuilder.model;

/**
 * City budget tracking SimCity-style.
 */
public class CityBudget {
    private int balance;
    private int monthlyIncome;
    private int monthlyExpenses;
    
    // Income sources
    private int residentialTax;
    private int commercialTax;
    private int industrialTax;
    
    // Expense categories
    private int transportationCost;
    private int healthcareCost;
    private int educationCost;
    private int publicsafetyCost;
    private int utilitiesCost;
    
    public CityBudget(int startingBalance) {
        this.balance = startingBalance;
    }
    
    public void calculateMonthly() {
        monthlyIncome = residentialTax + commercialTax + industrialTax;
        monthlyExpenses = transportationCost + healthcareCost + educationCost + 
                         publicsafetyCost + utilitiesCost;
    }
    
    public int getBalance() {
        return balance;
    }
    
    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    public void addIncome(int amount) {
        balance += amount;
    }
    
    public boolean spendMoney(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    public int getMonthlyIncome() {
        return monthlyIncome;
    }
    
    public int getMonthlyExpenses() {
        return monthlyExpenses;
    }
    
    public int getNetIncome() {
        return monthlyIncome - monthlyExpenses;
    }
    
    // Setters for income sources
    public void setResidentialTax(int amount) { this.residentialTax = amount; }
    public void setCommercialTax(int amount) { this.commercialTax = amount; }
    public void setIndustrialTax(int amount) { this.industrialTax = amount; }
    
    // Setters for expenses
    public void setTransportationCost(int amount) { this.transportationCost = amount; }
    public void setHealthcareCost(int amount) { this.healthcareCost = amount; }
    public void setEducationCost(int amount) { this.educationCost = amount; }
    public void setPublicsafetyCost(int amount) { this.publicsafetyCost = amount; }
    public void setUtilitiesCost(int amount) { this.utilitiesCost = amount; }
    
    // Getters for detailed breakdown
    public int getResidentialTax() { return residentialTax; }
    public int getCommercialTax() { return commercialTax; }
    public int getIndustrialTax() { return industrialTax; }
    public int getTransportationCost() { return transportationCost; }
    public int getHealthcareCost() { return healthcareCost; }
    public int getEducationCost() { return educationCost; }
    public int getPublicsafetyCost() { return publicsafetyCost; }
    public int getUtilitiesCost() { return utilitiesCost; }
}
