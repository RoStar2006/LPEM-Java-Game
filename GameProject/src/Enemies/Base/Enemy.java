/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies.Base;

import Blocks.Base.Block;
import Core.Camera;
import Blocks.Base.Core;
import Core.StatsManager;
import World.World;
import World.WorldManager;
import java.awt.Color;
import java.awt.geom.Point2D;

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
    private Color color;
    
    private Point2D.Double blockPoint = new Point2D.Double();
    
    private double angle, xDir, yDir;
    
    /**
     * Chance from 0 to 100% for this enemy to spawn.
     * This chance is used to calculate the enemy's weight.
     */
    private double chance = 100;
    /**
     * How much the enemy costs for it to spawn.
     */
    private int cost = 1;
    private double weight = 0;

    public Enemy(double x, double y, Core core) {
        statsManager = new StatsManager(10);
        statsManager.setEnemy(this);
        
        init(x, y, core);
        initEnemy(10, 1, 0.75, 1, 7, 0.5, Color.RED);
    }
    
    public final void init(double x, double y, Core core) {
        this.x = x;
        this.y = y;
        this.core = core;
        
        if(core != null) {
            calcDir(core);
        }
    }
    
    protected final void initEnemy(double maxHealth, double damage, double attackSpeed, double range, int speed, double size, Color color) {
        statsManager.init(maxHealth, damage, attackSpeed);
        this.speed = speed;
        this.range = range;
        this.size = size;
        this.color = color;
    }
    
    /**
     * 
     * @param chance
     * Chance from 0 to 100% for this enemy to spawn.
     * This chance is used to calculate the enemy's weight.
     * @param cost 
     * How much the enemy costs for it to spawn.
     */
    protected final void initWeight(double chance, int cost) {
        this.chance = chance;
        this.cost = cost;
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
    
    private void calcDir(Block block) {
        angle = Math.atan2(block.getCenterY() - y, block.getCenterX() - x);
        xDir = Math.cos(angle);
        yDir = Math.sin(angle);
    }
    
    private Block getBlockInFront(World world) {
        for(int i = -(int)Math.ceil(size); i < Math.ceil(range); i++) { // blocks in front
            int lX = (int)Math.round(x + (i + 1) * xDir);
            int lY = (int)Math.round(y + (i + 1) * yDir);
            Block b = world.getBlock(lX, lY);
            
            for(int j = 1; j <= Math.ceil(size); j++) {
                if(b == null) {
                    b = world.getBlock(lX, lY + (int)Math.signum(yDir)*j);
                } 
                if(b == null) {
                    b = world.getBlock(lX + (int)Math.signum(xDir)*j, lY);
                }
                
                if(b != null) {
                    break;
                }
            }
            
            if(b != null && calcDist(b) <= getAttackRange()) {
                return b;
            }
        }
        
        return null;
    }
    
    private Block getBlockInRange(World world) {
        Block result = null;
        double smallestDist = -1;
        int x1 = (int)Math.floor(x - getAttackRange());
        int x2 = (int)Math.ceil(x + getAttackRange());
        int y1 = (int)Math.floor(y - getAttackRange());
        int y2 = (int)Math.ceil(y + getAttackRange());
        
        for(int lX = x1; lX < x2; lX++) {
            for(int lY = y1; lY < y2; lY++) {
                Block block = world.getBlock(lX, lY);
                
                if(block != null) {
                    double d = calcDist(block);
                    if(d <= getAttackRange() && (smallestDist == -1 || d < smallestDist)) {
                        smallestDist = d;
                        result = block;
                    }
                }
            }
        }
        
        return result;
    }
    
    private double calcDist(Block block) {
        Point2D.Double pointOnRect = calcPointOnRect(block);
        
        double vX = x - pointOnRect.x;
        double vY = y - pointOnRect.y;
        
        return Math.sqrt(vX*vX + vY*vY);
    }
    
    /**
     * This method calculates the point on a rectangle (block), where the enemy line of sight intersects first. <br>
     * Source: https://stackoverflow.com/questions/4061576/finding-points-on-a-rectangle-at-a-given-angle
     * @param block
     * @return 
     */
    private Point2D.Double calcPointOnRect(Block block) {
        double theta = Math.PI - angle;
        double rectAtan = Math.atan2(block.getHeight(), block.getWidth());
        double tanTheta = Math.tan(theta);
        int region;

        if ((theta > -rectAtan) && (theta <= rectAtan)) {
            region = 1;
        } else if ((theta > rectAtan) && (theta <= (Math.PI - rectAtan))) {
            region = 2;
        } else if ((theta > (Math.PI - rectAtan)) || (theta <= -(Math.PI - rectAtan))) {
            region = 3;
        } else {
            region = 4;
        }
        
        int xFactor = 1;
        int yFactor = 1;

        switch (region) {
          case 1 -> yFactor = -1;
          case 2 -> yFactor = -1;
          case 3 -> xFactor = -1;
          case 4 -> xFactor = -1;
        }

        if((region == 1) || (region == 3)) {
            blockPoint.x = block.getCenterX() + xFactor * (block.getWidth() / 2.0);
            blockPoint.y = block.getCenterY() + yFactor * (block.getWidth() / 2.0) * tanTheta;
        } else {
            blockPoint.x = block.getCenterX() + xFactor * (block.getHeight() / (2.0 * tanTheta));
            blockPoint.y = block.getCenterY() + yFactor * (block.getHeight() /  2.0);
        }

        return blockPoint;
    }
    
    private double getOffsetX() {
        return x - size/2;
    }
    
    private double getOffsetY() {
        return y - size/2;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public double getRadius() {
        return size / 2;
    }
    
    private double getAttackRange() {
        return range + getRadius();
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public double getChance() {
        return chance;
    }

    public int getCost() {
        return cost;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public void draw(Camera c) {
        // Enemy debug
        // c.drawCircle(getOffsetX() - range, getOffsetY() - range, range*2+size, Color.CYAN); // range
        /*for(int i = -(int)Math.ceil(size); i < Math.ceil(range); i++) { // blocks in front
            int lX = (int)Math.round(x + (i + 1) * xDir);
            int lY = (int)Math.round(y + (i + 1) * yDir);
            
            c.drawRect(lX, lY, 1, 1, Color.yellow);
            for(int j = 1; j <= Math.ceil(size); j++) {
                c.drawRect(lX, lY + (int)Math.signum(yDir) * j, 1, 1, Color.red);
                c.drawRect(lX + (int)Math.signum(xDir) * j, lY, 1, 1, Color.green);
            }
        }*/
        // c.drawRect(Math.round(x) - WorldManager.ENEMY_BUILD_DIST, Math.round(y) - WorldManager.ENEMY_BUILD_DIST, WorldManager.ENEMY_BUILD_DIST*2+1, WorldManager.ENEMY_BUILD_DIST*2+1, Color.yellow); // Forbidden build area
        // c.drawRect(getOffsetX(), getOffsetY(), size, size, Color.GREEN); // Draw a square around the enemy
        c.fillCircle(getOffsetX(), getOffsetY(), size, color);
    }
    
}
