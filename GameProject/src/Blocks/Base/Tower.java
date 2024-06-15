/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Core.Camera;
import Core.Utils;
import Enemies.Enemy;
import Items.Inventory;
import Items.ItemType;
import Menus.DetailsMenu;
import Projectiles.Base.Projectile;
import Projectiles.Base.ProjectilePool;
import World.World;
import java.awt.Color;
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
    private HashMap<ItemType, Double> hmProjectileDamage = new HashMap<>();
    private ItemType curAmmoType = null;
    
    public Tower(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Tower);
        initBlock("Tower", 200, 1, 1, Color.WHITE, Color.ORANGE);
        initTower(Projectile.class, 0.5, 12, 0, 1);
    }
    
    protected final void initTower(Class<? extends Projectile> pClass, double attackSpeed, double range, double spread, double projectileCount) {
        statsManager.init(1, attackSpeed);
        this.pClass = pClass;
        this.range = range;
        this.spread = spread/2;
        this.projectileCount = projectileCount;
        this.spreadStep = spread / projectileCount;
    }
    
    protected final void addProjectileCost(ItemType neededItem, int quantity, double damage) {
        if(!hasInventory()) {
            createInventory(100, 1, false);
        }
        addWhitelistItem(neededItem);
        hmProjectileCost.put(neededItem, quantity);
        hmProjectileDamage.put(neededItem, damage);
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
        double damage = hmProjectileDamage.getOrDefault(curAmmoType, statsManager.getDamage());
        Projectile p = ProjectilePool.get(pClass,  statsManager, enemy.getStatsManager(), spread, range, damage);
        
        if(p != null) {
            world.getProjectileManager().add(p);
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
                curAmmoType = entry.getKey();
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void drawHovered(Camera c) {
        c.setColor(RANGE_COLOR);
        c.drawCircle(getCenterX()-range, getCenterY()-range, range*2);
    }

    public double getRange() {
        return range;
    }

    public double getSpread() {
        return spread*2;
    }

    public double getProjectileCount() {
        return projectileCount;
    }
    
    public final ArrayList<String> formattedProjectileDetails() {
        ArrayList<String> strArr = new ArrayList<>();
        
        for(Map.Entry<ItemType, Integer> entry : hmProjectileCost.entrySet()) {
            strArr.add("<itemIcon>" + Inventory.formatName(entry.getKey()));
            strArr.add(DetailsMenu.addNumber("Damage:", hmProjectileDamage.get(entry.getKey())));
            strArr.add("Needed:" + entry.getValue());
        }
        
        return strArr;
    }
}
