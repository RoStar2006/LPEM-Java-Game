/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Defense.Turret;

import Blocks.Base.Turret;
import Projectiles.Trail;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class Laser extends Turret {
    
    public Laser(int x, int y) {
        super(x, y);
        initBlock("Laser", 1, 275, 1, 1, Color.YELLOW, Color.YELLOW);
        initTower(Trail.class, 5, 2, 0, 20, 10, 3);
        setRequiredEnergy(2);
    }
    
}
