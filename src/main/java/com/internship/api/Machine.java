package com.internship.api;

import java.util.*;
import java.util.stream.Collectors;

public class Machine {
    List<RecipeCoffee> recipes = new ArrayList<>();
    List<Ingredient> ingredients = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);


    public Machine(String[] args) {
        recipes.add(new RecipeCoffee("ristretto", 0.5, 0.5, 0d, 10));
        recipes.add(new RecipeCoffee("espresso", 0.3, 0.7, 0d, 10));
        recipes.add(new RecipeCoffee("lungo", 0.15, 0.85, 0d, 10));
        recipes.add(new RecipeCoffee("cappuccino", 0.15, 0.4, 0.45, 10));
        recipes.add(new RecipeCoffee("latte", 0.1, 0.3, 0.6, 10));

        ingredients.add(new Ingredient("coffee", args.length >= 2? Integer.parseInt(args[1]) : 0));
        ingredients.add(new Ingredient("water", args.length >= 1? Integer.parseInt(args[0]) : 0));
        ingredients.add(new Ingredient("milk", args.length >= 2? Integer.parseInt(args[2]) : 0));
    }

    public void run() {
        try {
            checkResourcesMachine(scanner);
            System.out.println("Tell me what kind of coffee do you want? (e.g. Cappuccino 300 or add water 1000)");
            while (true) {
                System.out.print("Enter command: ");
                String inputUser = scanner.nextLine();
                String userInputTypeCoffee = getCommand(inputUser);
                String userInputNameResources = getCommand(inputUser);
                int mlVolume = getVolumeMl(inputUser);
                Optional<RecipeCoffee> recipeOpt = recipes.stream().filter(c -> c.getTitle().equals(userInputTypeCoffee)).findFirst();
                Optional<Ingredient> ingredientOpt = ingredients.stream().filter(n -> n.getName().equals(userInputNameResources)).findFirst();

                if (recipeOpt.isPresent()) {
                    RecipeCoffee userSelectedTypeCoffee = recipeOpt.get();
                    if (mlVolume >= userSelectedTypeCoffee.getMinCookVolume()) {
                        System.out.println("Accepted for execution...");
                        useIngredients(mlVolume, userSelectedTypeCoffee);
                    } else {
                        System.out.println("Sorry minimum volume for " + userSelectedTypeCoffee.getMinCookVolume() + " ml");
                    }
                } else if (ingredientOpt.isPresent()) {
                    final Ingredient ingredient = ingredientOpt.get();
                    ingredient.updateContainer(mlVolume);
                    printCurrentVolume();
                } else {
                    throw ExceptionCoffeeMachine.NAME_IS_INCORRECT;
                }
            }
        } catch (ExceptionCoffeeMachine e) {
            if (e.getCode() == 103) {
                printBye();
            } else {
                System.out.println("Custom error: " + e.getMessage());
                run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ReRun Coffee machine");
            run();
        }
    }

    private void useIngredients(int mlOrder, RecipeCoffee selectedTypeCoffee) throws ExceptionCoffeeMachine {
        int minusCoffee = (int) (mlOrder * selectedTypeCoffee.getCoffee());
        int minusWater = (int) (mlOrder * selectedTypeCoffee.getWater());
        int minusMilk = (int) (mlOrder * selectedTypeCoffee.getMilk());
        boolean isPrintCoffeeReady = true;

        if(ingredients.get(0).getVolume() >= minusCoffee){
            if(ingredients.get(1).getVolume() >= minusWater && ingredients.get(2).getVolume() >= minusMilk){
                ingredients.get(0).setVolume(ingredients.get(0).getVolume() - minusCoffee);
            }
        } else {
            if(ingredients.get(0).getVolume() == 0){
                System.out.println("Coffee is over!");
            } else {
                System.out.printf("For your coffee, you need to add at least %d ml more coffee.\n", -(ingredients.get(0).getVolume() - minusCoffee));
                isPrintCoffeeReady = false;
            }
            return;
        }
        if(ingredients.get(1).getVolume() >= minusWater){
            if(ingredients.get(0).getVolume() >= minusCoffee && ingredients.get(2).getVolume() >= minusMilk){
                ingredients.get(1).setVolume(ingredients.get(1).getVolume() - minusWater);
            }
        } else {
            if(ingredients.get(2).getVolume() == 0){
                System.out.println("Water is over!");
            } else {
                System.out.printf("For your coffee, you need to add at least %d ml more water.\n", -(ingredients.get(1).getVolume() - minusWater));
                isPrintCoffeeReady = false;
            }
            return;
        }
        if(ingredients.get(2).getVolume() >= minusMilk){
            ingredients.get(2).setVolume(ingredients.get(2).getVolume() - minusMilk);
        } else {
            if(ingredients.get(2).getVolume() == 0){
                System.out.println("Milk is over!");
            } else {
                System.out.printf("For your coffee, you need to add at least %d ml more milk .\n", -(ingredients.get(2).getVolume() - minusMilk));
                isPrintCoffeeReady = false;
            }
        }

        if (isPrintCoffeeReady) {
            System.out.printf("Your %s %d ml is ready!\n", selectedTypeCoffee.getTitle().substring(0, 1).toUpperCase() + selectedTypeCoffee.getTitle().substring(1), mlOrder);
        }
        printCurrentVolume();
    }

    private boolean checkResourcesMachine(Scanner in) {
        System.out.println("Check in tanks ingredients ...");
        ingredients.forEach(el -> {
            if(el.getVolume() == null || el.getVolume() == 0){
                 // el.updateContainer(getVolumeMl(in.nextLine()));
                System.out.printf("Not enough %s!\n", el.getName());
            }
        });
        return true;
    }

    private String getCommand(String inputScanner) throws ExceptionCoffeeMachine {
        if(inputScanner == null || inputScanner.isEmpty()) return "";
        final String s = inputScanner.trim().replaceAll("\\s{2,}", " ").toLowerCase();
        if(s.equals("turn off")) throw ExceptionCoffeeMachine.TURN_OFF;
        final String[] s2 = s.split(" ");
        if (s2.length == 1){
            System.err.printf("Mistake! Possibly incorrect command. %s\n", inputScanner);
        }
        return s2[0];
    }

    private int getVolumeMl(String inputScanner) throws ExceptionCoffeeMachine {
        getCommand(inputScanner);
        final String[] s = inputScanner.trim().replaceAll("\\s{2,}", " ").split(" ");
        if (s.length != 2 || Character.isAlphabetic(s[1].charAt(0))){
            System.err.printf("Mistake! Possibly wrong volume. %s\n", inputScanner);
            throw ExceptionCoffeeMachine.VOLUME_IS_INCORRECT;
        }
        return Integer.parseInt(s[1].replaceAll("\\.(\\d*)*", ""));
    }

    private void printCurrentVolume() {
        System.out.println(ingredients.stream().map(el ->{return el.getVolume().toString();}).collect(Collectors.joining(" ")));
    }

    private void printBye() {
        System.out.println("Tefal who once thought of us...");
        System.out.println("Bye!");
    }
}
