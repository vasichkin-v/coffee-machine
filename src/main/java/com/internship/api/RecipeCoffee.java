package com.internship.api;

public class RecipeCoffee {
    private final String title;
    private final Double coffee;
    private final Double water;
    private final Double milk;
    private final Integer minCookVolume;

    public RecipeCoffee(String title, Double coffee, Double water, Double milk, Integer minCookVolume) {
        this.title = title;
        this.coffee = coffee;
        this.water = water;
        this.milk = milk;
        this.minCookVolume = minCookVolume;
    }

    public String getTitle() {
        return title;
    }

    public Double getCoffee() {
        return coffee;
    }

    public Double getWater() {
        return water;
    }

    public Double getMilk() {
        return milk;
    }

    public Integer getMinCookVolume() {
        return minCookVolume;
    }
}
