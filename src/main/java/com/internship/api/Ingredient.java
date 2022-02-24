package com.internship.api;

import java.util.Scanner;

public class Ingredient {
    private String name;
    private Integer volume;

    public Ingredient(String name, Integer volume) {
        this.name = name;
        this.volume = volume;
    }

    public void updateContainer(int val) {
        // System.out.printf("Specify in ml how much you want to add %s (e.g. %s 1000): \n", getName(), getName());
        checkAndSetVal(val);
    }

    private void checkAndSetVal(int volumeMl) {
        if (volumeMl > 0){
            setVolume(getVolume() + volumeMl);
        } else {
            System.out.println("Enter the correct integer value (ml)");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
