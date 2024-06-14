/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies;

import Core.Camera;
import Blocks.Base.Block;
import Blocks.Base.Core;
import Core.StatsManager;
import World.World;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class Enemy {
    private double x, y;
    /**
     * blocks per second
     */
    private int speed;
    private Core core;
    private double size;
    private double range;
    private StatsManager statsManager;
    
    private double xDir, yDir;

    public Enemy(double x, double y, Core core) {
        this.x = x;
        this.y = y;
        this.core = core;
        
        statsManager = new StatsManager(10);
        statsManager.setEnemy(this);
        
        initEnemy(10, 1, 0.75, 1, 0.5, 7);
        
        calcDir();
    }
    
    protected void initEnemy(double maxHealth, double damage, double attackSpeed, double range, double size, int speed) {
        statsManager.init(maxHealth, damage, attackSpeed);
        this.speed = speed;
        this.range = range;
        this.size = size;
    }
    
    public void move(double deltaTime, World w) {
        Block fB = getBlockInFront(w);
        Block rB = getBlockInRange(w);
        
        if(fB == null) {
            x += xDir * speed * deltaTime;
            y += yDir * speed * deltaTime;
        } 
        
        if(fB != null) {
            statsManager.update(deltaTime);
            statsManager.doDamage(fB.getStatsManager(), w);
        } else if(rB != null) {
            statsManager.update(deltaTime);
            statsManager.doDamage(rB.getStatsManager(), w);
        }
    }
    
    private void calcDir() {
        double angle = Math.atan2(core.getY()+1 - y, core.getX()+1 - x);
        xDir = Math.cos(angle);
        yDir = Math.sin(angle);
    }
    
    private Block getBlockInFront(World world) {
        double lX = x + xDir;
        double lY = y + yDir;
        
        for(int i = 0; i < range; i++) {
            Block block = world.getBlock((int)Math.round(lX + i * Math.signum(xDir)), (int)Math.round(lY + i * Math.signum(yDir)));
            if(block != null) {
                return block;
            }
        }
        
        return null;
    }
    
    private Block getBlockInRange(World world) {
        Block result = null;
        double smallestDist = -1;
        int x1 = (int)Math.floor(x + size - range);
        int x2 = (int)Math.ceil(x + size + range);
        int y1 = (int)Math.floor(y + size - range);
        int y2 = (int)Math.ceil(x + size + range);
        
        for(int x = x1; x < x2; x++) {
            for(int y = y1; y < y2; y++) {
                Block block = world.getBlock(x, y);
                
                if(block != null) {
                    double d = calcDist(block);
                    if(smallestDist == -1 || d < smallestDist) {
                        smallestDist = d;
                        result = block;
                    }
                }
            }
        }
        
        return result;
    }
    
    private double calcDist(Block block) {
        double vX = block.getX() + block.getWidth()/2.0 - x + size;
        double vY = block.getY() + block.getHeight()/2.0 - y + size;
        
        return Math.sqrt(vX*vX + vY*vY);
    }
    
    public int getX() {
        return (int)Math.round(x);
    }
    
    public double getCenterX() {
        return x + size;
    }
    
    public int getY() {
        return (int)Math.round(y);
    }
    
    public double getCenterY() {
        return y + size;
    }
    
    public int getSize() {
        return (int)Math.ceil(size);
    }
    
    public double getRadius() {
        return size/2.0;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
    
    public void draw(Camera c) {
        // Enemy debug
        // c.drawRect((int)Math.floor(x + size - range), (int)Math.floor(y + size - range), (int)Math.ceil(x + size + range)-(int)Math.floor(x + size - range), (int)Math.ceil(y + size + range)-(int)Math.floor(y + size - range), Color.CYAN); // range block perfect
        // c.drawRect(x + size - range, y + size - range, range*2, range*2, Color.CYAN); // range
//        for(int i = 0; i < range; i++) { // blocks in front
//            c.drawRect((int)Math.round(x + xDir + i * Math.signum(xDir)), (int)Math.round(y + yDir + i * Math.signum(yDir)), 1, 1, Color.yellow);
//        }
        // c.drawRect(Math.round(x) - WorldManager.ENEMY_BUILD_DIST, Math.round(y) - WorldManager.ENEMY_BUILD_DIST, WorldManager.ENEMY_BUILD_DIST*2+1, WorldManager.ENEMY_BUILD_DIST*2+1, Color.yellow); // Forbidden build area
        // c.drawRect(x + size/2, y + size/2, size, size, Color.GREEN); // Draw a square around the enemy
        c.fillCircle(x + size/2, y + size/2, size, Color.RED);
    }
    
}
