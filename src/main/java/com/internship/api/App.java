package com.internship.api;


import java.util.Arrays;

public class App
{
    public static void main( String[] args )
    {
        new Machine(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])).run();
    }
}
