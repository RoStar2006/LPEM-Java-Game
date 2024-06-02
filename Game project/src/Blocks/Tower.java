/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks;

import Core.Camera;
import Core.StatsManager;
import Core.Utils;
import Enemies.Enemy;
import Items.ItemType;
import Projectiles.Projectile;
import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Romeo
 */
public class Tower extends UpdatedBlock {
    private static final Color RANGE_COLOR = new Color(255, 255, 255, 128);
    private double range = 12; // In blocks
    private double spread = 0;
    private double projectileCount = 1;
    private double spreadStep = 0;
    private Class<? extends Projectile> pClass;
    
    private HashMap<ItemType, Integer> hmProjectileCost = new HashMap<>();
    
    private final Class[] paramTypesSub = { double.class, double.class, double.class, double.class, double.class, StatsManager.class, StatsManager.class }; // The costructor template for the projectile creation
    
    public Tower(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Tower);
        initBlock("Tower", 200, 1, 1, Color.ORANGE);
        initTower(Projectile.class, 1, 2, 12, 20, 10);
        addProjectileCost(ItemType.IronOre, 1);
    }
    
    protected void initTower(Class<? extends Projectile> pClass, double damage, double attackSpeed, double range, double spread, double projectileCount) {
        statsManager.init(damage, attackSpeed);
        this.pClass = pClass;
        this.range = range;
        this.spread = spread/2;
        this.projectileCount = projectileCount;
        this.spreadStep = spread / projectileCount;
    }
    
    protected void addProjectileCost(ItemType neededItem, int quantaty) {
        if(!hasInventory()) {
            createInventory(100, 1, false);
        }
        addWhitelistItem(neededItem);
        hmProjectileCost.put(neededItem, quantaty);
    }

    @Override
    public void update(double deltaTime, World world) {
        Enemy enemy = world.getEnemyManager().getNearestEnemy(getCenterX(), getCenterY(), range);
        
        statsManager.update(deltaTime);
        
        if(enemy != null) {
            while(statsManager.offCooldown() && consumeShot()) {
                statsManager.shoot();
                
                if(projectileCount > 1 || (projectileCount == 1 && spread == 0)) {
                    double curSpread = -spread;
                    
                    for(double i = 0; i < projectileCount; i++) {
                        shoot(world, enemy, curSpread);
                        curSpread += spreadStep;
                    }
                } else if(projectileCount == 1 && spread != 0) {
                    double randomSpread = Utils.nextDouble(-spread, spread);
                    
                    shoot(world, enemy, randomSpread);
                }
            }
        }
    }
    
    protected void shoot(World world, Enemy enemy, double spread) {
        double angle = Math.atan2(enemy.getCenterY() - getCenterY(), enemy.getCenterX() - getCenterX()) + Math.toRadians(spread);
        double xDir = Math.cos(angle);
        double yDir = Math.sin(angle);
        
        Object[] paramValuesSub = {getCenterX(), getCenterY(), xDir, yDir, range, statsManager, enemy.getStatsManager()};
        
        Projectile p = null;
        try {
            p = pClass.getDeclaredConstructor(paramTypesSub).newInstance(paramValuesSub); 
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("ERROR while creating projectile!");
        } finally {
            if(p != null) {
                world.getProjectileManager().add(p);
            }
        }
    }
    
    protected boolean consumeShot() {
        if(hmProjectileCost.isEmpty()) {
            return true;
        }
        
        for(Map.Entry<ItemType, Integer> entry : getInventory().entrySet()) {
            int req = hmProjectileCost.getOrDefault(entry.getKey(), -1);
            if(req <= entry.getValue() && req != -1) {
                removeItem(entry.getKey(), req);
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void drawIcon(Graphics g, int x, int y, int size) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, size/2, size/2);
        g.fillRect(x+size/2, y+size/2, size/2, size/2);
        g.setColor(Color.ORANGE);
        g.fillRect(x+size/2, y, size/2, size/2);
        g.fillRect(x, y+size/2, size/2, size/2);
    }

    @Override
    public void drawHovered(Camera c) {
        c.setColor(RANGE_COLOR);
        c.drawCircle(getCenterX()-range, getCenterY()-range, range*2);
    }
    
}
