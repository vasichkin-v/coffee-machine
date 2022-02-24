package com.internship.api;

import java.util.*;

public class Machine {
    private Integer coffeeContainer;
    private Integer waterContainer;
    private Integer milkContainer;
    private boolean isMachineWorking = true;
    List<RecipeCoffee> recipes = new ArrayList<>();
    List<String> nameResources = new ArrayList<>(Arrays.asList("water", "coffee", "milk"));
    private final Scanner in = new Scanner(System.in);


    public Machine(Integer coffee, Integer water, Integer milk) {
        this.coffeeContainer = coffee;
        this.waterContainer = water;
        this.milkContainer = milk;

        recipes.add(new RecipeCoffee("ristretto", 0.5, 0.5, 0d, 10));
        recipes.add(new RecipeCoffee("espresso", 0.3, 0.7, 0d, 10));
        recipes.add(new RecipeCoffee("lungo", 0.15, 0.85, 0d, 10));
        recipes.add(new RecipeCoffee("cappuccino", 0.15, 0.4, 0.45, 10));
        recipes.add(new RecipeCoffee("latte", 0.1, 0.3, 0.6, 10));
    }

    public void run() {
        try {
            if (checkResourcesMachine(in)) {
                System.out.println("Tell me what kind of coffee do you want? (e.g. Cappuccino 300 or add water 1000)");
                while (isMachineWorking()) {
                    RecipeCoffee userSelectedTypeCoffee = null;
                    String userEnterNameResource = null;
                    System.out.print("Enter command: ");
                    String inputUser = in.nextLine();
                    if(inputUser.toLowerCase().matches("turn off")){
                        setStatusMachineWorking(false);
                        System.out.println("Tefal who once thought of us...");
                        System.out.println("Bye!");
                        return;
                    }
                    String userInputNameResources = getCmdName(inputUser);
                    String userInputTypeCoffee = getCmdName(inputUser);
                    int mlVolume = getVolumeMl(inputUser);
                    Optional<RecipeCoffee> recipe = recipes.stream().filter(c -> c.getTitle().equals(userInputTypeCoffee)).findFirst();
                    Optional<String> resourceName = nameResources.stream().filter(n -> n.equals(userInputNameResources)).findFirst();

                    if(recipe.isPresent()){
                        userSelectedTypeCoffee = recipe.get();
                    } else if(resourceName.isPresent()) {
                        userEnterNameResource = resourceName.get();
                    } else {
                        System.out.println("Reading error! Possibly wrong command name.");
                        continue;
                    }

                    if (userSelectedTypeCoffee != null) {
                        if(getVolumeMl(inputUser) >= userSelectedTypeCoffee.getMinCookVolume()){
                            System.out.println("Accepted for execution...");
                            Thread.sleep(1000);
                            useIngredients(mlVolume, userSelectedTypeCoffee);
                        } else {
                            System.out.println("Sorry minimum volume for " + userSelectedTypeCoffee.getMinCookVolume() + " ml");
                        }
                    } else {
                        if (userEnterNameResource != null){
                            switch (userEnterNameResource){
                                case "water":
                                    checkAndSetWater(mlVolume);
                                    break;
                                case "milk":
                                    checkAndSetMilk(mlVolume);
                                    break;
                                case "coffee":
                                    checkAndSetCoffee(mlVolume);
                                    break;
                            }
                        } else {
                            throw ExceptionCoffeeMachine.CMD_IS_INCORRECT;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCurrentVolume() {
//        System.out.printf("Current volume of coffee: %d ml, water: %d ml, milk: %d ml.\n", getCoffeeContainer(), getWaterContainer(), getMilkContainer());
        System.out.printf("%d %d %d\n", getCoffeeContainer(), getWaterContainer(), getMilkContainer());
    }

    private void useIngredients(int mlVolumeCoffee, RecipeCoffee userSelectedTypeCoffee) throws ExceptionCoffeeMachine, InterruptedException {
        int minusCoffee = (int) (mlVolumeCoffee * userSelectedTypeCoffee.getCoffee());
        int minusWater = (int) (mlVolumeCoffee * userSelectedTypeCoffee.getWater());
        int minusMilk = (int) (mlVolumeCoffee * userSelectedTypeCoffee.getMilk());
        boolean isPrintCoffeeReady = true;

        if(getCoffeeContainer() >= minusCoffee){
            if(getWaterContainer() >= minusWater && getMilkContainer() >= minusMilk){
                setCoffeeContainer(getCoffeeContainer() - minusCoffee);
            }
        } else {
            if(getCoffeeContainer() == 0){
                System.out.println("Coffee is over!");
            } else {
                System.out.printf("For your coffee, you need to add at least %d ml more coffee.\n", -(getCoffeeContainer() - minusCoffee));
                isPrintCoffeeReady = false;
            }
            return;
        }
        if(getWaterContainer() >= minusWater){
            if(getCoffeeContainer() >= minusCoffee && getMilkContainer() >= minusMilk){
                setWaterContainer(getWaterContainer() - minusWater);
            }
        } else {
            if(getWaterContainer() == 0){
                System.out.println("Water is over!");
            } else {
                System.out.printf("For your coffee, you need to add at least %d ml more water.\n", -(getWaterContainer() - minusWater));
                isPrintCoffeeReady = false;
            }
            return;
        }
        if(getMilkContainer() >= minusMilk){
            setMilkContainer(getMilkContainer() - minusMilk);
        } else {
            if(getMilkContainer() == 0){
                System.out.println("Milk is over!");
            } else {
                System.out.printf("For your coffee, you need to add at least %d ml more milk .\n", -(getMilkContainer() - minusMilk));
                isPrintCoffeeReady = false;
            }
        }
        if (isPrintCoffeeReady) {
            System.out.printf("Your %s %d ml is ready!\n", userSelectedTypeCoffee.getTitle().substring(0, 1).toUpperCase() + userSelectedTypeCoffee.getTitle().substring(1), mlVolumeCoffee);
        }
        printCurrentVolume();
    }

    private boolean checkResourcesMachine(Scanner in) throws ExceptionCoffeeMachine, InterruptedException {
        System.out.println("Check in tanks ingredients ...");
        if(getCoffeeContainer() == null || getCoffeeContainer() == 0){
            updateContainerCoffee(in);
        }
        if(getWaterContainer() == null || getWaterContainer() == 0){
            updateContainerWater(in);
        }
        if(getMilkContainer() == null || getMilkContainer() == 0){
            updateContainerMilk(in);
        }
        return true;
    }

    private void updateContainerMilk(Scanner in) throws ExceptionCoffeeMachine, InterruptedException {
        System.out.print("Specify in ml how much you want to add milk (e.g. milk 1000): ");
        String input = in.nextLine();
        int volumeMl = getVolumeMl(input);
        checkAndSetMilk(volumeMl);
    }

    private void updateContainerWater(Scanner in) throws ExceptionCoffeeMachine, InterruptedException {
        System.out.print("Specify in ml how much you want to add water (e.g. water 1000): ");
        String input = in.nextLine();
        int volumeMl = getVolumeMl(input);
        checkAndSetWater(volumeMl);
    }

    private void updateContainerCoffee(Scanner in) throws ExceptionCoffeeMachine, InterruptedException {
        System.out.print("Specify in ml how much you want to add coffee (e.g. coffee 1000): ");
        String input = in.nextLine();
        int volumeMl = getVolumeMl(input);
        checkAndSetCoffee(volumeMl);
    }

    private void checkAndSetMilk(int volumeMl) throws InterruptedException, ExceptionCoffeeMachine {
        if (volumeMl > 0){
            setMilkContainer(getMilkContainer() + volumeMl);
            Thread.sleep(1000);
            printCurrentVolume();
        } else {
            System.out.println("Enter the correct integer value (ml)");
        }
    }

    private void checkAndSetWater(int volumeMl) throws InterruptedException {
        if (volumeMl > 0){
            setWaterContainer(getWaterContainer() + volumeMl);
            Thread.sleep(1000);
            printCurrentVolume();
        } else {
            System.out.println("Enter the correct integer value (ml)");
        }
    }

    private void checkAndSetCoffee(int volumeMl) throws InterruptedException {
        if (volumeMl > 0){
            setCoffeeContainer(getCoffeeContainer() + volumeMl);
            Thread.sleep(1000);
            printCurrentVolume();
        } else {
            System.out.println("Enter the correct integer value (ml)");
        }
    }

    private String getCmdName(String inputScanner) throws ExceptionCoffeeMachine {
        //return Pattern.compile("(\\W|\\d*)").matcher(inputScanner).replaceAll("").toLowerCase();
        final String[] s = inputScanner.trim().replaceAll("\\s{2,}", " ").split(" ");
        if (s.length == 1){
            System.err.printf("Mistake! Possibly incorrect name. %s\n", inputScanner);
            throw ExceptionCoffeeMachine.NAME_IS_INCORRECT;
        }
        return s[0].toLowerCase();
    }

    private int getVolumeMl(String inputScanner) throws ExceptionCoffeeMachine {
        //final String s = Pattern.compile("(\\.\\d)|[^(\\-?\\d)]*").matcher(inputScanner).replaceAll("");
        final String[] s = inputScanner.trim().replaceAll("\\s{2,}", " ").split(" ");
        if (s.length != 2 && !Character.isAlphabetic(s[1].charAt(0))){
            System.err.printf("Mistake! Possibly wrong volume. %s\n", inputScanner);
            throw ExceptionCoffeeMachine.VOLUME_IS_INCORRECT;
        }
        return Integer.parseInt(s[1].replaceAll("\\.(\\d*)*", ""));
    }

    public boolean isMachineWorking() {
        return isMachineWorking;
    }

    public void setStatusMachineWorking(boolean machineWorking) {
        this.isMachineWorking = machineWorking;
    }

    public Integer getCoffeeContainer() {
        return coffeeContainer == null ? 0: coffeeContainer;
    }

    public void setCoffeeContainer(Integer coffeeContainer) {
        this.coffeeContainer = coffeeContainer;
    }

    public Integer getWaterContainer() {
        return waterContainer == null ? 0: waterContainer;
    }

    public void setWaterContainer(Integer waterContainer) {
        this.waterContainer = waterContainer;
    }

    public Integer getMilkContainer() {
        return milkContainer == null ? 0: milkContainer;
    }

    public void setMilkContainer(Integer milkContainer) {
        this.milkContainer = milkContainer;
    }
}
