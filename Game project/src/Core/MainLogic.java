package Core;


import World.Generator;
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
    private Point screenRes;
    private long seed = 1;
    
    private World world;
    private Camera camera;
    private Controls controls;
    
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
        this.screenRes = screenRes;
        lastTime = System.nanoTime();
        
        camera = new Camera(screenRes.x/2, screenRes.y/2);
        controls = new Controls(camera);
        
        world = Generator.generateWorld(seed, 64, 64);
        generateBorder();
    }
    
    /**
     * This manages the game's physics and other core methods like update(), ...
     */
    public void update() {
        deltaTime = (System.nanoTime() - lastTime) / Math.pow(10, 9);
        lastTime = System.nanoTime();
        
        ArrayList<UpdatedBlock> alUpdatedBlocks = world.getAlUpdateBlocks();
        for(int i = alUpdatedBlocks.size()-1; i >= 0; i--) {
            UpdatedBlock updatedBlock = alUpdatedBlocks.get(i);
            
            if(updatedBlock == null || updatedBlock.isDeleted()) {
                alUpdatedBlocks.remove(i);
                continue;
            }
            
            updatedBlock.update(deltaTime);
        }
        
        camera.move();
    }
    
    /**
     * This manages the draw() calls of every object
     * @param g 
     */
    public void draw(Graphics g) {
        camera.updateGraphics(g);
        
        drawBackground(g);
        world.draw(camera);
        
        camera.drawCamera();
    }
    
    private void drawBackground(Graphics g) {
        g.setColor(voidColor);
        g.fillRect(0, 0, screenRes.x, screenRes.y);
        
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
        
        for(int i = 1; i <= borderSpacing; i++) {
            alBorder.add(new Block(-i, -borderSpacing+i-1, Generator.WORLD_SIZE_X+i*2, Generator.WORLD_SIZE_Y+(borderSpacing-i+1)*2, borderColor));
        }
    }
    
    public static double getDeltaTime() {
        return deltaTime;
    }

    public Controls getControls() {
        return controls;
    }
}
