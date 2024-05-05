package Core;


import Utilities.Utils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * This class is the core of the game and calls every update() and draw() method
 * 
 * @author Romeo
 */
public class MainLogic {
    private Point screenRes;
    private long seed = 1;
    private long lastTime = 0;
    /**
     * Delta time in seconds
     */
    private static double deltaTime = 0;
    
    private Camera camera;
    private Controls controls;

    /**
     * @param screenRes This is needed to center the camera to the middle of the screen
     */
    public MainLogic(Point screenRes) {
        this.screenRes = screenRes;
        Utils.initRandom(seed);
        lastTime = System.nanoTime();
        
        camera = new Camera(screenRes.x/2, screenRes.y/2);
        controls = new Controls(camera);
    }
    
    /**
     * This manages the game's physics and other core methods like update(), ...
     */
    public void update() {
        deltaTime = (System.nanoTime() - lastTime) / Math.pow(10, 9);
        lastTime = System.nanoTime();
        
        camera.move();
    }
    
    /**
     * This manages the draw() calls of every object
     * @param g 
     */
    public void draw(Graphics g) {
        camera.updateGraphics(g);
        
        Color color = new Color(255, 255, 255);
        int size = 8;
        for(int i = -screenRes.x/2; i < screenRes.x/2; i += size) {
            double xPerc = ((double)(i+screenRes.x/2)/(screenRes.x));
            for(int j = -screenRes.y/2; j < screenRes.y/2; j += size) {
                double yPerc = ((double)(j+screenRes.y/2)/(screenRes.y));
                double zPerc = 1 - xPerc;
                Color color2 = new Color((int)(color.getRed() * xPerc), (int)(color.getGreen() * yPerc), (int)(color.getBlue() * zPerc));
                camera.setColor(color2);
                camera.fillRect(i, j, size, size);
            }
        }
        
        camera.setColor(Color.yellow);
        camera.fillRect(0, 0, size, size);
        camera.drawCamera();
    }
    
    public static double getDeltaTime() {
        return deltaTime;
    }

    public Controls getControls() {
        return controls;
    }
}
