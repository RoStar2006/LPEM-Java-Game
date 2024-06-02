package Core;


import Blocks.Block;
import Menus.MenuManager;
import World.WorldManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import World.*;
import java.util.ArrayList;

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
    private final int borderSpacing = 17;
    private final Color voidColor = new Color(22, 15, 30);
    private final Color borderColor = new Color(60, 45, 67, 255/borderSpacing);
    private ArrayList<Block> alBorder = new ArrayList<>();
    

    /**
     * @param screenRes This is needed to center the camera to the middle of the screen
     */
    public MainLogic(Point screenRes) {
        this.SCREEN_RES = screenRes;
        lastTime = System.nanoTime();
        
        camera = new Camera(screenRes.x/2, screenRes.y/2);
        menuManager = new MenuManager(camera);
        controls = new Controls(camera, menuManager);
        
        worldManager = new WorldManager(menuManager);
        worldManager.generateWorld(seed, 256, 256);
        camera.setPos(worldManager.getCore().getX()+1.5, worldManager.getCore().getY()+1.5);
        
        generateBorder();
    }
    
    /**
     * This manages the game's physics and other core methods like update(), ...
     */
    public void update() {
        deltaTime = (System.nanoTime() - lastTime) / Math.pow(10, 9) * MainLogic.GAME_SPEED;
        lastTime = System.nanoTime();
        
        worldManager.update(deltaTime);
        
        camera.move();
    }
    
    /**
     * This manages the draw() calls of every object
     * @param g 
     */
    public void draw(Graphics g) {
        camera.updateGraphics(g);
        drawBackground(g);
        
        worldManager.draw(camera, g);
        
        camera.drawCamera();
    }
    
    private void drawBackground(Graphics g) {
        g.setColor(voidColor);
        g.fillRect(0, 0, SCREEN_RES.x, SCREEN_RES.y);
        
        for(Block border : alBorder) {
            border.draw(camera);
        }
    }
    
    /**
     * Generates the border around the game world.
     * This only needs to be called, when a new world is generated
     */
    private void generateBorder() {
        alBorder = new ArrayList<>();
        
        /*for(int i = 1; i <= borderSpacing; i++) {
            alBorder.add(new Block(-i, -borderSpacing+i-1, WorldManager.WORLD_SIZE_X+i*2, WorldManager.WORLD_SIZE_Y+(borderSpacing-i+1)*2, borderColor));
        }*/
        System.err.println("Disabled world border generation");
    }
    
    public static double getDeltaTime() {
        return deltaTime;
    }

    public Controls getControls() {
        return controls;
    }
}
