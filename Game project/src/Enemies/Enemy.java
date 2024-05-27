/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies;

import Core.Camera;
import Blocks.Block;
import Blocks.Core;
import World.World;
import World.WorldManager;
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
    private int speed = 7;
    private Core core;
    private double size = 0.5;
    private double range = 7;
    private double damage = 1;
    private double attackSpeed = 0.75;
    private double curAttackCooldown = attackSpeed;
    
    private double xDir, yDir;

    public Enemy(double x, double y, Core core) {
        this.x = x;
        this.y = y;
        this.core = core;
        
        calcDir();
    }
    
    public void move(double deltaTime, World w) {
        Block fB = getBlockInFront(w);
        Block rB = getBlockInRange(w);
        
        if(fB == null) {
            x += xDir * speed * deltaTime;
            y += yDir * speed * deltaTime;
        } 
        
        if(fB != null) {
            curAttackCooldown -= deltaTime;
            if(curAttackCooldown <= 0) {
                curAttackCooldown += attackSpeed;
                fB.damage(damage, w);
            }
        } else if(rB != null) {
            curAttackCooldown -= deltaTime;
            if(curAttackCooldown <= 0) {
                curAttackCooldown += attackSpeed;
                rB.damage(damage, w);
            }
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
        
        for(int i = 0; i <= range; i++) {
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
        int x2 = (int)Math.ceil(x1 + range*2);
        int y1 = (int)Math.floor(y + size - range);
        int y2 = (int)Math.ceil(y1 + range*2);
        
        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y <= y2; y++) {
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
    
    public int getY() {
        return (int)Math.round(y);
    }
    
    public int getSize() {
        return (int)Math.ceil(size);
    }
    
    public void draw(Camera c) {
        // Enemy debug
//        c.drawRect(x + size - range, y + size - range, range*2, range*2, Color.CYAN); // range
//        for(int i = 0; i < range; i++) { // blocks in front
//            c.drawRect((int)Math.round(x + xDir + i * Math.signum(xDir)), (int)Math.round(y + yDir + i * Math.signum(yDir)), 1, 1, Color.yellow);
//        }
        // c.drawRect(Math.round(x) - WorldManager.ENEMY_BUILD_DIST, Math.round(y) - WorldManager.ENEMY_BUILD_DIST, WorldManager.ENEMY_BUILD_DIST*2+1, WorldManager.ENEMY_BUILD_DIST*2+1, Color.yellow); // Forbidden build area
        c.fillCircle(x + size/2, y + size/2, size, Color.RED);
    }
    
}
