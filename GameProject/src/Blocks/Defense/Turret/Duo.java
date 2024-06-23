/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Turret;

import Blocks.Base.Turret;
import Items.ItemType;
import Projectiles.Base.Projectile;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class Duo extends Turret {
    
    public Duo(int x, int y) {
        super(x, y);
        initBlock("Duo", 1, 325, 1, 1, Color.YELLOW, Color.GRAY);
        initTower(Projectile.class, 1, 0.25, 15, 3, 2);
        addNeededItem(ItemType.IronSheet, 10);
        addNeededItem(ItemType.IronRod, 5);
        addNeededItem(ItemType.Screws, 10);
        addProjectileCost(ItemType.IronOre, 1, 3);
        addProjectileCost(ItemType.CopperOre, 1, 2);
        addProjectileCost(ItemType.Ammo, 1, 6);
        addProjectileCost(ItemType.AdvancedAmmo, 1, 10);
    }
    
}
