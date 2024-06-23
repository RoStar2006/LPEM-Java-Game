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
public class DoomBringer extends Turret {
    public DoomBringer(int x, int y) {
        super(x, y);
        initBlock("Doom Bringer", 1, 300, 2, 2, Color.YELLOW, Color.ORANGE);
        initTower(Projectile.class,25, 1.75, 0, 30, 5, 15);
        addNeededItem(ItemType.CircuitBoard, 30);
        addNeededItem(ItemType.CopperCable, 25);
        addNeededItem(ItemType.TitaniumSheet, 25);
        addNeededItem(ItemType.Screws, 40);
        setRequiredEnergy(20);
    }
}
