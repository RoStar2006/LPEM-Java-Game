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
public class Havoc extends Turret {
    public Havoc(int x, int y) {
        super(x, y);
        initBlock("Havoc", 1, 300, 1, 1, Color.YELLOW, Color.DARK_GRAY);
        initTower(Projectile.class, 0.75, 0, 7, 20, 20);
        addNeededItem(ItemType.CircuitBoard, 10);
        addNeededItem(ItemType.IronSheet, 10);
        addNeededItem(ItemType.SteelIngot, 3);
        addNeededItem(ItemType.Screws, 10);
        addProjectileCost(ItemType.IronOre, 1, 2);
        addProjectileCost(ItemType.CopperOre, 1, 1);
        addProjectileCost(ItemType.Ammo, 1, 4);
        addProjectileCost(ItemType.AdvancedAmmo, 1, 6);
    }
}
