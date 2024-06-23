/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Turret;

import Blocks.Base.Turret;
import Items.ItemType;
import Projectiles.Trail;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class LaserTurret extends Turret {
    
    public LaserTurret(int x, int y) {
        super(x, y);
        initBlock("Laser Turret", 1, 275, 1, 1, Color.YELLOW, Color.YELLOW);
        initTower(Trail.class, 5, 2, 0, 17, 10, 3);
        setRequiredEnergy(2.5);
        addNeededItem(ItemType.CircuitBoard, 5);
        addNeededItem(ItemType.CopperCable, 20);
        addNeededItem(ItemType.IronSheet, 15);
    }
    
}
