/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import Blocks.Base.Block;
import Enemies.Enemy;
import World.World;

/**
 *
 * @author Romeo
 */
public class StatsManager {
    private double damage;
    private double maxHealth;
    private double health;
    private double attackSpeed;
    private double curAttackCooldown;
    private Block block;
    private Enemy enemy;
    private long lastDamageTime = 0; // ms
    private final int damageDisplaySpan = 3000; // ms
    
    public StatsManager(double maxHealth) {
        init(maxHealth);
    }

    public StatsManager(double maxHealth, double damage, double attackSpeed) {
        init(maxHealth, damage, attackSpeed);
    }
    
    public void init(double maxHealth, double damage, double attackSpeed) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }
    
    public void init(double damage, double attackSpeed) {
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }
    
    public void init(double maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }
    
    public void update(double deltaTime) {
        if(curAttackCooldown > 0) {
            curAttackCooldown -= deltaTime;
        }
    }
    
    public void doDamage(StatsManager otherStatsManager, World world) {
        while(offCooldown()) {
            curAttackCooldown += attackSpeed;
            otherStatsManager.takeDamage(damage, world);
        }
    }
    
    public boolean shoot() {
        if(offCooldown()) {
            curAttackCooldown += attackSpeed;
            return true;
        } else {
            return false;
        }
    }
    
    public void forceDamage(StatsManager otherStatsManager, double damage, World world) {
        otherStatsManager.takeDamage(damage, world);
    }
    
    public void takeDamage(double amount, World world) {
        health -= amount;
        lastDamageTime = System.currentTimeMillis();
        
        if(health <= 0) {
            health = 0;
            if(isBlock()) {
                world.removeBlock(block.getX(), block.getY(), false);
            }
        }
    }
    
    public void regenerateFlat(double amount) {
        health += amount;
        lastDamageTime = System.currentTimeMillis();
        
        if(health > maxHealth) {
            health = maxHealth;
        }
    }
    
    public void regeneratePerc(double percentage) {
        regenerateFlat(maxHealth*percentage);
    }
    
    /**
     * This method should only be used by the {@link PlannedBlock} class.
     * It regenerates n percent of health without showing the health bar
     * @param percentage 
     */
    public void hiddenRegen(double percentage) {
        health += maxHealth*percentage;
        
        if(health > maxHealth) {
            health = maxHealth;
        }
    }
    
    /**
     * This method should only be used by the {@link World} class.
     * It sets the health to 1 without showing the health bar
     */
    public void setToOneHP() {
        health = 1;
    }
    
    /*
        Getters and setters
    */
    public void setBlock(Block block) {
        this.block = block;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
    
    public boolean offCooldown() {
        return curAttackCooldown <= 0 && attackSpeed > 0;
    }
    
    public boolean isBlock() {
        return block != null;
    }
    
    public boolean isEnemy() {
        return enemy != null;
    }
    
    public boolean isDead() {
        return health <= 0;
    }
    
    public double getRadius() {
        if(isEnemy()) {
            return enemy.getRadius();
        }
        
        return -1;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getDamage() {
        return damage;
    }
    
    public boolean showDamage() {
        return (System.currentTimeMillis() - lastDamageTime) < damageDisplaySpan;
    }

    public double getAttackRate() {
        return 1.0/attackSpeed;
    }
    
    public double getCenterX() {
        if(isBlock()) {
            return block.getCenterX();
        } else if(isEnemy()) {
            return enemy.getCenterX();
        }
        
        return 0; // should never return 0, because the stats manager is only applied to blocks and enemies
    }
    
    public double getCenterY() {
        if(isBlock()) {
            return block.getCenterY();
        } else if(isEnemy()) {
            return enemy.getCenterY();
        }
        
        return 0; // should never return 0, because the stats manager is only applied to blocks and enemies
    }
}
