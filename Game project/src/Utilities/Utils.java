/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.util.Random;

/**
 * This class provides numerous simple mathematical methods all using a seeded {@link Random}.
 * @author Romeo
 */
public class Utils {
    private static Random RANDOM = new Random();
    
    public static void initRandom(long seed) {
        RANDOM.setSeed(seed);
    }
    
    /**
     * 
     * @return A random double between 0 (inclusive) and 1 (exclusive).
     */
    public static double nextDouble() {
        return RANDOM.nextDouble();
    }
    
    /**
     * 
     * @param max
     * @return A random double between 0 (inclusive) and max (exclusive).
     */
    public static double nextDouble(double max) {
        return RANDOM.nextDouble(max);
    }
    
    /**
     * 
     * @param min
     * @param max
     * @return A random double between min (inclusive) and max (exclusive).
     */
    public static double nextDouble(double min, double max) {
        return RANDOM.nextDouble(min, max);
    }
    
    /**
     * 
     * @param value
     * @param min
     * @param max
     * @return Returns the value clamped between the given min and max.
     */
    public static double clampDouble(double value, double min, double max) {
        if(min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
        
        if(value < min) {
            value = min;
        } else if(value > max) {
            value = max;
        }
        
        return value;
    }
    
    /**
     * 
     * @param a
     * Start value
     * @param b
     * End value
     * @param t
     * Interpolation value between 0 and 1.
     * @return 
     * This return a value which is t percent in between a and b
     */
    public static double lerpDouble(double a, double b, double t) {
        t = clampDouble(t, 0, 1);
        return a + (b - a) * t; // a*(1-t) + b*t
    }
    
    /**
     * 
     * @param value
     * @param places
     * @return 
     * Returns the value rounded to n places behind or after the decimal point
     */
    public static double roundDouble(double value, int places) {
        double exp = Math.pow(10, places);
        return (int)Math.round(value * exp) / exp;
    }
    
    
    /**
     * 
     * @return A random integer
     */
    public static int nextInt() {
        return RANDOM.nextInt();
    }
    
    /**
     * 
     * @param max
     * @return A random integer between 0 (inclusive) and max (exclusive).
     */
    public static int nextInt(int max) {
        return RANDOM.nextInt(max);
    }
    
    /**
     * 
     * @param min
     * @param max
     * @return A random integer between min (inclusive) and max (exclusive).
     */
    public static int nextInt(int min, int max) {
        return RANDOM.nextInt(min, max);
    }
    
    /**
     * 
     * @param value
     * @param min
     * @param max
     * @return Returns the value clamped between the given min and max.
     */
    public static int clampInt(int value, int min, int max) {
        if(min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        
        if(value < min) {
            value = min;
        } else if(value > max) {
            value = max;
        }
        
        return value;
    }
    
    /**
     * 
     * @param a
     * Start value
     * @param b
     * End value
     * @param t
     * Interpolation value between 0 and 1.
     * @return 
     * This return a value which is t percent in between a and b
     */
    public static int lerpInt(int a, int b, double t) {
        t = clampDouble(t, 0, 1);
        return (int)Math.round(a + (b - a) * t); // a*(1-t) + b*t
    }
    
    /**
     * 
     * @param value
     * @param places
     * @return 
     * Returns the value rounded to n places behind or after the decimal point
     */
    public static double roundInt(int value, int places) {
        double exp = Math.pow(10, places);
        return (int)Math.round(value * exp) / exp;
    }
    
    
    /**
     * 
     * @return A random boolean
     */
    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }
}
