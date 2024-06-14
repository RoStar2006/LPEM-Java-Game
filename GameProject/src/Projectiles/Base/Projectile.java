/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projectiles.Base;

import Core.Camera;
import Core.MainLogic;
import Core.StatsManager;
import Core.Utils;
import Enemies.Enemy;
import World.World;
import World.WorldManager;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Romeo
 */
public class Projectile {
    private double startX, startY;
    private double endX, endY;
    protected double x, y;
    private double xDir, yDir;
    private StatsManager source;
    private StatsManager target;
    /**
     * Positive speed -> Normal projectile behavior <br>
     * Negative speed -> The decay time of the projectile
     */
    private double speed;
    private double damage;
    private double width;
    /**
     * Positive length -> Normal projectile behavior <br>
     * Negative length -> The length spans from the start to the target
     */
    private double length;
    private boolean fullLength = false;
    
    private boolean hit = false;
    private double curDecay = 0;
    private boolean extendedToTarget = false;
    
    protected Color coreColor = new Color(255, 164, 0);
    protected Color trailColor = new Color(255, 164, 0, 64);
    
    private double lastStepX, lastStepY;
    private Line2D.Double line2D;
    private Point2D.Double point2D;
    
    public Projectile(double startX, double startY, double xDir, double yDir, double range, double damage, StatsManager source, StatsManager target) {
        fullInit(startX, startY, xDir, yDir, range, damage, source, target);
        
        init(10, 0.25, 0.75);
    }
    
    public final void fullInit(double startX, double startY, double xDir, double yDir, double range, double damage, StatsManager source, StatsManager target) {
        this.startX = startX;
        this.x = startX;
        this.startY = startY;
        this.y = startY;
        this.source = source;
        this.target = target;
        this.xDir = xDir;
        this.yDir = yDir;
        this.endX = startX+xDir*range;
        this.endY = startY+yDir*range;
        this.hit = false;
        this.curDecay = 0;
        this.damage = damage;
        
        point2D = new Point2D.Double(target.getCenterX(), target.getCenterY());
        line2D = new Line2D.Double(startX, startY, startX, startY);
    }
    
    protected void init(double speed, double width, double length) {
        this.speed = speed;
        this.width = width;
        this.length = length;
    }

    protected void setFullLength(boolean fullLength) {
        this.fullLength = fullLength;
    }
    
    /**
     * 
     * @param deltaTime
     * @param world
     * @return Returns true, if something was hit
     */
    public boolean update(double deltaTime, World world) {
        return move(deltaTime) & checkCollision(world); // single "&" to run both methods, even if the first one is false
    }
    
    protected boolean move(double deltaTime) {
        if(speed > 0) {
            lastStepX = xDir * speed * deltaTime;
            lastStepY = yDir * speed * deltaTime;
            x = Utils.clampDouble(x+lastStepX, startX, endX);
            y = Utils.clampDouble(y+lastStepY, startY, endY);
            
            line2D.setLine(x-lastStepX, y-lastStepY, x, y);
            
            return true;
        } else {
            curDecay -= deltaTime;
            if(!extendedToTarget) {
                if(!fullLength) {
                    double distance = Math.sqrt(Math.pow(startX-target.getCenterX(), 2) + Math.pow(startY-target.getCenterY(), 2));
                    x = startX+xDir*distance;
                    y = startY+yDir*distance;
                } else {
                    x = endX;
                    y = endY;
                }
                
                extendedToTarget = true;
            }
            line2D.setLine(startX, startY, x, y);
            
            return isDecayed();
        }
    }
    
    protected boolean checkCollision(World world) {
        if(hit || isDecayed()) {
            return true;
        }
        
        boolean result = false;
        
        if(target.isEnemy()) {
            result = checkEnemyCollision(world);
        } else if(target.isBlock()) {
            System.err.println("Projectile block collision is not implemented!");
        }
        
        return (x == endX && y == endY) || result;
    }
    
    private boolean checkEnemyCollision(World world) {
        for(Enemy enemy : world.getEnemyManager().getAlEnemies()) {
            if(!enemy.getStatsManager().isDead()) {
                point2D.setLocation(enemy.getCenterX(), enemy.getCenterY());

                if(line2D.ptSegDist(point2D) <= enemy.getRadius()) {
                    source.forceDamage(enemy.getStatsManager(), damage, world);
                    hit = true;
                    if(length >= 0) {
                        return true;
                    }
                }
            }
        }
        
        return hit;
    }
    
    private boolean isDecayed() {
        return speed <= 0 && curDecay <= speed;
    }
    
    protected double getTrailWidth() {
        return WorldManager.BLOCK_SIZE*width;
    }
    
    protected double getTrailStartX() {
        if(length >= 0) {
            return Utils.clampDouble(x-xDir*length, startX, endX);
        }
        return startX;
    }
    
    protected double getTrailStartY() {
        if(length >= 0) {   
            return Utils.clampDouble(y-yDir*length, startY, endY);
        }
        return startY;
    }
    
    protected double getTrailOffsetX() {
        return xDir*width/2.0;
    }
    
    protected double getTrailOffsetY() {
        return yDir*width/2.0;
    }
    
    public void draw(Camera c) {
        // c.drawLine(getTrailStartX(), getTrailStartY(), x-getTrailOffsetX(), y-getTrailOffsetY(), getTrailWidth(), trailColor);
        c.fillCircle(x-width/2.0, y-width/2.0, width, coreColor);
    }
}
