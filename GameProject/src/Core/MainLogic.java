package Core;


import Menus.MenuManager;
import World.WorldManager;
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
    public static Point SCREEN_RES;
    private long seed = 1;
    public final static double GAME_SPEED = 2;
    private boolean paused = false;
    private boolean gameOver = false;
    
    private final int worldSizeX = 256;
    private final int worldSizeY = 256;
    
    private Camera camera;
    private Controls controls;
    private WorldManager worldManager;
    private MenuManager menuManager;
    
    /**
     * Delta time in seconds
     */
    private static double deltaTime = 0;
    private long lastTime = 0;
    
    /*
        World border stuff
    */
    private final Color voidColor = new Color(22, 15, 30);
    private final Color borderColor = new Color(31, 22, 38);
    
    private static double curConveyorAnim = 0;
    private static double curEnergyAnim = 0;

    /**
     * @param screenRes This is needed to center the camera to the middle of the screen
     */
    public MainLogic(Point screenRes) {
        MainLogic.SCREEN_RES = screenRes;
        lastTime = System.nanoTime();
        
        camera = new Camera(screenRes.x/2, screenRes.y/2);
        menuManager = new MenuManager(this, camera);
        controls = new Controls(camera, menuManager);
        
        worldManager = new WorldManager(menuManager);
        worldManager.generateWorld(this, seed, worldSizeX, worldSizeY);
        camera.setPos(worldManager.getCore().getX()+worldManager.getCore().getWidth()/2.0, worldManager.getCore().getY()+worldManager.getCore().getHeight()/2.0);
    }
    
    /**
     * This manages the game's physics and other core methods like update(), ...
     */
    public void update() {
        deltaTime = (System.nanoTime() - lastTime) / Math.pow(10, 9);
        lastTime = System.nanoTime();
        
        double speed = GAME_SPEED;
        
        while(speed >= 1) {    
            updateGame(deltaTime);
            speed--;
        }
        
        if(speed < 1 && speed > 0) {
            updateGame(deltaTime*speed);
        }
        
        if(menuManager.canInteract() || gameOver) {
            camera.move();
        }
    }
    
    private void updateGame(double deltaTime) {
        curConveyorAnim += deltaTime;
        curEnergyAnim += deltaTime;
        if(!paused && !gameOver) {
            worldManager.update(deltaTime);
        }
    }

    public static double getCurConveyorAnim() {
        return curConveyorAnim;
    }

    public static double getCurEnergyAnim() {
        return curEnergyAnim;
    }
    
    /**
     * This manages the draw() calls of every object
     * @param g 
     */
    public void draw(Graphics g) {
        camera.updateGraphics(g);
        drawBackground(g);
        
        worldManager.draw(camera, g);
        
        // camera.drawCamera();
    }
    
    private void drawBackground(Graphics g) {
        g.setColor(voidColor);
        g.fillRect(0, 0, SCREEN_RES.x, SCREEN_RES.y);
        
        camera.fillRect(0, 0, worldSizeX, worldSizeY, borderColor);
    }
    
    public static double getDeltaTime() {
        return deltaTime;
    }

    public Controls getControls() {
        return controls;
    }

    public boolean isPaused() {
        return paused;
    }

    public void togglePaused() {
        if(!gameOver) {
            this.paused = !paused;
        }
    }
    
    public void gameOver() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
